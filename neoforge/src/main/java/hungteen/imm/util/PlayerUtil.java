package hungteen.imm.util;

import hungteen.htlib.api.registry.RangeNumber;
import hungteen.htlib.util.WeightedList;
import hungteen.htlib.util.helper.MathHelper;
import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.IMMConfigs;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.enums.ExperienceTypes;
import hungteen.imm.api.enums.RealmStages;
import hungteen.imm.api.registry.*;
import hungteen.imm.common.RealmManager;
import hungteen.imm.common.capability.player.PlayerDataManager;
import hungteen.imm.common.command.IMMCommand;
import hungteen.imm.common.impl.registry.*;
import hungteen.imm.common.spell.SpellTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-24 16:23
 **/
public class PlayerUtil {

    public static boolean notConsume(Player player){
        return player.getAbilities().instabuild;
    }

    public static boolean isCreativeOrSpectator(Player player){
        return player.isCreative() || player.isSpectator();
    }

    public static void setCoolDown(Player player, Item item, int coolDown){
        player.getCooldowns().addCooldown(item, coolDown);
    }

    public static boolean sitToMeditate(Player player, BlockPos pos, float yOffset, boolean relyOnBlock){
        if(! isSitInMeditation(player)){
            final Vec3 vec = MathHelper.toVec3(pos);
            List<Monster> list = player.level().getEntitiesOfClass(Monster.class, MathHelper.getAABB(MathHelper.toVec3(pos), 8F, 5F), (entity) -> {
                return entity.isPreventingPlayerRest(player);
            });
            if(list.isEmpty()){
                if(EntityUtil.sitToMeditate(player, pos, yOffset, relyOnBlock)){
                    setIntegerData(player, PlayerRangeIntegers.MEDITATE_TICK, 1);
                    return true;
                }
            } else {
                PlayerHelper.sendTipTo(player, TipUtil.info("unsafe_surround").withStyle(ChatFormatting.RED));
            }
        }
        return false;
    }

    public static void quitMeditate(Player player){
        if(isSitInMeditation(player)){
            setIntegerData(player, PlayerRangeIntegers.MEDITATE_TICK, 0);
            player.stopRiding();
        }
    }

    public static boolean isSitInMeditation(Player player){
        return getIntegerData(player, PlayerRangeIntegers.MEDITATE_TICK) > 0;
    }

    /**
     * Add distance parameter.
     */
    public static boolean stillValid(ContainerLevelAccess levelAccess, Player player, Block block, double distance) {
        return levelAccess.evaluate((level, blockPos) -> level.getBlockState(blockPos).is(block) && player.distanceToSqr(MathHelper.toVec3(blockPos)) <= distance, true);
    }

    /**
     * 往背包添加物品，如果失败则掉落。
     * @return True if not add into inventory.
     */
    public static boolean addItem(Player player, ItemStack stack){
        if (player.addItem(stack)) {
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
            return true;
        } else {
            ItemEntity itementity = player.drop(stack, false);
            if (itementity != null) {
                itementity.setNoPickUpDelay();
                itementity.setThrower(player);
            }
            return false;
        }
    }

    public static boolean isInCD(Player player, Item item){
        return player.getCooldowns().isOnCooldown(item);
    }

    /**
     * 玩家灵根的生成规则： <br>
     * 1. 首先依据概率选择是几个灵根（0 - 5）。 <br>
     * 2. 如果是1个灵根，那么依据权重在普通灵根和异灵根中选择一个。 <br>
     * 3. 否则依据权重在普通五行灵根中选择若干个。 <br>
     */
    public static List<ISpiritualType> getSpiritualRoots(RandomSource random){
        final double[] rootChances = {IMMConfigs.getNoRootChance(), IMMConfigs.getOneRootChance(), IMMConfigs.getTwoRootChance(), IMMConfigs.getThreeRootChance(), IMMConfigs.getFourRootChance()};
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
        if(IMMAPI.get().spiritualRegistry().isPresent()){
            if(rootCount == 1){
                rootChosen.addAll(WeightedList.create(SpiritualTypes.registry().getValues().stream().toList()).getRandomItems(random, 1, true));
            } else if(rootCount > 1){
                rootChosen.addAll(WeightedList.create(IMMAPI.get().spiritualRegistry().get().getValues().stream().filter(ISpiritualType::isCommonRoot).collect(Collectors.toList())).getRandomItems(random, rootCount, true));
            }
        }
        return rootChosen;
    }

