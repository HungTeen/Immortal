package hungteen.immortal.utils;

import hungteen.htlib.interfaces.IRangeData;
import hungteen.htlib.util.WeightList;
import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.immortal.ImmortalConfigs;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.registry.IRealm;
import hungteen.immortal.api.registry.ISpell;
import hungteen.immortal.api.registry.ISpiritualRoot;
import hungteen.immortal.common.capability.CapabilityHandler;
import hungteen.immortal.common.capability.player.PlayerCapability;
import hungteen.immortal.common.capability.player.PlayerDataManager;
import hungteen.immortal.common.command.ImmortalCommand;
import hungteen.immortal.impl.PlayerDatas;
import hungteen.immortal.impl.Realms;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

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

    public static boolean isInCD(Player player, Item item){
        return player.getCooldowns().isOnCooldown(item);
    }
    /**
     * 重置玩家的灵根。
     */
    public static void resetSpiritualRoots(Player player){
        List<ISpiritualRoot> roots = getSpiritualRoots(player.getRandom());
        getOptManager(player).ifPresent(l -> {
            l.clearSpiritualRoot();
            roots.forEach(r -> {
                l.addSpiritualRoot(r);
            });
        });
    }

    /**
     * 玩家灵根的生成规则：
     * 1. 首先依据概率选择是几个灵根（0 - 5）。
     * 2. 如果是1个灵根，那么依据权重在普通灵根和异灵根中选择一个。
     * 3. 否则依据权重在普通五行灵根中选择若干个。
     */
    public static List<ISpiritualRoot> getSpiritualRoots(RandomSource random){
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
    private static List<ISpiritualRoot> randomSpawnRoots(RandomSource random, int rootCount){
        final List<ISpiritualRoot> rootChosen = new ArrayList<>();
        if(rootCount == 1){
            final WeightList<ISpiritualRoot> weightList = new WeightList<>(ImmortalAPI.get().getSpiritualRoots(), ISpiritualRoot::getWeight);
            rootChosen.addAll(weightList.getRandomItems(random, 1, true));
        } else if(rootCount > 1){
            final WeightList<ISpiritualRoot> weightList = new WeightList<>(ImmortalAPI.get().getSpiritualRoots().stream().filter(ISpiritualRoot::isCommonRoot).collect(Collectors.toList()), ISpiritualRoot::getWeight);
            rootChosen.addAll(weightList.getRandomItems(random, rootCount, true));
        }
        return rootChosen;
    }

    public static void showPlayerSpiritualRoots(Player player){
        PlayerUtil.getOptManager(player).ifPresent(l -> {
            final MutableComponent component = Component.translatable("misc.immortal.spiritual_root");
            for(ISpiritualRoot spiritualRoot : ImmortalAPI.get().getSpiritualRoots()){
                if(l.hasSpiritualRoot(spiritualRoot)){
                    component.append(Component.literal(","));
                    component.append(Component.translatable("misc.immortal.root." + spiritualRoot.getName()));
                }
            }
            PlayerHelper.sendMsgTo(player, component);
        });
    }

    public static Optional<PlayerDataManager> getOptManager(Player player) {
        return Optional.ofNullable(getManager(player));
    }

    @Nullable
    public static PlayerDataManager getManager(Player player) {
        if(PlayerHelper.isValidPlayer(player)) {
            final Optional<PlayerCapability> optional = CapabilityHandler.getPlayerCapability(player).resolve();
            return optional.map(PlayerCapability::get).orElse(null);
        }
        return null;
    }

    public static <T> T getManagerResult(Player player, Function<PlayerDataManager, T> function, T defaultValue) {
        final PlayerDataManager manager = getManager(player);
        return manager != null ? function.apply(manager) : defaultValue;
    }

    /* Operations About Spells */

    public static void learnSpell(Player player, ISpell spell, int level) {
        getOptManager(player).ifPresent(l -> l.learnSpell(spell, level));
    }

    public static void forgetSpell(Player player, ISpell spell){
        getOptManager(player).ifPresent(l -> l.forgetSpell(spell));
    }

    public static void setSpellList(Player player, int pos, ISpell spell){
        getOptManager(player).ifPresent(l -> l.setSpellList(pos, spell));
    }

    public static void removeSpellList(Player player, int pos, ISpell spell){
        getOptManager(player).ifPresent(l -> l.removeSpellList(pos, spell));
    }

    /**
     * Not only used by S -> C sync.
     * Also use by {@link ImmortalCommand} for ignore checking.
     */
    public static void activateSpell(Player player, ISpell spell, long num){
        getOptManager(player).ifPresent(l -> {
            l.activateSpell(spell, num);
        });
    }

    public static void selectSpell(Player player, long num){
        getOptManager(player).ifPresent(l -> {
            l.selectSpell(num);
        });
    }

    public static void nextSpell(Player player, long num){
        getOptManager(player).ifPresent(l -> {
            l.nextSpell(num);
        });
    }

    public static int getSpellSelectedPosition(Player player) {
        return getManagerResult(player, PlayerDataManager::getSelectedSpellPosition, 0);
    }

    @Nullable
    public static ISpell getSpellAt(Player player, int pos) {
        return getManagerResult(player, m -> m.getSpellAt(pos), null);
    }

    @Nullable
    public static ISpell getSelectedSpell(Player player) {
        return getManagerResult(player, PlayerDataManager::getSelectedSpell, null);
    }

    public static boolean isSpellActivated(Player player, ISpell spell) {
        return getManagerResult(player, m -> m.isSpellActivated(spell), false);
    }

    public static double getSpellCDValue(Player player, ISpell spell) {
        return getManagerResult(player, m -> m.getSpellCDValue(spell), 0D);
    }

    public static boolean learnedSpell(Player player, ISpell spell) {
        return learnedSpell(player, spell, 1);
    }

    public static boolean learnedSpell(Player player, ISpell spell, int level) {
        return getManagerResult(player, m -> m.learnedSpell(spell, level), false);
    }

    public static int getSpellLearnLevel(Player player, ISpell spell) {
        return getManagerResult(player, m -> m.getSpellLearnLevel(spell), 0);
    }

    /* Operations about Integer Data */

    public static void setIntegerData(Player player, IRangeData<Integer> rangeData, int value){
        getOptManager(player).ifPresent(l -> l.setIntegerData(rangeData, value));
    }

    public static void addIntegerData(Player player, IRangeData<Integer> rangeData, int value){
        getOptManager(player).ifPresent(l -> l.addIntegerData(rangeData, value));
    }

    public static int getIntegerData(Player player, IRangeData<Integer> rangeData){
        return getManagerResult(player, m -> m.getIntegerData(rangeData), rangeData.defaultData());
    }

    public static int getSpiritualMana(Player player){
        return getIntegerData(player, PlayerDatas.SPIRITUAL_MANA);
    }

    public static int getFullSpiritualMana(Player player){
        return getManagerResult(player, PlayerDataManager::getFullManaValue, 0);
    }

    public static int getCultivation(Player player){
        return getManagerResult(player, PlayerDataManager::getLimitManaValue, 0);
    }

    /* Misc Operations */

    public static List<ISpiritualRoot> getSpiritualRoots(Player player){
        return getManagerResult(player, PlayerDataManager::getSpiritualRoots, List.of());
    }

    public static IRealm getPlayerRealm(Player player){
        return getManagerResult(player, m -> m.getRealm(), Realms.MORTALITY);
    }

    /**
     * 直接改变境界，会降低修为，同时灵气值归零。
     */
    public static void setRealm(Player player, IRealm realm){
        getOptManager(player).ifPresent(m -> {
            m.setRealm(realm);
            if(! player.level.isClientSide){
                m.setIntegerData(PlayerDatas.CULTIVATION, realm.getCultivation());
                m.setIntegerData(PlayerDatas.SPIRITUAL_MANA, 0);
            }
        });
    }

    public static void tick(Player player){
        getOptManager(player).ifPresent(PlayerDataManager::tick);
    }

}
