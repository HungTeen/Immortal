package hungteen.imm.client.event;

import hungteen.imm.ImmortalMod;
import hungteen.imm.client.ClientDatas;
import hungteen.imm.client.event.handler.RenderEventHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-10 10:10
 **/
@Mod.EventBusSubscriber(modid = ImmortalMod.MOD_ID, value = Dist.CLIENT)
public class OverlayEvents {

    @SubscribeEvent
    public static void onPostRenderOverlay(RenderGuiOverlayEvent.Pre ev) {
        if(ev.getOverlay() == VanillaGuiOverlay.EXPERIENCE_BAR.type() || ev.getOverlay() == VanillaGuiOverlay.JUMP_BAR.type()){
            if(RenderEventHandler.canRenderManaBar()){
                ev.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onPostRenderOverlay(RenderGuiOverlayEvent.Post ev) {
    }

    /**
     * {@link hungteen.imm.client.ClientRegister#registerGuiOverlays(RegisterGuiOverlaysEvent)}
     */
    public static void registerOverlay(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("spiritual_mana_bar", (gui, poseStack, partialTick, screenWidth, screenHeight) -> {
            if (RenderEventHandler.canRenderManaBar()) {
                gui.setupOverlayRenderState(true, false);
                RenderEventHandler.renderSpiritualMana(poseStack, screenHeight, screenWidth);
            }
        });
        event.registerAboveAll("spell_circle", (gui, poseStack, partialTick, screenWidth, screenHeight) -> {
            if(RenderEventHandler.canRenderOverlay() && ClientDatas.ShowSpellCircle){
                RenderEventHandler.renderSpellCircle(poseStack, screenHeight, screenWidth);
            }
        });
        event.registerAboveAll("elements", (gui, poseStack, partialTick, screenWidth, screenHeight) -> {
            if(RenderEventHandler.canRenderOverlay()){
                RenderEventHandler.renderElements(poseStack, screenHeight, screenWidth);
            }
        });
        event.registerAboveAll("smithing_progress_bar", (gui, poseStack, partialTick, screenWidth, screenHeight) -> {
            if(RenderEventHandler.canRenderOverlay()){
                RenderEventHandler.renderSmithingBar(poseStack, screenHeight, screenWidth);
            }
        });
    }

}
