package hungteen.imm.client.render.entity.spirit;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hungteen.imm.client.model.IMMModelLayers;
import hungteen.imm.client.model.entity.spirit.WaterSpiritModel;
import hungteen.imm.client.render.entity.IMMMobRender;
import hungteen.imm.common.entity.creature.spirit.WaterSpirit;
import hungteen.imm.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/10/24 12:24
 **/
public class WaterSpiritRender extends IMMMobRender<WaterSpirit> {

    private static final ResourceLocation TEXTURE = Util.get().entityTexture("spirit/water_spirit");

    public WaterSpiritRender(EntityRendererProvider.Context context) {
        super(context, new WaterSpiritModel<>(context.bakeLayer(IMMModelLayers.WATER_SPIRIT), false), 0.3F);
        this.addLayer(new OuterLayer<>(this, context.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(WaterSpirit waterSpirit) {
        return TEXTURE;
    }

    static class OuterLayer<T extends WaterSpirit> extends RenderLayer<T, EntityModel<T>> {
        private final WaterSpiritModel<T> model;

        public OuterLayer(RenderLayerParent<T, EntityModel<T>> p_174536_, EntityModelSet set) {
            super(p_174536_);
            this.model = new WaterSpiritModel<>(set.bakeLayer(IMMModelLayers.WATER_SPIRIT), true);
        }

        @Override
        public void render(PoseStack stack, MultiBufferSource source, int p_117472_, T spirit, float p_117474_, float p_117475_, float p_117476_, float p_117477_, float p_117478_, float p_117479_) {
            Minecraft minecraft = Minecraft.getInstance();
            boolean glow = minecraft.shouldEntityAppearGlowing(spirit) && spirit.isInvisible();
            if (!spirit.isInvisible() || glow) {
                VertexConsumer consumer;
                if (glow) {
                    consumer = source.getBuffer(RenderType.outline(this.getTextureLocation(spirit)));
                } else {
                    consumer = source.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(spirit)));
                }

                this.getParentModel().copyPropertiesTo(this.model);
                this.model.prepareMobModel(spirit, p_117474_, p_117475_, p_117476_);
                this.model.setupAnim(spirit, p_117474_, p_117475_, p_117477_, p_117478_, p_117479_);
                this.model.renderToBuffer(stack, consumer, p_117472_, LivingEntityRenderer.getOverlayCoords(spirit, 0.0F), 1.0F, 1.0F, 1.0F, 0.5F);
            }
        }
    }

}
