package hungteen.immortal.utils;

import hungteen.htlib.util.MathUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-27 14:35
 **/
public class BlockUtil {

    public static boolean stillValid(Player player, BlockEntity entity) {
        if (player.level.getBlockEntity(entity.getBlockPos()) != entity) {
            return false;
        } else {
            return player.distanceToSqr(MathUtil.toVector(entity.getBlockPos())) <= 64;
        }
    }
}