    public static Optional<PlayerDataManager> getOptManager(Player player) {
        return Optional.ofNullable(getManager(player));
    }

    @Nullable
    public static PlayerDataManager getManager(Player player) {
//        return PlayerCapabilityManager.getManager(player, CapabilityHandler.PLAYER_CAP);
        return null;
    }

    public static <T> T getManagerResult(Player player, Function<PlayerDataManager, T> function, T defaultValue) {
//        return PlayerCapabilityManager.getManagerResult(player, CapabilityHandler.PLAYER_CAP, function, defaultValue);
        return defaultValue;
    }

    /* Operations About Spiritual Roots */

    public static void addSpiritualRoot(Player player, ISpiritualType root){
        getOptManager(player).ifPresent(l -> l.addSpiritualRoot(root));
    }

    public static void removeSpiritualRoot(Player player, ISpiritualType root){
        getOptManager(player).ifPresent(l -> l.removeSpiritualRoot(root));
    }

    public static void clearSpiritualRoot(Player player){
        getOptManager(player).ifPresent(PlayerDataManager::clearSpiritualRoot);
    }

    /**
     * 重置玩家的灵根。
     * {@link IMMCommand}
     */
    public static void resetSpiritualRoots(Player player){
        getOptManager(player).ifPresent(l -> {
            l.clearSpiritualRoot();
            getSpiritualRoots(player.getRandom()).forEach(l::addSpiritualRoot);
        });
    }

    /* Operations About Spells */

    public static void learnSpell(Player player, ISpellType spell, int level) {
        getOptManager(player).ifPresent(l -> l.learnSpell(spell, level));
    }

    public static void forgetSpell(Player player, ISpellType spell){
        getOptManager(player).ifPresent(l -> l.forgetSpell(spell));
    }

    public static void learnAllSpell(Player player, int level) {
        getOptManager(player).ifPresent(l -> {
            SpellTypes.registry().getValues().forEach(spell -> {
                l.learnSpell(spell, level);
            });
        });
    }

    public static void forgetAllSpell(Player player){
        getOptManager(player).ifPresent(PlayerDataManager::forgetAllSpells);
    }

    public static void setSpellAt(Player player, int pos, @Nullable ISpellType spell){
        getOptManager(player).ifPresent(l -> {
            if(spell == null){
                l.removeSpellAt(pos);
            } else {
                // 没有学会则先学。
                if(! l.hasLearnedSpell(spell, 1)){
                    l.learnSpell(spell, 1);
                }
                l.setSpellAt(pos, spell);
            }
        });
    }

    public static void removeSpellAt(Player player, int pos){
        getOptManager(player).ifPresent(l -> l.removeSpellAt(pos));
    }

    public static void cooldownSpell(Player player, ISpellType spell, long num){
        getOptManager(player).ifPresent(l -> l.cooldownSpell(spell, num));
    }

    @Nullable
    public static ISpellType getSpellAt(Player player, int pos) {
        return getManagerResult(player, m -> m.getSpellAt(pos), null);
    }

    public static boolean isSpellOnCircle(Player player, ISpellType spell) {
        return getManagerResult(player, m -> m.isSpellOnCircle(spell), false);
    }

    public static boolean isSpellOnCoolDown(Player player, ISpellType spell) {
        return getManagerResult(player, m -> m.isSpellOnCoolDown(spell), false);
    }

