package hungteen.imm.client.particle;

import hungteen.htlib.client.particle.HTMultiTextureParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-04 08:25
 **/
public class FireElementParticle extends HTMultiTextureParticle {

    public FireElementParticle(ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
        this.gravity = 0F;
        this.xd = this.xd * (double)0.001F;
        this.yd = this.yd * (double)0.01F;
        this.zd = this.zd * (double)0.001F;
        this.x += (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
        this.y += (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
        this.z += (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
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
            return new FireElementParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, sprite());
        }
    }

}
