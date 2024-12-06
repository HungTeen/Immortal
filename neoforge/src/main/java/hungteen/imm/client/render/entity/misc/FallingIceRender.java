package hungteen.imm.client.render.entity.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.imm.client.util.RenderUtil;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.entity.misc.FallingIceEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/6 11:56
 **/
public class FallingIceRender extends EntityRenderer<FallingIceEntity> {

    private final BlockRenderDispatcher dispatcher;

    public FallingIceRender(EntityRendererProvider.Context context) {
        super(context);
        this.dispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(FallingIceEntity iceEntity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(iceEntity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.pushPose();
        float scale = Mth.lerp(partialTick, iceEntity.lastScale, iceEntity.getIceScale());
        iceEntity.lastScale = scale;
        poseStack.scale(scale, scale, scale);
        for(int i = -1; i <= 1; ++ i){
            for(int j = -1; j <= 1; ++ j){
                RenderUtil.renderBlock(dispatcher, iceEntity, poseStack, bufferSource, IMMBlocks.FALLING_ICE.get().defaultBlockState(), i, j);
            }
        }
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(FallingIceEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
