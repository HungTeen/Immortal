package hungteen.immortal.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-27 14:35
 **/
public class BlockUtil {

    public static AABB getBlockAABB(BlockPos pos){
        return new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
    }
}
