package hungteen.imm.client.render.entity.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.imm.client.util.RenderUtil;
import hungteen.imm.common.entity.misc.TwistingVines;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/6 11:56
 **/
public class TwistingVinesRender extends EntityRenderer<TwistingVines> {

    private final BlockRenderDispatcher dispatcher;

    public TwistingVinesRender(EntityRendererProvider.Context context) {
        super(context);
        this.dispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(TwistingVines vines, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(vines, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.pushPose();
        float ticks = Mth.lerp(partialTick, vines.lastSpawnTicks, vines.getSpawnTicks());
        vines.lastSpawnTicks = ticks;
        float dy = Mth.sin(Math.min(0, (ticks - TwistingVines.SPAWN_CD)) / TwistingVines.SPAWN_CD);
        poseStack.translate(0, dy, 0);
        VineBlock.PROPERTY_BY_DIRECTION.forEach((direction, property) -> {
            RenderUtil.renderBlock(dispatcher, vines, poseStack, bufferSource, Blocks.VINE.defaultBlockState().setValue(property, true), 0, 0);
        });
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(TwistingVines entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
