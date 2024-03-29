package hungteen.imm.client.event;

import com.mojang.blaze3d.platform.InputConstants;
import hungteen.imm.ImmortalMod;
import hungteen.imm.client.ClientData;
import hungteen.imm.client.ClientProxy;
import hungteen.imm.client.ClientUtil;
import hungteen.imm.client.event.handler.SpellHandler;
import hungteen.imm.util.TipUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-09 21:55
 **/
@Mod.EventBusSubscriber(modid = ImmortalMod.MOD_ID, value = Dist.CLIENT)
public class InputEvents {

    @SubscribeEvent
    public static void onKeyDown(InputEvent.Key event) {
        if(ClientProxy.MC.isWindowActive() && ClientUtil.screen() == null && ClientUtil.player() != null && event.getAction() == InputConstants.PRESS) {
            SpellHandler.checkSpellCircle(event.getKey());
            if(event.getKey() == InputConstants.KEY_H && Screen.hasAltDown()){
                ClientData.isDebugMode = ! ClientData.isDebugMode;
                ClientUtil.player().sendSystemMessage(TipUtil.info("debug." + (ClientData.isDebugMode ? "open" : "close")).withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD));
            }
        }
    }

    @SubscribeEvent
    public static void onMouseDown(InputEvent.MouseButton.Pre event) {
        if(ClientProxy.MC.isWindowActive() && ClientUtil.screen() == null && ClientUtil.player() != null && event.getAction() == InputConstants.PRESS) {
            // 检查触发法术。
            if(SpellHandler.checkSpellCircle(event.getButton())) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        if(ClientProxy.MC.isWindowActive() && ClientUtil.screen() == null && ClientUtil.player() != null) {
            double delta = event.getScrollDelta();
            if (SpellHandler.selectOnSpellCircle(delta)) {
                event.setCanceled(true);
            }
        }
    }

}
