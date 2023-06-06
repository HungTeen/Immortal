package hungteen.imm.util;

import net.minecraft.util.Mth;

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
        if (maxNum != 0) {
            int percent = Mth.floor(num * maxLen / maxNum);
            if (percent <= 0 && num != 0) {
                return 1;
            } else {
                return percent >= maxLen && num != maxNum ? maxLen - 1 : Mth.clamp(percent, 0, maxLen);
            }
        } else {
            return 0;
        }
    }

}
