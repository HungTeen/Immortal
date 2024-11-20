package hungteen.imm.client.render.entity.spirit;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;
import com.mojang.math.Axis;
import hungteen.imm.client.model.IMMModelLayers;
import hungteen.imm.client.model.entity.spirit.FireSpiritModel;
import hungteen.imm.client.render.entity.IMMMobRender;
import hungteen.imm.common.entity.creature.spirit.FireSpirit;
import hungteen.imm.util.Util;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.data.ModelData;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 25.10.2023 12:46
 **/
public class FireSpiritRender extends IMMMobRender<FireSpirit> {

    private static final ResourceLocation TEXTURE = Util.get().entityTexture("spirit/fire_spirit");

    public FireSpiritRender(EntityRendererProvider.Context context) {
        super(context, new FireSpiritModel<>(context.bakeLayer(IMMModelLayers.FIRE_SPIRIT), false), 0.3F);
        this.addLayer(new OuterLayer<>(this, context));
    }

    @Override
    public Either<Float, Vec3> getScaleByEntity(FireSpirit entity, float partialTick) {
        final float f1 = 1F;
        final float f2 = Mth.lerp(partialTick, entity.oSquish, entity.squish) / (f1 * 0.5F + 1.0F);
        final float f3 = 1.0F / (f2 + 1.0F);
        return Either.right(new Vec3(f3 * f1, 1.0F / f3 * f1, f3 * f1));
    }

    @Override
    public Vec3 getTranslateVec(FireSpirit entity, float partialTick) {
        return new Vec3(0, Mth.sin((entity.tickCount + partialTick) / 15.0F) * 0.1F - 0.2F, 0);
    }

    @Override
    public ResourceLocation getTextureLocation(FireSpirit spirit) {
        return TEXTURE;
    }

    static class OuterLayer<T extends FireSpirit> extends RenderLayer<T, EntityModel<T>> {

        private final BlockRenderDispatcher blockRenderer;

        public OuterLayer(RenderLayerParent<T, EntityModel<T>> parent, EntityRendererProvider.Context context) {
            super(parent);
            this.blockRenderer = context.getBlockRenderDispatcher();
        }

        @Override
        public void render(PoseStack stack, MultiBufferSource source, int p_117472_, T spirit, float p_117474_, float p_117475_, float p_117476_, float p_117477_, float p_117478_, float p_117479_) {
            if (!spirit.isInvisible()) {
                stack.pushPose();
                final float size = 0.55F;
                stack.scale(size, size, size);
                stack.translate(-0.5F, 1.75F, 0.5F);
                stack.mulPose(Axis.XP.rotationDegrees(180.0F));
                this.blockRenderer.renderSingleBlock(Blocks.FIRE.defaultBlockState(), stack, source, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.cutout());
                stack.popPose();
            }
        }
    }
}
