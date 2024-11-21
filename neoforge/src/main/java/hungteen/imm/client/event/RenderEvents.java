package hungteen.imm.client.event;

import hungteen.imm.client.render.level.ElementRenderer;
import hungteen.imm.client.render.level.ReactionRenderer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.*;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-07-02 16:43
 **/
//@EventBusSubscriber(modid = IMMAPI.MOD_ID, value = Dist.CLIENT)
public class RenderEvents {

    @SubscribeEvent
    public static void preRenderOverlay(RenderGuiLayerEvent.Pre ev) {
//        if(ev.getLayer() == VanillaGuiOverlay.EXPERIENCE_BAR.type() || ev.getOverlay() == VanillaGuiOverlay.JUMP_BAR.type()){
//            if(CommonOverlay.canRenderManaBar()){
//                ev.setCanceled(true);
//            }
//        }
    }

    @SubscribeEvent
    public static void postRenderOverlay(RenderGuiLayerEvent.Post ev) {
    }

    @SubscribeEvent
    public static void renderPlayer(RenderPlayerEvent.Post ev) {

    }

    @SubscribeEvent
    public static void renderNameTag(RenderNameTagEvent ev) {
        ElementRenderer.renderEntityElements(ev.getEntity(), ev.getEntityRenderer(), ev.getPoseStack(), ev.getMultiBufferSource(), ev.getPackedLight());
    }

    @SubscribeEvent
    public static void gatherComponents(RenderTooltipEvent.GatherComponents event){
//        if(ElixirManager.isElixirIngredient(event.getItemStack())){
//            List<Either<FormattedText, TooltipComponent>> components = event.getTooltipElements();
//            components.add(components.size(), Either.right(new ElementToolTip(ElixirManager.getElixirIngredient(event.getItemStack()))));
//        }
    }

    @SubscribeEvent
    public static void renderLevel(RenderLevelStageEvent event){
        if(event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES){
            ReactionRenderer.renderParticles(event.getPoseStack(), event.getCamera());
        }
    }

}
