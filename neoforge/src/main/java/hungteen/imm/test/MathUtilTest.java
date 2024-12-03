package hungteen.imm.test;

import hungteen.imm.api.IMMAPI;
import hungteen.imm.util.MathUtil;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * @program Immortal
 * @author PangTeen
 * @create 2024/11/21 21:07
 **/
@GameTestHolder(IMMAPI.MOD_ID)
public class MathUtilTest {

    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtil.TEMPLATE_EMPTY)
    public static void testGaussian(GameTestHelper helper) {
        System.out.println("MathUtilTest.test");
        for(int i = 0; i < 3; ++ i){
            System.out.println("n = 3, std = 1, i = " + i);
            printArray(MathUtil.generateGaussianArray(3, i, 1));
        }
        for(int i = 0; i < 3; ++ i){
            System.out.println("n = 3, std = 10, i = " + i);
            printArray(MathUtil.generateGaussianArray(3, i, 0.1));
        }
//        for(int i = 0; i < 10; ++ i) {
//            System.out.println("n = 3, std = 1, i = " + i);
//            printArray(MathUtil.generateGaussianArray(10, i, 1));
//        }
        helper.succeed();
    }

    private static void printArray(double[] array) {
        for (double v : array) {
            System.out.print(v + " ");
        }
        System.out.println();
    }

}
