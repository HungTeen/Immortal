package hungteen.immortal.utils;

import hungteen.htlib.interfaces.IRangeData;
import hungteen.htlib.util.WeightList;
import hungteen.immortal.ImmortalConfigs;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.events.PlayerSpellEvent;
import hungteen.immortal.api.interfaces.ISpell;
import hungteen.immortal.api.interfaces.ISpiritualRoot;
import hungteen.immortal.capability.CapabilityHandler;
import hungteen.immortal.capability.player.PlayerCapability;
import hungteen.immortal.capability.player.PlayerDataManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
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

    /**
     * 玩家灵根的生成规则：
     * 1. 首先依据概率选择是几个灵根（0 - 5）。
     * 2. 如果是1个灵根，那么依据权重在普通灵根和异灵根中选择一个。
     * 3. 否则依据权重在普通五行灵根中选择若干个。
     */
    public static void spawnSpiritualRoots(Player player){
        final double[] rootChances = {ImmortalConfigs.getNoRootChance(), ImmortalConfigs.getOneRootChance(), ImmortalConfigs.getTwoRootChance(), ImmortalConfigs.getThreeRootChance(), ImmortalConfigs.getFourRootChance()};
        double chance = player.getRandom().nextDouble();
        for(int i = 0; i < rootChances.length; ++ i){
            if(chance < rootChances[i]){
                randomSpawnRoots(player, i);
                return;
            }
            chance -= rootChances[i];
        }
        randomSpawnRoots(player, 5);
    }

    /**
     * {@link #spawnSpiritualRoots(Player)}
     */
    private static void randomSpawnRoots(Player player, int rootCount){
        final List<ISpiritualRoot> rootChosen = new ArrayList<>();
        System.out.println(rootCount);
        if(rootCount == 1){
            final WeightList<ISpiritualRoot> weightList = new WeightList<>(ImmortalAPI.get().getSpiritualRoots(), ISpiritualRoot::getWeight);
            rootChosen.addAll(weightList.getRandomItems(player.getRandom(), 1, true));
        } else if(rootCount > 1){
            final WeightList<ISpiritualRoot> weightList = new WeightList<>(ImmortalAPI.get().getSpiritualRoots().stream().filter(ISpiritualRoot::isCommonRoot).collect(Collectors.toList()), ISpiritualRoot::getWeight);
            rootChosen.addAll(weightList.getRandomItems(player.getRandom(), rootCount, true));
        }
        getOptManager(player).ifPresent(l -> {
            l.clearSpiritualRoot();
            rootChosen.forEach(r -> {
                System.out.println(r.getName());
                l.addSpiritualRoot(r);
            });
        });

    }

    public static void showPlayerSpiritualRoots(Player player){
        PlayerUtil.getOptManager(player).ifPresent(l -> {
            final BaseComponent component = new TranslatableComponent("misc.immortal.spiritual_root");
            for(ISpiritualRoot spiritualRoot : ImmortalAPI.get().getSpiritualRoots()){
                if(l.hasSpiritualRoot(spiritualRoot)){
                    component.append(new TextComponent(","));
                    component.append(new TranslatableComponent("misc.immortal.root." + spiritualRoot.getName()));
                }
            }
            hungteen.htlib.util.PlayerUtil.sendMsgTo(player, component);
        });
    }

    public static Optional<PlayerDataManager> getOptManager(Player player) {
        return Optional.ofNullable(getManager(player));
    }

    @Nullable
    public static PlayerDataManager getManager(Player player) {
        if(hungteen.htlib.util.PlayerUtil.isValidPlayer(player)) {
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
     * Also use by {@link hungteen.immortal.command.ImmortalCommand} for ignore checking.
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

    public static void tick(Player player){
        getOptManager(player).ifPresent(l -> l.tick());
    }

}
