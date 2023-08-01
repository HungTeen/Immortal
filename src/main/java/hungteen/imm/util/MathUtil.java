package hungteen.imm.util;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-06-04 22:10
 **/
public class MathUtil {

    public static float unwrapDegree(float degree){
        return degree * ((float)Math.PI / 180F);
    }

    public static double unwrapDegree(double degree){
        return degree * (Math.PI / 180D);
    }

    public static int getBarLen(float num, float maxNum, int maxLen) {
        return getBarLen(maxNum == 0 ? 1F : num / maxNum, maxLen);
    }

    public static int getBarLen(float percent, int maxLen) {
        if (percent >= 0) {
            int len = Mth.floor(percent * maxLen);
            if (len <= 0 && percent != 0) {
                return 1;
            } else if(len >= maxLen && percent < 1) {
                return maxLen - 1;
            }
            return Mth.clamp(len, 0, maxLen);
        } else {
            return 0;
        }
    }

    public static boolean inAABB(Vec3 pos, AABB aabb){
        return aabb.contains(pos);
    }

}
