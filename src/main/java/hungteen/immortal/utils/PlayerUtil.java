package hungteen.immortal.utils;

import hungteen.htlib.util.WeightList;
import hungteen.immortal.ModConfigs;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.interfaces.ISpiritualRoot;
import hungteen.immortal.common.capability.CapabilityHandler;
import hungteen.immortal.common.capability.player.IPlayerCapability;
import hungteen.immortal.common.capability.player.PlayerCapability;
import hungteen.immortal.common.capability.player.PlayerDataManager;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        final double[] rootChances = {ModConfigs.getNoRootChance(), ModConfigs.getOneRootChance(), ModConfigs.getTwoRootChance(), ModConfigs.getThreeRootChance(), ModConfigs.getFourRootChance()};
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

}
