package hungteen.imm.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.WaterDropParticle;
import net.minecraft.core.particles.SimpleParticleType;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-09-17 10:49
 **/
public class WaterElementParticle extends WaterDropParticle {

    protected WaterElementParticle(ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(clientLevel, x, y, z);
        if (ySpeed == 0.0D && (xSpeed != 0.0D || zSpeed != 0.0D)) {
            this.xd = xSpeed;
            this.yd = 0.1D;
            this.zd = zSpeed;
        }
    }

    public record Factory(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            WaterElementParticle particle = new WaterElementParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.pickSprite(sprite());
            return particle;
        }
    }
}
