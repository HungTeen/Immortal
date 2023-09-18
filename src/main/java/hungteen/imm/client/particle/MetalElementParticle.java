package hungteen.imm.client.particle;

import hungteen.htlib.client.particle.HTMultiTextureParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

/**
 * Copy from {@link SpellParticle}
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-04 08:25
 **/
public class MetalElementParticle extends HTMultiTextureParticle {

    public MetalElementParticle(ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
        this.gravity = 0F;
        this.setParticleSpeed(0, 0, 0);
        this.quadSize *= 0.75F;
        this.lifetime = (int)(10D / (Math.random() * 0.8D + 0.2D));
        this.hasPhysics = false;
        final float rotation = clientLevel.getRandom().nextFloat() * 360;
        this.oRoll = this.roll = rotation;
        this.setSpriteFromAge(this.sprites);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public record Factory(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new MetalElementParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, sprite());
        }
    }

}
