package hungteen.imm.util;

import hungteen.htlib.util.helper.MathHelper;
import hungteen.htlib.util.helper.RandomHelper;
import hungteen.htlib.util.helper.impl.ParticleHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * @author HungTeen
 * @program Immortal
 * @create 2022-11-04 10:17
 **/
public class ParticleUtil {

    public static BlockParticleOption block(BlockState state) {
        return new BlockParticleOption(ParticleTypes.BLOCK, state);
    }

    public static BlockParticleOption block(BlockState state, BlockPos pos) {
        return block(state).setPos(pos);
    }

    public static void spawnEntityParticle(Entity entity, ParticleOptions particle, int count, double speed) {
        ParticleHelper.spawnParticles(entity.level(), particle, entity.getX(), entity.getY(0.5), entity.getZ(), count, entity.getBbWidth(), entity.getBbHeight() / 2, speed);
    }

    /**
     * 参考吃东西的粒子效果，找到生成位置。。
     */
    public static Vec3 getUsingItemPos(LivingEntity living) {
        double d0 = (double) (-living.getRandom().nextFloat()) * 0.6 - 0.3;
        Vec3 vec31 = new Vec3(((double) living.getRandom().nextFloat() - 0.5) * 0.3, d0, 0.6);
        vec31 = vec31.xRot(-living.getXRot() * (float) (Math.PI / 180.0));
        vec31 = vec31.yRot(-living.getYRot() * (float) (Math.PI / 180.0));
        vec31 = vec31.add(living.getX(), living.getEyeY(), living.getZ());
        return vec31;
    }

//    public static void spawnLineSpiritualParticle(Level world, float red, float green, float blue, Vec3 origin, Vec3 target, float particleRatio, int particleCountEach, double offsetScale) {
//        double distance = origin.distanceTo(target);
//        int particleNum = Mth.ceil(distance * (double) particleRatio);
//
//        for (int i = 0; i < particleNum; ++i) {
//            for (int j = 0; j < particleCountEach; ++j) {
//                Vec3 pos = origin.add(target.subtract(origin).normalize().scale(Math.max(1.0D, distance - 2.0D) / (double) particleNum * (double) (i + 1) / (double) particleRatio)).add(RandomHelper.vec3Range(world.getRandom(), offsetScale));
//                ParticleHelper.spawnParticles(world, IMMParticles.SPIRITUAL_MANA.get(), pos, red, green, blue);
//            }
//        }
//    }

    public static void spawnClientParticles(Level level, ParticleOptions particle, Vec3 vec, int amount, double dst, double speed) {
        ParticleHelper.spawnClientParticles(level, particle, vec.x(), vec.y(), vec.z(), amount, dst, dst, dst, speed);
    }

    public static void spawnClientParticles(Level level, ParticleOptions particle, double x, double y, double z, int amount, double dst, double speed) {
        ParticleHelper.spawnClientParticles(level, particle, x, y, z, amount, dst, dst, dst, speed);
    }

    public static void spawnParticlesOnBlockFaces(Level level, BlockPos blockPos, ParticleOptions particleOptions, IntProvider cntProvider) {
        spawnParticlesOnBlockFaces(level, blockPos, particleOptions, cntProvider, Direction.values());
    }

    public static void spawnParticlesOnBlockFaces(Level level, BlockPos blockPos, ParticleOptions particleOptions, IntProvider cntProvider, Direction... dirs) {
        for (Direction direction : dirs) {
            spawnParticlesOnBlockFace(level, blockPos, particleOptions, cntProvider, direction, RandomHelper.vec3Range(level.random, 0.05D), 0.55D);
        }
    }

    public static void spawnParticlesOnBlockFace(Level level, BlockPos pos, ParticleOptions options, IntProvider provider, Direction dir, Vec3 speed, double offset) {
        final int count = provider.sample(level.random);
        spawnParticleOnFace(level, pos, dir, options, count, speed, offset);
    }

    /**
     * {@link net.minecraft.util.ParticleUtils#spawnParticleOnFace(Level, BlockPos, Direction, ParticleOptions, Vec3, double)}
     */
    public static void spawnParticleOnFace(Level level, BlockPos blockPos, Direction dir, ParticleOptions particle, int amount, Vec3 speed, double offset) {
        final Vec3 pos = MathHelper.toVec3(blockPos);
        final int i = dir.getStepX();
        final int j = dir.getStepY();
        final int k = dir.getStepZ();
        ParticleHelper.spawnClientParticles(
                level,
                particle,
                pos.x,
                pos.y,
                pos.z,
                amount,
                offset,
                offset,
                i == 0 ? speed.x() : 0.0D,
                j == 0 ? speed.y() : 0.0D,
                k == 0 ? speed.z() : 0.0D
        );
    }
}
