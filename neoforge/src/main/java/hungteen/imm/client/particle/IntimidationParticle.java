package hungteen.imm.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/29 9:59
 **/
public class IntimidationParticle extends IMMMobAppearanceParticle {

    private static final ResourceLocation ENDERMAN_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/enderman/enderman.png");

    public IntimidationParticle(ClientLevel level, double x, double y, double z) {
        super(
                level,
                new EndermanModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.ENDERMAN)),
                RenderType.entityTranslucent(ENDERMAN_LOCATION),
                x, y, z
        );
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(
                SimpleParticleType type,
                ClientLevel level,
                double x,
                double y,
                double z,
                double xSpeed,
                double ySpeed,
                double zSpeed
        ) {
            return new IntimidationParticle(level, x, y, z);
        }
    }
}
