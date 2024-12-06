package hungteen.imm.common.entity.misc;

import hungteen.htlib.util.helper.JavaHelper;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.htlib.util.helper.impl.ParticleHelper;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.cultivation.ElementManager;
import hungteen.imm.common.misc.IMMSounds;
import hungteen.imm.common.misc.damage.IMMDamageSources;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.NBTUtil;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/6 9:48
 **/
public class FallingIceEntity extends HTTraceableEntity implements IEntityWithComplexSpawn{

    private static final EntityDataAccessor<Float> ICE_SCALE = SynchedEntityData.defineId(FallingIceEntity.class, EntityDataSerializers.FLOAT);
    public float lastScale = 0.01F;
    private int floatTicks = 60;

    public FallingIceEntity(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ICE_SCALE, 0.1F);
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf additionalData) {
        this.setFloatTicks(additionalData.readInt());
    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        buffer.writeInt(this.getFloatTicks());
    }

    @Override
    public void tick() {
        super.tick();
        if(EntityHelper.isServer(this)){
            if(this.isFloating()){
                this.setIceScale(Math.clamp(this.tickCount * 1.0F / (this.floatTicks + 1), 0, 1));
                if(! this.isFloating()){
                    playSound(IMMSounds.FALLING_ICE_FINISH.get());
                    this.setDeltaMovement(new Vec3(0, -0.25, 0));
                }
            }
            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if(hitresult.getType() != HitResult.Type.MISS){
                // 加一个速度判断，防止卡住。
                if(hitresult instanceof BlockHitResult blockHitResult || (!this.isFloating() && Math.abs(this.getDeltaMovement().y()) < 0.01) || this.isInWater()){
                    this.onHit();
                }
            }
        } else {
            ParticleHelper.spawnClientParticles(this.level(), ParticleTypes.CLOUD, position(), 3, 1.5D, 0.1D);
        }
        this.tickMove();
    }

    public void onHit(){
        if(this.level() instanceof ServerLevel serverLevel){
            playSound(IMMSounds.FALLING_ICE_HIT.get());
            ParticleHelper.sendParticles(serverLevel, new BlockParticleOption(ParticleTypes.BLOCK, IMMBlocks.FALLING_ICE.get().defaultBlockState()), getX(), getY(), getZ(), 30, 1.5, 0.2D, 1.5, 0.1D);
            AABB aabb = EntityUtil.getEntityAABB(this).inflate(2, 2, 2);
            EntityHelper.getPredicateEntities(this, aabb, LivingEntity.class, JavaHelper::alwaysTrue).forEach(target -> {
                target.hurt(IMMDamageSources.waterElement(this, this.getOwner()), 5F);
                ElementManager.addPercentElement(target, Element.WATER, false,1.2F);
            });
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if(key.equals(ICE_SCALE)){
            this.refreshDimensions();
        }
    }

    protected boolean canHitEntity(Entity target) {
        return false;
//        if (!target.canBeHitByProjectile()) {
//            return false;
//        } else {
//            Entity entity = this.getOwner();
//            return entity == null || !entity.isPassengerOfSameVehicle(target);
//        }
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return super.getDimensions(pose).scale(this.getIceScale());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        NBTUtil.read(tag, tag::getFloat, "ice_scale", this::setIceScale);
        NBTUtil.read(tag, tag::getInt, "float_ticks", this::setFloatTicks);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        NBTUtil.write(tag::putFloat, "ice_scale", this.getIceScale());
        NBTUtil.write(tag::putInt, "float_ticks", this.getFloatTicks());
    }

    @Override
    public boolean isNoGravity() {
        return this.isFloating();
    }

    public boolean isFloating(){
        return this.getIceScale() < 1;
    }

    public void setIceScale(float scale) {
        this.entityData.set(ICE_SCALE, scale);
    }

    public float getIceScale() {
        return this.entityData.get(ICE_SCALE);
    }

    public void setFloatTicks(int floatTicks) {
        this.floatTicks = floatTicks;
    }

    public int getFloatTicks() {
        return floatTicks;
    }
}
