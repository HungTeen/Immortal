package hungteen.immortal.utils;

import hungteen.immortal.ModConfigs;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.util.ArrayList;
import java.util.List;

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

    private static void randomSpawnRoots(Player player, int rootCount){
        if(rootCount == 1){

        }
    }

}
