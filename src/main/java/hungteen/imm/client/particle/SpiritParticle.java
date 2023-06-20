package hungteen.imm.client.particle;

import hungteen.htlib.client.particle.HTTextureParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/6/19 16:10
 */
public class SpiritParticle extends HTTextureParticle {

    private static final RandomSource RANDOM = RandomSource.create();
    private final SpriteSet sprites;

    public SpiritParticle(ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed);
        this.sprites = sprites;
        this.friction = 0.96F;
        this.gravity = 0F;
        this.speedUpWhenYMotionIsBlocked = true;

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
            SpiritParticle particle = new SpiritParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.sprite);
            return particle;
        }
    }
}
