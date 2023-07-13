package hungteen.imm.util;

import hungteen.htlib.util.helper.RandomHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/8 15:14
 */
public class RandomUtil {

    public static Vec3 vec3Range(RandomSource rand, double vertical, double horizontal) {
        return new Vec3(RandomHelper.doubleRange(rand, horizontal), RandomHelper.doubleRange(rand, vertical), RandomHelper.doubleRange(rand, horizontal));
    }

    public static float getTriangle(RandomSource source){
        return source.nextFloat() - source.nextFloat();
    }
}
