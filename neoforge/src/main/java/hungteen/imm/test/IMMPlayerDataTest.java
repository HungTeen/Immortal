package hungteen.imm.test;

import hungteen.imm.api.IMMAPI;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * @program Immortal
 * @author PangTeen
 * @create 2024/11/21 21:07
 **/
@GameTestHolder(IMMAPI.MOD_ID)
public class IMMPlayerDataTest {

    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtil.TEMPLATE_EMPTY)
    public static void test(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        PlayerUtil.resetSpiritualRoots(player);
        helper.assertFalse(PlayerUtil.getSpiritualRoots(player).isEmpty(), "SpiritualRoots is empty");
        helper.succeed();
    }

}
