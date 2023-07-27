package hungteen.imm.client.event;

import hungteen.imm.ImmortalMod;
import hungteen.imm.client.event.handler.RenderEventHandler;
import hungteen.imm.client.gui.overlay.CommonOverlay;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-07-02 16:43
 **/
@Mod.EventBusSubscriber(modid = ImmortalMod.MOD_ID, value = Dist.CLIENT)
public class RenderEvents {

    @SubscribeEvent
    public static void onPostRenderOverlay(RenderGuiOverlayEvent.Pre ev) {
        if(ev.getOverlay() == VanillaGuiOverlay.EXPERIENCE_BAR.type() || ev.getOverlay() == VanillaGuiOverlay.JUMP_BAR.type()){
            if(CommonOverlay.canRenderManaBar()){
                ev.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onPostRenderOverlay(RenderGuiOverlayEvent.Post ev) {
    }

    @SubscribeEvent
    public static void onPostRenderOverlay(RenderNameTagEvent ev) {
        RenderEventHandler.renderEntityElements(ev.getEntity(), ev.getEntityRenderer(), ev.getPoseStack(), ev.getMultiBufferSource(), ev.getPackedLight());
    }

    @SubscribeEvent
    public static void gatherComponents(RenderTooltipEvent.GatherComponents event){
//        if(ElixirManager.isElixirIngredient(event.getItemStack())){
//            List<Either<FormattedText, TooltipComponent>> components = event.getTooltipElements();
//            components.add(components.size(), Either.right(new ElementToolTip(ElixirManager.getElixirIngredient(event.getItemStack()))));
//        }
    }

    @SubscribeEvent
    public static void gatherComponents(RenderLevelStageEvent event){
    }

}
