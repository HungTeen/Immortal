package hungteen.immortal.utils;

import hungteen.htlib.api.interfaces.IRangeNumber;
import hungteen.htlib.common.capability.PlayerCapabilityManager;
import hungteen.htlib.util.WeightList;
import hungteen.immortal.ImmortalConfigs;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.registry.IRealmType;
import hungteen.immortal.api.registry.ISpellType;
import hungteen.immortal.api.registry.ISpiritualType;
import hungteen.immortal.common.capability.CapabilityHandler;
import hungteen.immortal.common.capability.player.PlayerDataManager;
import hungteen.immortal.common.command.ImmortalCommand;
import hungteen.immortal.common.impl.PlayerRangeNumbers;
import hungteen.immortal.common.impl.RealmTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-24 16:23
 **/
public class PlayerUtil {

    public static void setCoolDown(Player player, Item item, int coolDown){
        player.getCooldowns().addCooldown(item, coolDown);
    }

    public static boolean addItem(Player player, ItemStack stack){
        if (player.addItem(stack)) {
            player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
            return true;
        } else {
            ItemEntity itementity = player.drop(stack, false);
            if (itementity != null) {
                itementity.setNoPickUpDelay();
                itementity.setOwner(player.getUUID());
            }
            return false;
        }
    }

    public static boolean isInCD(Player player, Item item){
        return player.getCooldowns().isOnCooldown(item);
    }

