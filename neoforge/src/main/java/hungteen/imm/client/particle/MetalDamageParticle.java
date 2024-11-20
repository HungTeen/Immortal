package hungteen.imm.client.particle;

import hungteen.htlib.client.particle.HTMultiTextureParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/10/29 13:19
 **/
public class MetalDamageParticle extends HTMultiTextureParticle {

    public MetalDamageParticle(ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
        this.gravity = -0.1F;
        this.friction = 0.9F;
        this.xd = xSpeed + (Math.random() * 2.0D - 1.0D) * (double)0.05F;
        this.yd = ySpeed + (Math.random() * 2.0D - 1.0D) * (double)0.05F;
        this.zd = zSpeed + (Math.random() * 2.0D - 1.0D) * (double)0.05F;
//        float f = this.random.nextFloat() * 0.3F + 0.7F;
//        this.rCol = f;
//        this.gCol = f;
//        this.bCol = f;
        this.quadSize = 0.1F * (this.random.nextFloat() * this.random.nextFloat() * 6.0F + 1.0F);
        this.lifetime = (int)(8.0D / ((double)this.random.nextFloat() * 0.8D + 0.2D)) + 2;
        this.setSpriteFromAge(sprites);
    }

    public record Factory(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new MetalDamageParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, sprite());
        }

    }

}
