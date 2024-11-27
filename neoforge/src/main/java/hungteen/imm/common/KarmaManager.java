package hungteen.imm.common;

import hungteen.imm.common.capability.player.IMMPlayerData;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.world.entity.player.Player;

/**
 * 业障管理。
 * @author PangTeen
 * @program Immortal
 * @create 2023/7/15 10:44
 */
public class KarmaManager {

    public static final float MAX_KARMA_VALUE = 100;

    /**
     * Range from [0, 90], which close to [0, 100).
     */
    public static float calculateKarma(Player player){
        return (float) (10 * Math.log10(1 + getKarma(player)));
    }

    public static float getKarma(Player player){
        return PlayerUtil.getFloatData(player, IMMPlayerData.FloatData.KARMA);
    }

}