    public static HitResult getHitResult(Player player) {
        final double range = 20; //TODO 神识决定距离。
        final Vec3 startVec = player.getEyePosition(1.0F);
        final Vec3 lookVec = player.getViewVector(1.0F);
        Vec3 endVec = startVec.add(lookVec.scale(range));
        BlockHitResult blockHitResult = player.level.clip(new ClipContext(startVec, endVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        if(blockHitResult.getType() != HitResult.Type.MISS){
            endVec = blockHitResult.getLocation();
        }
        final AABB aabb = player.getBoundingBox().inflate(range);
        final EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(player.level, player, startVec, endVec, aabb, (entity) -> {
            return !entity.isSpectator();
        });
        return entityHitResult != null ? entityHitResult : blockHitResult;
    }

    /**
     * 重置玩家的灵根。
     * {@link ImmortalCommand}
     */
    public static void resetSpiritualRoots(Player player){
        getOptManager(player).ifPresent(l -> {
            l.clearSpiritualRoot();
            getSpiritualRoots(player.getRandom()).forEach(l::addSpiritualRoot);
        });
    }

    /**
     * 玩家灵根的生成规则： <br>
     * 1. 首先依据概率选择是几个灵根（0 - 5）。 <br>
     * 2. 如果是1个灵根，那么依据权重在普通灵根和异灵根中选择一个。 <br>
     * 3. 否则依据权重在普通五行灵根中选择若干个。 <br>
     */
    public static List<ISpiritualType> getSpiritualRoots(RandomSource random){
        final double[] rootChances = {ImmortalConfigs.getNoRootChance(), ImmortalConfigs.getOneRootChance(), ImmortalConfigs.getTwoRootChance(), ImmortalConfigs.getThreeRootChance(), ImmortalConfigs.getFourRootChance()};
        double chance = random.nextDouble();
        for(int i = 0; i < rootChances.length; ++ i){
            if(chance < rootChances[i]){
                return randomSpawnRoots(random, i);
            }
            chance -= rootChances[i];
        }
        return randomSpawnRoots(random, 5);
    }

    /**
     * {@link #getSpiritualRoots(RandomSource)}
     */
    private static List<ISpiritualType> randomSpawnRoots(RandomSource random, int rootCount){
        final List<ISpiritualType> rootChosen = new ArrayList<>();
        if(ImmortalAPI.get().spiritualRegistry().isPresent()){
            if(rootCount == 1){
                final WeightList<ISpiritualType> weightList = new WeightList<>(ImmortalAPI.get().spiritualRegistry().get().getValues(), ISpiritualType::getWeight);
                rootChosen.addAll(weightList.getRandomItems(random, 1, true));
            } else if(rootCount > 1){
                final WeightList<ISpiritualType> weightList = new WeightList<>(ImmortalAPI.get().spiritualRegistry().get().getValues().stream().filter(ISpiritualType::isCommonRoot).collect(Collectors.toList()), ISpiritualType::getWeight);
                rootChosen.addAll(weightList.getRandomItems(random, rootCount, true));
            }
        }

        return rootChosen;
    }

    public static Optional<PlayerDataManager> getOptManager(Player player) {
        return Optional.ofNullable(getManager(player));
    }

    @Nullable
    public static PlayerDataManager getManager(Player player) {
        return PlayerCapabilityManager.getManager(player, CapabilityHandler.PLAYER_CAP);
    }

    public static <T> T getManagerResult(Player player, Function<PlayerDataManager, T> function, T defaultValue) {
        return PlayerCapabilityManager.getManagerResult(player, CapabilityHandler.PLAYER_CAP, function, defaultValue);
    }

    /* Operations About Spells */

    public static void learnSpell(Player player, ISpellType spell, int level) {
        getOptManager(player).ifPresent(l -> l.learnSpell(spell, level));
    }

    public static void forgetSpell(Player player, ISpellType spell){
        getOptManager(player).ifPresent(l -> l.forgetSpell(spell));
    }

    public static void learnAllSpell(Player player, int level) {
        getOptManager(player).ifPresent(l -> l.learnAllSpells(level));
    }

    public static void forgetAllSpell(Player player){
        getOptManager(player).ifPresent(PlayerDataManager::forgetAllSpells);
    }

    public static void setSpellList(Player player, int pos, ISpellType spell){
        getOptManager(player).ifPresent(l -> l.setSpellList(pos, spell));
    }

    public static void removeSpellList(Player player, int pos, ISpellType spell){
        getOptManager(player).ifPresent(l -> l.removeSpellList(pos, spell));
    }

    public static void addSpellSet(Player player, ISpellType spell){
        getOptManager(player).ifPresent(l -> l.addSpellSet(spell));
    }

    public static void removeSpellSet(Player player, ISpellType spell){
        getOptManager(player).ifPresent(l -> l.removeSpellSet(spell));
    }

    public static void clearSpellSet(Player player){
        getOptManager(player).ifPresent(PlayerDataManager::clearSpellSet);
    }

    public static void cooldownSpell(Player player, ISpellType spell, long num){
        getOptManager(player).ifPresent(l -> l.cooldownSpell(spell, num));
    }

    @Nullable
    public static ISpellType getSpellAt(Player player, int pos) {
        return getManagerResult(player, m -> m.getSpellAt(pos), null);
    }

    public static boolean isSpellOnCooldown(Player player, ISpellType spell) {
        return getManagerResult(player, m -> m.isSpellOnCoolDown(spell), false);
    }

    public static double getSpellCDValue(Player player, ISpellType spell) {
        return getManagerResult(player, m -> m.getSpellCoolDown(spell), 0D);
    }

    public static boolean hasLearnedSpell(Player player, ISpellType spell) {
        return hasLearnedSpell(player, spell, 1);
    }

    public static boolean hasLearnedSpell(Player player, ISpellType spell, int level) {
        return getManagerResult(player, m -> m.hasLearnedSpell(spell, level), false);
    }

    public static int getSpellLevel(Player player, ISpellType spell) {
        return getManagerResult(player, m -> m.getSpellLevel(spell), 0);
    }

    public static boolean hasPassiveSpell(Player player, ISpellType spell, int level) {
        return getManagerResult(player, m -> m.hasPassiveSpell(spell, level), false);
    }

    public static boolean hasPassiveSpell(Player player, ISpellType spell) {
        return getManagerResult(player, m -> m.hasPassiveSpell(spell), false);
    }

    /* Operations about Integer Data */

    public static void setIntegerData(Player player, IRangeNumber<Integer> rangeData, int value){
        getOptManager(player).ifPresent(l -> l.setIntegerData(rangeData, value));
    }

    public static void addIntegerData(Player player, IRangeNumber<Integer> rangeData, int value){
        getOptManager(player).ifPresent(l -> l.addIntegerData(rangeData, value));
    }

    public static int getIntegerData(Player player, IRangeNumber<Integer> rangeData){
        return getManagerResult(player, m -> m.getIntegerData(rangeData), rangeData.defaultData());
    }

    public static int getSpiritualMana(Player player){
        return getIntegerData(player, PlayerRangeNumbers.SPIRITUAL_MANA);
    }

    public static int getFullSpiritualMana(Player player){
        return getManagerResult(player, PlayerDataManager::getFullManaValue, 0);
    }

    public static int getCultivation(Player player){
        return getManagerResult(player, PlayerDataManager::getLimitManaValue, 0);
    }

    public static boolean requireSyncCircle(Player player){
        return getManagerResult(player, PlayerDataManager::requireSyncCircle, false);
    }

    public static boolean useDefaultCircle(Player player){
        return getManagerResult(player, PlayerDataManager::useDefaultCircle, true);
    }

    /* Misc Operations */

    public static List<ISpiritualType> getSpiritualRoots(Player player){
        return getManagerResult(player, PlayerDataManager::getSpiritualRoots, List.of());
    }

    public static IRealmType getPlayerRealm(Player player){
        return getManagerResult(player, PlayerDataManager::getRealmType, RealmTypes.MORTALITY);
    }

    /**
     * 直接改变境界，会降低修为，同时灵气值归零。
     */
    public static void setRealm(Player player, IRealmType realm){
        getOptManager(player).ifPresent(m -> {
            m.setRealmType(realm);
            if(! player.level.isClientSide){
                m.setIntegerData(PlayerRangeNumbers.CULTIVATION, realm.requireCultivation());
                m.setIntegerData(PlayerRangeNumbers.SPIRITUAL_MANA, 0);
            }
        });
    }

}
