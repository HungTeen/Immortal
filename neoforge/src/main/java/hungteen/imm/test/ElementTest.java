package hungteen.imm.test;

import hungteen.imm.api.IMMAPI;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/5 18:55
 **/
@GameTestHolder(IMMAPI.MOD_ID)
public class ElementTest {

    /**
     * 尽量保证达到如下表格的效果（衰减系数和时间有倍数关系）：<br>
     * 衰减系数为 1 时：元素量为 20 时，需要衰减 5s；元素量为 100 时，需要衰减 10s。<br>
     * 衰减系数为 0.8 时：元素量为 20 时，需要衰减 7s；元素量为 100 时，需要衰减 12s。<br>
     * 衰减系数为 0.6 时：元素量为 20 时，需要衰减 9s；元素量为 100 时，需要衰减 15s。<br>
     * 衰减系数为 0.4 时：元素量为 20 时，需要衰减 13s；元素量为 100 时，需要衰减 23s。<br>
     * 衰减系数为 0.2 时：元素量为 20 时，需要衰减 25s；元素量为 100 时，需要衰减 46s。<br>
     * @param helper
     */
    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtil.TEMPLATE_EMPTY)
    public static void testElementDecay(GameTestHelper helper) {
//        int[] amounts = {1, 10, 20, 30, 50, 75, 100};
//        float[] factors = {1F, 0.8F, 0.6F, 0.4F, 0.2F};
//        for(var f : factors) {
//            for (var amount : amounts) {
//                int leftTick = ElementManager.getLeftTick(amount, f);
//                System.out.println("Amount: " + amount + ", Factor: " + f + ", LeftTick: " + leftTick);
//            }
//        }
        helper.succeed();
    }

}
