package hungteen.immortal.client.event;

import hungteen.immortal.ImmortalMod;
import hungteen.immortal.client.ClientProxy;
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
//        if(ClientProxy.MC.isWindowActive() && ClientProxy.MC.player != null) {
//            // Switch display of spell circle.
//            if(!ImmortalKeyBinds.isMouseInput(ImmortalKeyBinds.SPELL_CIRCLE) && ImmortalKeyBinds.SPELL_CIRCLE.consumeClick()){
//                switchSpellCircle();
//            }
//        }
    }

    @SubscribeEvent
    public static void onMouseDown(InputEvent.MouseButton.Pre event) {
        if(ClientProxy.MC.isWindowActive() && ClientProxy.MC.player != null) {
//            // Switch display of spell circle.
//            if(ImmortalKeyBinds.isMouseInput(ImmortalKeyBinds.SPELL_CIRCLE) && ImmortalKeyBinds.SPELL_CIRCLE.consumeClick()){
//                switchSpellCircle();
//                event.setCanceled(true);
//            }
//            // Right click to select spell.
//            if(ClientDatas.ShowSpellCircle && event.getButton() == InputConstants.MOUSE_BUTTON_RIGHT){
//                NetworkHandler.sendToServer(new SpellPacket(null, SpellPacket.SpellOptions.SELECT, ClientDatas.lastSelectedPosition));
//                ClientDatas.ShowSpellCircle = false;
//                event.setCanceled(true);
//            }
        }
    }

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
//		double delta = event.getScrollDelta();
//		if(delta != 0.0 && ClientProxy.MC.player != null && ClientDatas.ShowSpellCircle) {
//            NetworkHandler.sendToServer(new SpellPacket(null, SpellPacket.SpellOptions.NEXT, delta < 0 ? 1 : -1));
//            event.setCanceled(true);
//		}
    }

}
