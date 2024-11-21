package hungteen.imm.util;

import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-06-04 22:10
 **/
public class MathUtil {

    public static boolean inSlotArea(double x, double y, int posX, int posY, int lenX, int lenY){
        return x >= posX && x <= posX + lenX && y >= posY && y <= posY + lenY;
    }
    public static Direction[] getHorizontalDirections(){
        return Direction.stream().filter(dir -> dir.getAxis().isHorizontal()).toArray(Direction[]::new);
    }

    public static double horizontalLength(Vec3 vec){
        return Math.sqrt(vec.x() * vec.x() + vec.z() * vec.z());
    }

    public static double log2(double x){
        return Math.log(x) / Math.log(2);
    }

    public static float toRadian(float degree){
        return degree * ((float)Math.PI / 180F);
    }

    public static double toRadian(double degree){
        return degree * (Math.PI / 180D);
    }

    public static Vec3 rotateHorizontally(Vec3 vec, double degree){
        final double sin = Math.sin(toRadian(degree));
        final double cos = Math.cos(toRadian(degree));
        return new Vec3(vec.z * cos - vec.x * sin, vec.y, vec.x * cos + vec.z * sin);
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

    public static AABB getUpperAABB(Vec3 pos, float width, float height){
        return new AABB(pos.x() - width, pos.y(), pos.z() - width, pos.x() + width, pos.y() + height, pos.z() + width);
    }

    public static AABB getAABB(Vec3 pos, double horizontalRange, double verticalRange) {
        return new AABB(pos.x() - horizontalRange, pos.y() - verticalRange, pos.z() - horizontalRange, pos.x() + horizontalRange, pos.y() + verticalRange, pos.z() + horizontalRange);
    }

    public static boolean inAABB(Vec3 pos, AABB aabb){
        return aabb.contains(pos);
    }

}
