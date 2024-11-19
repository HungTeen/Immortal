package hungteen.imm.client.render.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import hungteen.imm.client.model.entity.CubeModel;
import hungteen.imm.client.render.entity.creature.monster.SharpStakeRender;
import hungteen.imm.common.entity.creature.monster.SharpStake;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-09-03 16:44
 **/
public class WoodStakeLayer extends RenderLayer<SharpStake, CubeModel<SharpStake>> {

    private final BlockRenderDispatcher blockRenderer;
    private final ItemRenderer itemRenderer;

    public WoodStakeLayer(EntityRendererProvider.Context context, SharpStakeRender parent) {
        super(parent);
        this.blockRenderer = context.getBlockRenderDispatcher();
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource bufferSource, int packedLightIn, SharpStake stake, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        final BlockState state = stake.getStakeState();
        if (!state.isAir()) {
            // Render log.
            stack.pushPose();
            final float size = 0.95F;
            stack.scale(size, size, size);
            stack.translate(-0.5D, 0.6D, -0.5D);
            this.blockRenderer.renderSingleBlock(state, stack, bufferSource, packedLightIn, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, null);
            stack.popPose();

            // Render axe.
            if (!stake.getMainHandItem().isEmpty()) {
                stack.pushPose();
                stack.translate(0, 0.1D, 0);
                stack.mulPose(Axis.YP.rotationDegrees(180F));
                this.itemRenderer.renderStatic(stake, stake.getMainHandItem(), ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, false, stack, bufferSource, stake.level(), packedLightIn, OverlayTexture.NO_OVERLAY, stake.getId());
                stack.popPose();
            }
        }
    }
}
