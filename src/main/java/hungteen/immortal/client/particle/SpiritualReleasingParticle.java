package hungteen.immortal.client.particle;

import hungteen.htlib.client.particle.HTTextureParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-04 08:25
 **/
public class SpiritualReleasingParticle extends HTTextureParticle {

    private final SpriteSet sprites;

    public SpiritualReleasingParticle(ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed);
        this.sprites = sprites;
        this.friction = 0.96F;
        this.gravity = 0F;
        this.speedUpWhenYMotionIsBlocked = true;
        this.setParticleSpeed(0, 0, 0);

        this.quadSize *= 0.75F;
        this.lifetime = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
        this.hasPhysics = false;
        this.setSpriteFromAge(this.sprites);
    }

    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);
        this.setAlpha(Mth.lerp(0.05F, this.alpha, 1.0F));
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public record Factory(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            SpiritualReleasingParticle particle = new SpiritualReleasingParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.sprite);
            particle.setColor((float) xSpeed, (float) ySpeed, (float) zSpeed);
            return particle;
        }
    }

}
