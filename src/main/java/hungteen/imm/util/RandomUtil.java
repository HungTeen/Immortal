package hungteen.imm.util;

import net.minecraft.util.RandomSource;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/8 15:14
 */
public class RandomUtil {

    public static float getTriangle(RandomSource source){
        return source.nextFloat() - source.nextFloat();
    }
}
