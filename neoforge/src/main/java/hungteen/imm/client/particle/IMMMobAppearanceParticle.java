package hungteen.imm.client.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;

/**
 * Copy from {@link net.minecraft.client.particle.MobAppearanceParticle}.
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/29 10:01
 **/
public abstract class IMMMobAppearanceParticle extends Particle {
    private final Model model;
    private final RenderType renderType;

    public IMMMobAppearanceParticle(ClientLevel level, Model model, RenderType renderType, double x, double y, double z) {
        super(level, x, y, z);
        this.model = model;
        this.renderType = renderType;
        this.gravity = 0.0F;
        this.lifetime = 30;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        float f = ((float)this.age + partialTicks) / (float)this.lifetime;
        float f1 = 0.05F + 0.5F * Mth.sin(f * (float) Math.PI);
        int i = FastColor.ARGB32.colorFromFloat(f1, 1.0F, 1.0F, 1.0F);
        PoseStack posestack = new PoseStack();
        posestack.mulPose(renderInfo.rotation());
        posestack.mulPose(Axis.XP.rotationDegrees(150.0F * f - 60.0F));
        posestack.scale(1.0F, -1.0F, -1.0F);
        posestack.translate(0.0F, -1.101F, 1.5F);
        MultiBufferSource.BufferSource consumer = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer vertexconsumer = consumer.getBuffer(this.renderType);
        this.model.renderToBuffer(posestack, vertexconsumer, 15728880, OverlayTexture.NO_OVERLAY, i);
        consumer.endBatch();
    }

}
