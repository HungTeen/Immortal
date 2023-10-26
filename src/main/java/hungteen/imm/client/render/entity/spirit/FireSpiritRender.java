package hungteen.imm.client.render.entity.spirit;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Either;
import hungteen.imm.client.model.IMMModelLayers;
import hungteen.imm.client.model.entity.spirit.FireSpiritModel;
import hungteen.imm.client.model.entity.spirit.WaterSpiritModel;
import hungteen.imm.client.render.entity.IMMMobRender;
import hungteen.imm.common.entity.creature.spirit.FireSpirit;
import hungteen.imm.common.entity.creature.spirit.WaterSpirit;
import hungteen.imm.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 25.10.2023 12:46
 **/
public class FireSpiritRender extends IMMMobRender<FireSpirit> {

    private static final ResourceLocation TEXTURE = Util.get().entityTexture("spirit/fire_spirit");

    public FireSpiritRender(EntityRendererProvider.Context context) {
        super(context, new FireSpiritModel<>(context.bakeLayer(IMMModelLayers.FIRE_SPIRIT), false), 0.3F);
//        this.addLayer(new OuterLayer<>(this, context.getModelSet()));
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

//    static class OuterLayer<T extends FireSpirit> extends RenderLayer<T, EntityModel<T>> {
//        private final FireSpiritModel<T> model;
//
//        public OuterLayer(RenderLayerParent<T, EntityModel<T>> p_174536_, EntityModelSet set) {
//            super(p_174536_);
//            this.model = new FireSpiritModel<>(set.bakeLayer(IMMModelLayers.FIRE_SPIRIT), true);
//        }
//
//        @Override
//        public void render(PoseStack stack, MultiBufferSource source, int p_117472_, T spirit, float p_117474_, float p_117475_, float p_117476_, float p_117477_, float p_117478_, float p_117479_) {
//            Minecraft minecraft = Minecraft.getInstance();
//            boolean glow = minecraft.shouldEntityAppearGlowing(spirit) && spirit.isInvisible();
//            if (!spirit.isInvisible() || glow) {
//                VertexConsumer consumer;
//                if (glow) {
//                    consumer = source.getBuffer(RenderType.outline(this.getTextureLocation(spirit)));
//                } else {
//                    consumer = source.getBuffer(RenderType.eyes(this.getTextureLocation(spirit)));
//                }
//
//                this.getParentModel().copyPropertiesTo(this.model);
//                this.model.prepareMobModel(spirit, p_117474_, p_117475_, p_117476_);
//                this.model.setupAnim(spirit, p_117474_, p_117475_, p_117477_, p_117478_, p_117479_);
//                this.model.renderToBuffer(stack, consumer, p_117472_, LivingEntityRenderer.getOverlayCoords(spirit, 0.0F), 1.0F, 1.0F, 1.0F, 0.5F);
//            }
//        }
//    }
}
