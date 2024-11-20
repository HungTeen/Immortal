package hungteen.imm.common;

import hungteen.imm.common.impl.registry.PlayerRangeIntegers;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.world.entity.player.Player;

/**
 * 业障管理。
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/15 10:44
 */
public class KarmaManager {

    public static final float MAX_KARMA_VALUE = 100;

    /**
     * Range from [0, 90], which close to [0, 100).
     */
    public static float calculateKarma(Player player){
        return (float) (10 * Math.log10(1 + getKarma(player)));
    }

    public static int getKarma(Player player){
        return PlayerUtil.getIntegerData(player, PlayerRangeIntegers.KARMA);
    }

}
