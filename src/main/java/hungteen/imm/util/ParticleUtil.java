package hungteen.imm.util;

import hungteen.htlib.util.helper.MathHelper;
import hungteen.htlib.util.helper.RandomHelper;
import hungteen.htlib.util.helper.registry.ParticleHelper;
import hungteen.imm.client.particle.IMMParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-04 10:17
 **/
public class ParticleUtil {

    public static void spawnLineSpiritualParticle(Level world, float red, float green, float blue, Vec3 origin, Vec3 target, float particleRatio, int particleCountEach, double offsetScale) {
        double distance = origin.distanceTo(target);
        int particleNum = Mth.ceil(distance * (double) particleRatio);

        for (int i = 0; i < particleNum; ++i) {
            for (int j = 0; j < particleCountEach; ++j) {
                Vec3 pos = origin.add(target.subtract(origin).normalize().scale(Math.max(1.0D, distance - 2.0D) / (double) particleNum * (double) (i + 1) / (double) particleRatio)).add(RandomHelper.vec3Range(world.getRandom(), offsetScale));
                ParticleHelper.spawnParticles(world, IMMParticles.SPIRITUAL_MANA.get(), pos, red, green, blue);
            }
        }
    }

    public static void spawnParticlesOnBlockFaces(Level level, BlockPos blockPos, ParticleOptions particleOptions, IntProvider cntProvider) {
        for(Direction direction : Direction.values()) {
            spawnParticlesOnBlockFace(level, blockPos, particleOptions, cntProvider, direction, RandomHelper.vec3Range(level.random, 0.05D), 0.55D);
        }
    }

    public static void spawnParticlesOnBlockFace(Level level, BlockPos pos, ParticleOptions options, IntProvider provider, Direction dir, Vec3 speed, double offset) {
        final int count = provider.sample(level.random);
        for(int j = 0; j < count; ++j) {
            spawnParticleOnFace(level, pos, dir, options, speed, offset);
        }
    }

    public static void spawnParticleOnFace(Level level, BlockPos blockPos, Direction dir, ParticleOptions particleOptions, Vec3 speed, double offset) {
        final Vec3 pos = MathHelper.toVec3(blockPos);
        final int i = dir.getStepX();
        final int j = dir.getStepY();
        final int k = dir.getStepZ();
        ParticleHelper.spawnParticles(level,
                particleOptions,
                pos.add(
                        i == 0 ? RandomHelper.doubleRange(level.random, 0.5) : i * offset,
                        j == 0 ? RandomHelper.doubleRange(level.random, 0.5) : j * offset,
                        k == 0 ? RandomHelper.doubleRange(level.random, 0.5) : k * offset
                ),
                i == 0 ? speed.x() : 0.0D,
                j == 0 ? speed.y() : 0.0D,
                k == 0 ? speed.z() : 0.0D
        );
    }
}