    public static float getSpellCDValue(Player player, ISpellType spell) {
        return getManagerResult(player, m -> m.getSpellCoolDown(spell), 0F);
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

    /* Sect Related Methods */

    public static float getSectRelation(Player player, ISectType sectType){
        return getManagerResult(player, m -> m.getSectRelation(sectType), 0F);
    }

    public static void setSectRelation(Player player, ISectType sectType, float value){
        getOptManager(player).ifPresent(l -> l.setSectRelation(sectType, value));
    }

    public static void addSectRelation(Player player, ISectType sectType, float value){
        getOptManager(player).ifPresent(l -> l.addSectRelation(sectType, value));
    }

    /* Experience Related Methods */

    public static void setExperience(Player player, ExperienceTypes type, float value){
        getOptManager(player).ifPresent(l -> l.setExperience(type, value));
    }

    public static void addExperience(Player player, ExperienceTypes type, float value){
        getOptManager(player).ifPresent(l -> l.addExperience(type, value));
    }

    public static float getExperience(Player player, ExperienceTypes type){
        return getManagerResult(player, l -> l.getExperience(type), 0F);
    }

    public static float getCultivation(Player player){
        return getManagerResult(player, PlayerDataManager::getCultivation, 0F);
    }

    /* Operations about Player Range Data */

    public static void setIntegerData(Player player, RangeNumber<Integer> rangeData, int value){
        getOptManager(player).ifPresent(l -> l.setIntegerData(rangeData, value));
    }

    public static void addIntegerData(Player player, RangeNumber<Integer> rangeData, int value){
        getOptManager(player).ifPresent(l -> l.addIntegerData(rangeData, value));
    }

    public static int getIntegerData(Player player, RangeNumber<Integer> rangeData){
        return getManagerResult(player, m -> m.getIntegerData(rangeData), rangeData.defaultData());
    }

    public static void setFloatData(Player player, RangeNumber<Float> rangeData, float value){
        getOptManager(player).ifPresent(l -> l.setFloatData(rangeData, value));
    }

    public static void addFloatData(Player player, RangeNumber<Float> rangeData, float value){
        getOptManager(player).ifPresent(l -> l.addFloatData(rangeData, value));
    }

    public static float getFloatData(Player player, RangeNumber<Float> rangeData){
        return getManagerResult(player, m -> m.getFloatData(rangeData), rangeData.defaultData());
    }

    public static float getMana(Player player){
        return getFloatData(player, PlayerRangeFloats.SPIRITUAL_MANA);
    }

    public static float getMaxMana(Player player){
        return getManagerResult(player, PlayerDataManager::getFullManaValue, 0F);
    }

    public static boolean isManaFull(Player player){
        return getManagerResult(player, l -> {
            return l.getFloatData(PlayerRangeFloats.SPIRITUAL_MANA) >= l.getFullManaValue();
        }, true);
    }

    public static int getSpellCircleMode(Player player){
        return getManagerResult(player, l -> l.getIntegerData(PlayerRangeIntegers.SPELL_CIRCLE_MODE), 0);
    }

    public static void setSpellCircleMode(Player player, int mode){
        getOptManager(player).ifPresent(l -> l.setIntegerData(PlayerRangeIntegers.SPELL_CIRCLE_MODE, mode));
    }

    public static boolean knowSpiritualRoots(Player player){
        return getManagerResult(player, l -> l.getIntegerData(PlayerRangeIntegers.KNOW_SPIRITUAL_ROOTS) == 1, false);
    }

    /**
     * 学习了业元素精通=知道了业元素的存在。
     */
    public static boolean knowSpiritElement(Player player){
        return getManagerResult(player, l -> l.hasLearnedSpell(SpellTypes.SPIRIT_MASTERY, 1), false);
    }

    /* Misc Operations */

    public static List<ISpiritualType> filterSpiritRoots(Player player, List<ISpiritualType> roots){
        return roots.stream().filter(root -> {
            if(root == SpiritualTypes.SPIRIT) {
                return knowSpiritElement(player);
            }
            return true;
        }).toList();
    }

    public static List<Element> filterElements(Player player, List<Element> elements){
        return elements.stream().filter(element -> {
            if(element == Element.SPIRIT) {
                return knowSpiritElement(player);
            }
            return true;
        }).toList();
    }

    public static boolean hasRoot(Player player, ISpiritualType root){
        return getManagerResult(player, l -> l.hasRoot(root), false);
    }

    public static List<ISpiritualType> getSpiritualRoots(Player player){
        return getManagerResult(player, PlayerDataManager::getSpiritualRoots, List.of());
    }

    public static IRealmType getPlayerRealm(Player player){
        return getManagerResult(player, PlayerDataManager::getRealmType, RealmTypes.MORTALITY);
    }

    public static RealmStages getPlayerRealmStage(Player player){
        return getManagerResult(player, PlayerDataManager::getRealmStage, RealmStages.PRELIMINARY);
    }

    /**
     * Only used on client side.
     */
    public static void clientSetRealm(Player player, IRealmType realm){
        checkAndSetRealm(player, realm, RealmStages.PRELIMINARY, false);
    }

    /**
     * 尝试直接改变境界。
     * @return 是否改变成功。
     */
    public static boolean checkAndSetRealm(Player player, IRealmType realm, @NotNull RealmStages stage, boolean force){
        // 自身修为达到了此境界的要求。
        return Boolean.TRUE.equals(getManagerResult(player, m -> {
            if (EntityHelper.isServer(player)) {
                // 自身修为达到了此境界的要求。
                if (m.getCultivation() >= RealmManager.getStageRequiredCultivation(realm, stage)) {
                    m.setRealmType(realm);
                    m.setRealmStage(stage);
                } else {
                    if (force) {
                        final float requiredXp = RealmManager.getStageRequiredCultivation(realm, stage) / ExperienceTypes.values().length;
                        for (ExperienceTypes type : ExperienceTypes.values()) {
                            m.setExperience(type, requiredXp);
                        }
                        m.setRealmType(realm);
                        m.setRealmStage(stage);
                    }
                    return force;
                }
            } else {
                m.setRealmType(realm);
            }
            return true;
        }, false));
    }

    /**
     * 直接改变境界阶段。
     * @return 是否改变成功。
     */
    public static boolean checkAndSetRealmStage(Player player, RealmStages stage){
        final AtomicBoolean success = new AtomicBoolean(true);
        getOptManager(player).ifPresent(m -> {
            if(EntityHelper.isServer(player)) {
                // 自身修为达到了此境界阶段的要求。
                if(m.getCultivation() >= RealmManager.getStageRequiredCultivation(m.getRealmType(), stage)) {
                    m.setRealmStage(stage);
                } else {
                    m.setFloatData(PlayerRangeFloats.BREAK_THROUGH_PROGRESS, 0F);
                    success.set(false);
                }
            } else {
                m.setRealmStage(stage);
            }
        });
        return success.get();
    }

    public static float getEachMaxCultivation(Player player){
        return getManagerResult(player, l -> RealmManager.getEachCultivation(l.getRealmType()), 0F);
    }

    public static float getMaxCultivation(Player player){
        return getManagerResult(player, l -> {
            final RealmStages nextStage = RealmStages.next(l.getRealmStage());
            return RealmManager.getStageRequiredCultivation(l.getRealmType(), nextStage);
        }, 0F);
    }

    public static boolean reachThreshold(Player player){
        return getPlayerRealmStage(player).canLevelUp() || getCultivation(player) >= getMaxCultivation(player);
    }

    public static ICultivationType getCultivationType(Player player){
        return getManagerResult(player, l -> {
            return l.getRealmType().getCultivationType();
        }, CultivationTypes.MORTAL);
    }

    @Nullable
    public static ISpellType getPreparingSpell(Player player){
        return getManagerResult(player, PlayerDataManager::getPreparingSpell, null);
    }

    public static void setPreparingSpell(Player player, @Nullable ISpellType type){
        getOptManager(player).ifPresent(m -> m.setPreparingSpell(type));
    }

}
