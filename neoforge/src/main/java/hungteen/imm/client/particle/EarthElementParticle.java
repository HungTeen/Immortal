package hungteen.imm.client.particle;

import hungteen.htlib.client.particle.HTMultiTextureParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-04 08:25
 **/
public class EarthElementParticle extends HTMultiTextureParticle {

    public EarthElementParticle(ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
        this.gravity = 0.004F;
        this.xd = this.xd * (double)0.001F;
        this.yd = 0;
        this.zd = this.zd * (double)0.001F;
        this.quadSize *= 0.75F;
        this.lifetime = (int)(10D / (Math.random() * 0.8D + 0.2D));
        this.hasPhysics = false;
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
            return new EarthElementParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, sprite());
        }
    }

}
