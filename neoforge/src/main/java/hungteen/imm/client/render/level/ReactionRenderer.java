package hungteen.imm.client.render.level;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import hungteen.htlib.util.helper.ColorHelper;
import hungteen.imm.api.registry.IElementReaction;
import hungteen.imm.client.ClientData;
import hungteen.imm.client.util.ClientUtil;
import hungteen.imm.client.util.RenderUtil;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-08-23 21:41
 **/
public class ReactionRenderer {

    private static final List<ReactionParticle> PARTICLES = new ArrayList<>();

    public static void tick() {
        PARTICLES.forEach(ReactionParticle::tick);
        PARTICLES.removeIf(ReactionParticle::expired);
    }

    public static void addReaction(Entity entity, IElementReaction reaction){
        if(ClientData.isDebugMode){
            PARTICLES.add(new ReactionParticle(entity, reaction));
        }
    }

    public static void renderParticles(PoseStack stack, Camera camera) {
        if(ClientData.isDebugMode) {
            PARTICLES.forEach(p -> renderParticle(stack, camera, p));
        }
    }

    private static void renderParticle(PoseStack stack, Camera camera, ReactionParticle particle) {
        final double distanceSqr = camera.getPosition().distanceTo(particle.position);
        if (distanceSqr > 1000) {
            return;
        }

        final float scaleToGui = 0.025f;

        final float tickDelta = 0; // TODO partial Ticks

        final double x = Mth.lerp(tickDelta, particle.lastPosition.x, particle.position.x);
        final double y = Mth.lerp(tickDelta, particle.lastPosition.y, particle.position.y);
        final double z = Mth.lerp(tickDelta, particle.lastPosition.z, particle.position.z);

        stack.pushPose();
        stack.translate(x - camera.getPosition().x, y - camera.getPosition().y, z - camera.getPosition().z);
        stack.mulPose(Axis.YP.rotationDegrees(-camera.getYRot()));
        stack.mulPose(Axis.XP.rotationDegrees(camera.getXRot()));
        stack.scale(-scaleToGui, -scaleToGui, scaleToGui);

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE,
                GL11.GL_ZERO);

        RenderUtil.renderCenterScaledText(stack, particle.reaction.getComponent(), 0, 0, 2, ColorHelper.WHITE.rgb(), ColorHelper.BLACK.rgb());

        RenderSystem.disableBlend();

        stack.popPose();
    }

    public static class ReactionParticle {

        private final IElementReaction reaction;
        private Vec3 position;
        private Vec3 lastPosition;
        private Vec3 speed;
        private int age = 0;

        public ReactionParticle(Entity entity, IElementReaction reaction) {
            this.reaction = reaction;
            final Vec3 entityLocation = entity.getEyePosition();
            final Vec3 cameraLocation = ClientUtil.camera().getPosition();
            final Vec3 offset = cameraLocation.subtract(entityLocation).normalize().scale(entity.getBbWidth());
            this.position = entityLocation.add(offset);
            this.lastPosition = this.position;
            this.speed = new Vec3(entity.level().getRandom().nextGaussian() * 0.06F, entity.level().getRandom().nextGaussian() * 0.04F, entity.level().getRandom().nextGaussian() * 0.06F);
        }

        public void tick() {
            ++ age;
            this.lastPosition = position;
            this.position = this.position.add(this.speed);
            this.speed = this.speed.scale(0.96F);
        }

        public boolean expired(){
            return ! ClientData.isDebugMode || age > 20;
        }

    }
}
