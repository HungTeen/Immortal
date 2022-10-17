package hungteen.immortal.client.event;

import hungteen.htlib.ClientProxy;
import hungteen.immortal.ImmortalMod;
import hungteen.immortal.client.ClientDatas;
import hungteen.immortal.client.event.handler.OverlayHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.system.CallbackI;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-10 10:10
 **/
@Mod.EventBusSubscriber(modid = ImmortalMod.MOD_ID, value = Dist.CLIENT)
public class OverlayEvents {

    @SubscribeEvent
    public static void onPostRenderOverlay(RenderGameOverlayEvent.Post ev) {
        if(ev.getType() == RenderGameOverlayEvent.ElementType.ALL){
//            if(! ClientProxy.MC.options.hideGui && ClientProxy.MC.screen == null && InputEvents.ShowOverlay && ClientProxy.MC.player != null && ! ClientProxy.MC.player.isSpectator()){
//                /* render resources on left upper corner */
//                if(! ClientProxy.MC.options.renderDebug){
//                    PVZOverlayHandler.renderResources(ev.getMatrixStack(), ev.getWindow().getGuiScaledWidth(), ev.getWindow().getGuiScaledHeight());
//                }
//
//            }
            if(! ClientProxy.MC.options.hideGui && ClientProxy.MC.screen == null && ClientProxy.MC.player != null && ! ClientProxy.MC.player.isSpectator()){
                if(ClientDatas.ShowSpellCircle){
                    OverlayHandler.renderSpellCircle(ev.getMatrixStack(), ev.getWindow().getGuiScaledWidth(), ev.getWindow().getGuiScaledHeight());
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPostRenderOverlay(RenderGameOverlayEvent.PreLayer ev) {
        if(ev.getOverlay() == ForgeIngameGui.EXPERIENCE_BAR_ELEMENT || ev.getOverlay() == ForgeIngameGui.JUMP_BAR_ELEMENT){
            if(OverlayHandler.canRenderManaBar()){
                ev.setCanceled(true);
            }
        }
    }

}
