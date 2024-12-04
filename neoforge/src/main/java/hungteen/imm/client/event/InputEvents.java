package hungteen.imm.client.event;

import com.mojang.blaze3d.platform.InputConstants;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.client.IMMClientProxy;
import hungteen.imm.client.data.ClientData;
import hungteen.imm.client.data.SpellClientData;
import hungteen.imm.client.util.ClientUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-09 21:55
 **/
@EventBusSubscriber(modid = IMMAPI.MOD_ID, value = Dist.CLIENT)
public class InputEvents {

    @SubscribeEvent
    public static void onKeyDown(InputEvent.Key event) {
        if(IMMClientProxy.MC.isWindowActive() && ClientUtil.screen() == null && ClientUtil.player() != null && event.getAction() == InputConstants.PRESS) {
            SpellClientData.checkSpellCircle(event.getKey());
            if(event.getKey() == InputConstants.KEY_H && Screen.hasAltDown()){
                ClientData.isDebugMode = ! ClientData.isDebugMode;
                ClientUtil.player().sendSystemMessage(TipUtil.info("debug." + (ClientData.isDebugMode ? "open" : "close")).withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD));
            }
        }
    }

    @SubscribeEvent
    public static void onMouseDown(InputEvent.MouseButton.Pre event) {
        if(IMMClientProxy.MC.isWindowActive() && ClientUtil.screen() == null && ClientUtil.player() != null && event.getAction() == InputConstants.PRESS) {
            // 检查触发法术。
            if(SpellClientData.checkSpellCircle(event.getButton())) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        if(IMMClientProxy.MC.isWindowActive() && ClientUtil.screen() == null && ClientUtil.player() != null) {
            double delta = event.getScrollDeltaY();
            if (SpellClientData.selectOnSpellCircle(delta)) {
                event.setCanceled(true);
            }
        }
    }

}
