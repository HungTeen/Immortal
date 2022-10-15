package hungteen.immortal.client.event;

import com.mojang.blaze3d.platform.InputConstants;
import hungteen.htlib.ClientProxy;
import hungteen.immortal.ImmortalMod;
import hungteen.immortal.client.ClientDatas;
import hungteen.immortal.client.ImmortalKeyBinds;
import hungteen.immortal.network.NetworkHandler;
import hungteen.immortal.network.SpellPacket;
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
    public static void onKeyDown(InputEvent.KeyInputEvent ev) {
        if(ClientProxy.MC.isWindowActive() && ClientProxy.MC.player != null) {
            if(!ImmortalKeyBinds.isMouseInput(ImmortalKeyBinds.SPELL_CIRCLE) && ImmortalKeyBinds.SPELL_CIRCLE.consumeClick()){
                ClientDatas.ShowSpellCircle = ! ClientDatas.ShowSpellCircle;
            }
            if(ClientDatas.ShowSpellCircle){
                // hotkey to choose spell on circle.
                for(int i = 0; i < Constants.SPELL_NUM_EACH_PAGE; ++ i){
                    if(ev.getKey() == InputConstants.KEY_0 + i + 1){
                        NetworkHandler.sendToServer(new SpellPacket(null, SpellPacket.SpellOptions.SELECT, i));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onMouseDown(InputEvent.MouseInputEvent ev) {
        if(ClientProxy.MC.isWindowActive() && ClientProxy.MC.player != null) {
            if(ImmortalKeyBinds.isMouseInput(ImmortalKeyBinds.SPELL_CIRCLE) && ImmortalKeyBinds.SPELL_CIRCLE.consumeClick()){
                ClientDatas.ShowSpellCircle = ! ClientDatas.ShowSpellCircle;
            }

            if(ClientDatas.ShowSpellCircle && ev.getButton() == InputConstants.MOUSE_BUTTON_RIGHT){
                NetworkHandler.sendToServer(new SpellPacket(null, SpellPacket.SpellOptions.ACTIVATE_AT, 0));
                ClientDatas.ShowSpellCircle = false;
            }
        }
//        if(ClientProxy.MC.isWindowActive() && ClientProxy.MC.player != null) {
//            if(ClientProxy.MC.player.getVehicle() instanceof CobCannonEntity) {
//                CobCannonEntity cob = (CobCannonEntity) ClientProxy.MC.player.getVehicle();
//                if(ClientProxy.MC.player.getMainHandItem().isEmpty() && cob.getCornNum() > 0 && ClientProxy.MC.options.keyUse.consumeClick()) {
//                    PVZPacketHandler.CHANNEL.sendToServer(new EntityInteractPacket(cob.getId(), 0, 0));
//                }
//            }
//        }
    }

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollEvent ev) {
		double delta = ev.getScrollDelta();
		if(delta != 0.0 && ClientProxy.MC.player != null && ClientDatas.ShowSpellCircle) {
            NetworkHandler.sendToServer(new SpellPacket(null, SpellPacket.SpellOptions.NEXT, delta < 0 ? 1 : -1));
            ev.setCanceled(true);
		}
    }

}
