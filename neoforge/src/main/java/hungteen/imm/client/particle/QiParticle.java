package hungteen.imm.client.particle;

import hungteen.htlib.client.particle.HTTextureParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

/**
 * Copy from {@link SpellParticle}
 * @program Immortal
 * @author HungTeen
 * @create 2022-11-04 08:25
 **/
public class QiParticle extends HTTextureParticle {

    private static final RandomSource RANDOM = RandomSource.create();
    private final SpriteSet sprites;

    public QiParticle(ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(clientLevel, x, y, z, 0.5D - RANDOM.nextDouble(), ySpeed, 0.5D - RANDOM.nextDouble());
        this.sprites = sprites;
        this.friction = 0.96F;
        this.gravity = 0F;
        this.speedUpWhenYMotionIsBlocked = true;
        this.yd *= 0.2F;
        if (xSpeed == 0.0D && zSpeed == 0.0D) {
            this.xd *= 0.1F;
            this.zd *= 0.1F;
        }

        this.quadSize *= 0.75F;
        this.lifetime = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
        this.hasPhysics = false;
        this.setSpriteFromAge(this.sprites);
    }

    @Override
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
            QiParticle particle = new QiParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.sprite);
//            particle.setColor((float) xSpeed, (float) ySpeed, (float) zSpeed);
            return particle;
        }
    }

}
