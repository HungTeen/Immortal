package hungteen.imm.util;

import hungteen.htlib.util.helper.RandomHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/7/8 15:14
 */
public class RandomUtil {

    public static Vec3 vec3Range(RandomSource rand, double vertical, double horizontal) {
        return new Vec3(RandomHelper.doubleRange(rand, horizontal), RandomHelper.doubleRange(rand, vertical), RandomHelper.doubleRange(rand, horizontal));
    }

    public static float getTriangle(RandomSource source){
        return source.nextFloat() - source.nextFloat();
    }

    public static <T> T choose(RandomSource random, List<T> list){
        return list.get(random.nextInt(list.size()));
    }

}
