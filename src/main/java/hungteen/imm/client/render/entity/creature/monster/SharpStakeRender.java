package hungteen.imm.client.render.entity.creature.monster;

import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.imm.client.model.entity.EmptyModel;
import hungteen.imm.client.render.entity.IMMMobRender;
import hungteen.imm.common.entity.creature.monster.SharpStake;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/6 15:14
 */
public class SharpStakeRender extends IMMMobRender<SharpStake> {

    private final BlockRenderDispatcher blockRender;

    public SharpStakeRender(EntityRendererProvider.Context context) {
        super(context, new EmptyModel<>(), 0.5F);
        this.blockRender = context.getBlockRenderDispatcher();
    }

    public void render(SharpStake stake, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferSource, int packedLightIn) {
        super.render(stake, entityYaw, partialTicks, stack, bufferSource, packedLightIn);
        final BlockState state = stake.getStakeState();
        if(! state.isAir()){
            stack.pushPose();
            stack.translate(-0.5D, 0.0D, -0.5D);
            this.blockRender.renderSingleBlock(state, stack, bufferSource, packedLightIn, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, null);
            stack.popPose();
        }
    }

    @Override
    public ResourceLocation getTextureLocation(SharpStake stake) {
        return null;
    }

}
