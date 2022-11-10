package hungteen.immortal.client.event;

import com.mojang.blaze3d.platform.InputConstants;
import hungteen.htlib.ClientProxy;
import hungteen.immortal.ImmortalMod;
import hungteen.immortal.client.ClientDatas;
import hungteen.immortal.client.ImmortalKeyBinds;
import hungteen.immortal.common.network.NetworkHandler;
import hungteen.immortal.common.network.SpellPacket;
import hungteen.immortal.utils.Constants;
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
            if(!ImmortalKeyBinds.isMouseInput(ImmortalKeyBinds.SPELL_CIRCLE) && ImmortalKeyBinds.SPELL_CIRCLE.consumeClick()){
                ClientDatas.ShowSpellCircle = ! ClientDatas.ShowSpellCircle;
            }
            if(ClientDatas.ShowSpellCircle){
                // hotkey to choose spell on circle.
                for(int i = 0; i < Constants.SPELL_NUM_EACH_PAGE; ++ i){
                    if(event.getKey() == InputConstants.KEY_0 + i + 1){
                        NetworkHandler.sendToServer(new SpellPacket(null, SpellPacket.SpellOptions.SELECT, i));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onMouseDown(InputEvent.MouseButton.Pre event) {
        if(ClientProxy.MC.isWindowActive() && ClientProxy.MC.player != null) {
            if(ImmortalKeyBinds.isMouseInput(ImmortalKeyBinds.SPELL_CIRCLE) && ImmortalKeyBinds.SPELL_CIRCLE.consumeClick()){
                ClientDatas.ShowSpellCircle = ! ClientDatas.ShowSpellCircle;
                event.setCanceled(true);
            }

            if(ClientDatas.ShowSpellCircle && event.getButton() == InputConstants.MOUSE_BUTTON_RIGHT){
                NetworkHandler.sendToServer(new SpellPacket(null, SpellPacket.SpellOptions.ACTIVATE_AT, 0));
                ClientDatas.ShowSpellCircle = false;
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
		double delta = event.getScrollDelta();
		if(delta != 0.0 && ClientProxy.MC.player != null && ClientDatas.ShowSpellCircle) {
            NetworkHandler.sendToServer(new SpellPacket(null, SpellPacket.SpellOptions.NEXT, delta < 0 ? 1 : -1));
            event.setCanceled(true);
		}
    }

}
