package hungteen.imm.client.event;

import hungteen.imm.api.IMMAPI;
import hungteen.imm.client.gui.overlay.CommonOverlay;
import hungteen.imm.client.render.level.ElementRenderer;
import hungteen.imm.client.render.level.ReactionRenderer;
import hungteen.imm.client.util.ClientUtil;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-07-02 16:43
 **/
@EventBusSubscriber(modid = IMMAPI.MOD_ID, value = Dist.CLIENT)
public class RenderEvents {

    @SubscribeEvent
    public static void preRenderOverlay(RenderGuiLayerEvent.Pre ev) {
        if(ev.getName().equals(VanillaGuiLayers.EXPERIENCE_BAR)
                || ev.getName().equals(VanillaGuiLayers.EXPERIENCE_LEVEL)
                || ev.getName().equals(VanillaGuiLayers.JUMP_METER)){
            ClientUtil.playerOpt().ifPresent(player -> {
                if (CommonOverlay.canRenderManaBar(player)) {
                    ev.setCanceled(true);
                }
            });
        }
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
