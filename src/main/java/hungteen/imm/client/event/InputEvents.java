package hungteen.imm.client.event;

import com.mojang.blaze3d.platform.InputConstants;
import hungteen.imm.ImmortalMod;
import hungteen.imm.client.ClientDatas;
import hungteen.imm.client.ClientHandler;
import hungteen.imm.client.ClientProxy;
import hungteen.imm.client.ImmortalKeyBinds;
import hungteen.imm.common.spell.SpellManager;
import hungteen.imm.utils.Constants;
import net.minecraft.util.Mth;
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
        if(ClientProxy.MC.isWindowActive() && ClientProxy.MC.player != null) {
            if(! ClientHandler.useDefaultCircle()) {
                // Switch display of spell circle.
                if(!ImmortalKeyBinds.isMouseInput(ImmortalKeyBinds.SPELL_CIRCLE) && ImmortalKeyBinds.SPELL_CIRCLE.consumeClick()){
                    ClientHandler.switchSpellCircle();
                }
            }
        }
    }

    @SubscribeEvent
    public static void onMouseDown(InputEvent.MouseButton.Pre event) {
        if(ClientProxy.MC.isWindowActive() && ClientProxy.MC.player != null) {
            if(! ClientHandler.useDefaultCircle()){
                // Switch display of spell circle.
                if(ImmortalKeyBinds.isMouseInput(ImmortalKeyBinds.SPELL_CIRCLE) && ImmortalKeyBinds.SPELL_CIRCLE.consumeClick()){
                    ClientHandler.switchSpellCircle();
                    event.setCanceled(true);
                }

                // Right click to activate spell.
                if(ClientDatas.ShowSpellCircle && event.getButton() == InputConstants.MOUSE_BUTTON_RIGHT){
                    SpellManager.activateAt(ClientDatas.lastSelectedPosition);
                    ClientHandler.switchSpellCircle();
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
		double delta = event.getScrollDelta();
        if(! ClientHandler.useDefaultCircle()){
            // Scroll to switch select position.
            if(delta != 0.0 && ClientProxy.MC.player != null && ClientDatas.ShowSpellCircle) {
                ClientDatas.lastSelectedPosition = Mth.clamp(ClientDatas.lastSelectedPosition + delta < 0 ? 1 : -1, 0, Constants.SPELL_CIRCLE_SIZE);
                event.setCanceled(true);
            }
        }
    }

}
