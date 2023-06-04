package hungteen.imm.util;

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
}
