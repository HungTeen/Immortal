package hungteen.imm.common.entity.misc;

import hungteen.htlib.common.entity.HTEntity;
import hungteen.htlib.util.helper.MathHelper;
import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.htlib.util.helper.registry.ParticleHelper;
import hungteen.imm.api.enums.Elements;
import hungteen.imm.api.enums.RealmStages;
import hungteen.imm.api.interfaces.IHasMana;
import hungteen.imm.api.interfaces.IHasRealm;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.client.particle.IMMParticles;
import hungteen.imm.common.ElementManager;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.impl.registry.RealmTypes;
import hungteen.imm.common.misc.damage.IMMDamageSources;
import hungteen.imm.util.Constants;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.LevelUtil;
import hungteen.imm.util.MathUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.fluids.IFluidBlock;

import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-07 21:40
 **/
public class SpiritualFlame extends HTEntity implements IEntityAdditionalSpawnData, IHasRealm, IHasMana {

    public static final float MAX_AMOUNT = Constants.MAX_SPIRITUAL_FLAME_AMOUNT;
    private static final int RECOVER_CD = 50;
    private static final float RECOVER_RATE = 0.02F;
    private static final EntityDataAccessor<Float> FLAME_AMOUNT = SynchedEntityData.defineId(SpiritualFlame.class, EntityDataSerializers.FLOAT);
    private int flameLevel;

    public SpiritualFlame(EntityType<?> type, Level world) {
        super(type, world);
    }

    public SpiritualFlame(Level world, int flameLevel) {
        super(IMMEntities.SPIRITUAL_FLAME.get(), world);
        this.flameLevel = flameLevel;
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeInt(this.flameLevel);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.flameLevel = additionalData.readInt();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(FLAME_AMOUNT, MAX_AMOUNT);
    }

    @Override
    public void tick() {
        super.tick();
        if(EntityHelper.isServer(this)){
            if(this.tickCount % 10 == 0){
                final float width = getCoverWidth() * 2F;
                final float height = getCoverHeight() * 1.5F;
                final AABB aabb = MathUtil.getUpperAABB(position(), width, height);
                // 烧伤附近的生物。
                EntityHelper.getPredicateEntities(this, aabb, LivingEntity.class, target -> {
                    return true;
                }).forEach(target -> {
                    this.flameTarget(target, (float) Math.max(0, 1 - distanceToSqr(target) / (width * width)));
                });
                // 与周围的方块交互。
                if(this.random.nextFloat() < 0.3F){
                    final int w = Mth.ceil(getCoverWidth());
                    final int h = Mth.ceil(getCoverHeight());
                    for(int i = -w; i <= w; ++ i){
                        for(int j = 0; j <= h; ++ j){
                            for(int k = 0; k <= w; ++ k){
                                final BlockPos pos = blockPosition().offset(i, j, k);
                                this.flameTarget(level().getBlockState(pos), pos);
                            }
                        }
                    }
                }
            }
            if(this.getFlameAmount() <= 0){
                this.discard();
            } else {
                if(this.tickCount % RECOVER_CD == 10){
                    this.addMana(this.getFlameAmount() * RECOVER_RATE);
                    if(this.random.nextFloat() < 0.1F){
                        this.playSound(SoundEvents.LAVA_POP);
                    }
                }
            }
        } else {
            // Spawn spiritual flame particles.
            final ParticleOptions particleType = getFlameParticleType(getFlameLevel());
            if(random.nextFloat() * 2 < getFlameAmount() / MAX_AMOUNT){
                final float width = getCoverWidth();
                final float height = getCoverHeight();
                ParticleHelper.spawnParticles(level(), particleType, getX(), getY() + height / 2, getZ(), getFlameCnt(), width, height, (random.nextFloat() - 0.5) / 3, 0.2F, (random.nextFloat() - 0.5) / 3);
            } else {
                ParticleHelper.spawnParticles(level(), particleType, position(), this.flameLevel, (random.nextFloat() - 0.5) / 3, 0.2F, (random.nextFloat() - 0.5) / 3);
            }
        }
    }

    public void flameTarget(LivingEntity target, float percent){
        target.setSecondsOnFire(Math.max(2, Mth.ceil (10 * percent)));
        if(percent > 0.75F){
            ElementManager.addElementAmount(target, Elements.FIRE, true, this.flamePercent() * 15 * percent);
        } else if(percent > 0.25F){
            ElementManager.addElementAmount(target, Elements.FIRE, false, this.flamePercent() * 10 * percent);
        }
        if(percent > 0.5F){
            target.hurt(IMMDamageSources.spiritualFlame(this), percent * 8);
        }
        LevelUtil.playSound(level(), SoundEvents.FIRE_EXTINGUISH, SoundSource.AMBIENT, target.position());
        this.addMana(-50 * percent);
    }

    public void flameTarget(BlockState state, BlockPos pos){
        if(! state.isAir()){
            if(state.getFluidState().is(FluidTags.WATER)){
                level().setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                this.addMana(- 100F / this.flameLevel);
                LevelUtil.playSound(level(), SoundEvents.LAVA_EXTINGUISH, SoundSource.AMBIENT, MathHelper.toVec3(pos));
            }
        }
    }

    public int getFlameCnt(){
        return Mth.ceil(getFlameAmount() / MAX_AMOUNT * 10);
    }

    public float getCoverWidth(){
        return (getFlameLevel() + getFlameAmount() / MAX_AMOUNT * 0.5F);
    }

    public float getCoverHeight(){
        return getCoverWidth() + 1F;
    }

    private float flamePercent(){
        return this.getFlameAmount() / MAX_AMOUNT;
    }

    /**
     * get flame particles by spiritual flame level.
     */
    public static ParticleOptions getFlameParticleType(int level) {
        return level <= 1 ? ParticleTypes.FLAME :
                level == 2 ? ParticleTypes.SOUL_FIRE_FLAME :
                        IMMParticles.SPIRITUAL_FLAME.get();
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
        tag.putFloat("SpiritualFlameAmount", getFlameAmount());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if(tag.contains("SpiritualFlameLevel")) {
            setFlameLevel(tag.getInt("SpiritualFlameLevel"));
        }
        if(tag.contains("SpiritualFlameAmount")){
            setFlameAmount(tag.getFloat("SpiritualFlameAmount"));
        }
    }

    public void setFlameLevel(int level) {
        this.flameLevel = level;
    }

    public int getFlameLevel() {
        return this.flameLevel;
    }

    public void setFullAmount(){
        this.setFlameAmount(MAX_AMOUNT);
    }

    public void setFlameAmount(float amount) {
        entityData.set(FLAME_AMOUNT, Mth.clamp(amount, 0, MAX_AMOUNT));
    }

    public float getFlameAmount() {
        return entityData.get(FLAME_AMOUNT);
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected boolean canRide(Entity entity) {
        return false;
    }

    @Override
    public IRealmType getRealm() {
        return RealmTypes.SPIRITUAL_FLAME_1;
    }

    @Override
    public Optional<RealmStages> getRealmStageOpt() {
        return Optional.empty();
    }

    @Override
    public float getMana() {
        return this.getFlameAmount();
    }

    @Override
    public void addMana(float amount) {
        this.setFlameAmount(this.getFlameAmount() + amount);
    }

    @Override
    public float getMaxMana() {
        return MAX_AMOUNT;
    }

}
