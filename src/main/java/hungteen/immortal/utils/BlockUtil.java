package hungteen.immortal.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
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

    public static ResourceLocation getWoolCushionLocation(DyeColor color){
        return Util.prefix(color.getName() + "_wool_cushion");
    }
}
