package hungteen.imm.common.entity.misc;

import hungteen.htlib.common.entity.HTEntity;
import hungteen.imm.client.particle.IMMParticles;
import hungteen.imm.util.Constants;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-07 21:40
 **/
public class SpiritualFlame extends HTEntity {

    private static final EntityDataAccessor<Integer> FLAME_LEVEL = SynchedEntityData.defineId(SpiritualFlame.class, EntityDataSerializers.INT);

    public SpiritualFlame(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(FLAME_LEVEL, 1);
    }

    @Override
    public void tick() {
        super.tick();
        //TODO 灵火一段时间后消失
        if(this.level.isClientSide){
            spawnFlames(this.level, this.position(), this.getFlameLevel(), this.random);
        }
    }

    /**
     * Spawn Spiritual Flame Particles.
     */
    public static void spawnFlames(Level world, Vec3 vec3, int level, RandomSource rand){
        final int lvl = (level - 1) % 3;
        final int extra = level * 5;
        final ParticleOptions options = getFlameParticleType(level);
        if(lvl == 0){
            for(int i = 0; i < extra + 10; ++ i){
                final Vec3 pos = vec3.add(rand.nextFloat() - 0.5, rand.nextFloat(), rand.nextFloat() - 0.5);
                world.addParticle(options, pos.x, pos.y, pos.z, (rand.nextFloat() - 0.5) / 4, 0.15F, (rand.nextFloat() - 0.5) / 4);
            }
        } else if(lvl == 1){
            for(int i = 0; i < extra + 30; ++ i){
                final Vec3 pos = vec3.add((rand.nextFloat() - 0.5) * 1.5, rand.nextFloat() * 1.2, (rand.nextFloat() - 0.5) * 1.5);
                world.addParticle(options, pos.x, pos.y, pos.z, (rand.nextFloat() - 0.5) / 3, 0.2F, (rand.nextFloat() - 0.5) / 3);
            }
        } else if(lvl == 2){
            for(int i = 0; i < extra + 50; ++ i){
                final Vec3 pos = vec3.add((rand.nextFloat() - 0.5) * 2, rand.nextFloat() * 1.5, (rand.nextFloat() - 0.5) * 2);
                world.addParticle(options, pos.x, pos.y, pos.z, (rand.nextFloat() - 0.5) / 2, 0.2F, (rand.nextFloat() - 0.5) / 2);
            }
        }
    }

    /**
     * get flame particles by spiritual flame level.
     */
    public static ParticleOptions getFlameParticleType(int level) {
        return level <= 3 ? ParticleTypes.FLAME :
                level <= 6 ? ParticleTypes.SOUL_FIRE_FLAME :
                        IMMParticles.IMMORTAL_FLAME.get();
    }

    public Vec3 getFlameCenter(){
        return this.position().add(0, 1, 0);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        double d0 = this.getBoundingBox().getSize() * 4.0D;
        if (Double.isNaN(d0)) {
            d0 = 4.0D;
        }

        d0 *= 64.0D;
        return distance < d0 * d0;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("SpiritualFlameLevel", getFlameLevel());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if(tag.contains("SpiritualFlameLevel")) {
            setFlameLevel(tag.getInt("SpiritualFlameLevel"));
        }
    }

    public void setFlameLevel(int level) {
        entityData.set(FLAME_LEVEL, Math.min(level, Constants.MAX_FLAME_LEVEL));
    }

    public int getFlameLevel() {
        return entityData.get(FLAME_LEVEL);
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    protected boolean canRide(Entity entity) {
        return false;
    }

}
