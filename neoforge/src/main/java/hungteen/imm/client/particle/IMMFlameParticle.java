package hungteen.imm.client.particle;

import hungteen.htlib.client.particle.HTTextureParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-07 22:00
 **/
public class IMMFlameParticle extends HTTextureParticle {

    /**
     * Copy From {@link net.minecraft.client.particle.RisingParticle}.
     */
    public IMMFlameParticle(ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed);
        this.friction = 0.96F;
        this.xd = this.xd * (double)0.01F + xSpeed;
        this.yd = this.yd * (double)0.01F + ySpeed;
        this.zd = this.zd * (double)0.01F + zSpeed;
        this.x += (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
        this.y += (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
        this.z += (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
        this.lifetime = (int)(8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
    }

    public void move(double xSpeed, double ySpeed, double zSpeed) {
        this.setBoundingBox(this.getBoundingBox().move(xSpeed, ySpeed, zSpeed));
        this.setLocationFromBoundingbox();
    }

    public float getQuadSize(float partials) {
        float f = ((float)this.age + partials) / (float)this.lifetime;
        return this.quadSize * (1.0F - f * f * 0.5F);
    }

    public int getLightColor(float partials) {
        float f = ((float)this.age + partials) / (float)this.lifetime;
        f = Mth.clamp(f, 0.0F, 1.0F);
        int i = super.getLightColor(partials);
        int j = i & 255;
        int k = i >> 16 & 255;
        j += (int)(f * 15.0F * 16.0F);
        if (j > 240) {
            j = 240;
        }

        return j | k << 16;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    public record Factory(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            IMMFlameParticle particle = new IMMFlameParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.pickSprite(this.sprite);
            return particle;
        }
    }

}
