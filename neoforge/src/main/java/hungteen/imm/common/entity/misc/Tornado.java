package hungteen.imm.common.entity.misc;

import hungteen.htlib.common.entity.HTEntity;
import hungteen.htlib.util.helper.RandomHelper;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.common.ElementManager;
import hungteen.imm.common.misc.damage.IMMDamageSources;
import hungteen.imm.util.EntityUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

/**
 * @program Immortal
 * @author PangTeen
 * @create 2023/10/19 21:08
 **/
public class Tornado extends HTEntity implements TraceableEntity, IEntityWithComplexSpawn {

    private static final EntityDataAccessor<Float> SCALE = SynchedEntityData.defineId(Tornado.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> REST_TICK = SynchedEntityData.defineId(Tornado.class, EntityDataSerializers.INT);
    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUUID;
    private int targetId;
    @Nullable
    private Entity targetEntity;
    private int workDuration;
    private boolean clockWise;
    private float speed;

    public Tornado(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SCALE, 1F);
        builder.define(REST_TICK, 0);
    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        buffer.writeInt(this.targetId);
        buffer.writeBoolean(this.clockWise);
        buffer.writeFloat(this.speed);
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf additionalData) {
        this.targetId = additionalData.readInt();
        this.clockWise = additionalData.readBoolean();
        this.speed = additionalData.readFloat();
    }

    public void shootTo(@Nullable Entity target, int duration, boolean clockWise, float speed) {
        if (target != null) {
            this.setTargetId(target.getId());
        }
        this.setWorkDuration(duration);
        this.setClockWise(clockWise);
        this.setSpeed(speed);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        super.onSyncedDataUpdated(accessor);
        if (accessor.equals(SCALE)) {
            this.refreshDimensions();
        }
    }

    @Override
    public void tick() {
        this.noPhysics = true;
        super.tick();
        this.getTarget().ifPresentOrElse(target -> {
            final Vec3 vec = target.position().subtract(position()).normalize();
            final Vec3 cur = getDeltaMovement().normalize();
            final float changeSpeed = (this.getRestTick() <= 0 ? 0.012F : 0.1F);
            this.setDeltaMovement((cur.add(vec.scale(changeSpeed))).normalize().scale(getSpeed()));
        }, () -> {
            final Vec3 speed = this.getDeltaMovement();
            this.setDeltaMovement(speed.x, Math.max(0, speed.y), speed.z);
        });
        tickMove(0.99F, 0.99F);
        if (EntityHelper.isServer(this)) {
            if(this.tickCount % 20 == 0 && this.random.nextFloat() < 0.5F && ElementManager.hasElement(this, Element.WOOD, false)){
                ElementManager.addElementAmount(this, Element.WOOD, false, 20);
            }
            if (this.getRestTick() > 0) {
                this.setRestTick(this.getRestTick() - 1);
            }
            EntityUtil.forRange(this, LivingEntity.class, getBbWidth(), getBbHeight(), target -> {
                return target != getOwner();
            }, (target, scale) -> {
                if (isFireTornado()) {
                    target.hurt(IMMDamageSources.fireElement(this), Math.min(1, 2F * scale * getScale()));
                    ElementManager.addElementAmount(target, Element.FIRE, false, 2 * getScale(), 20);
                } else {
                    target.hurt(IMMDamageSources.woodElement(this), Math.min(1, 1F * scale * getScale()));
                }
                ElementManager.addElementAmount(target, Element.WOOD, false, 3 * getScale(), 20);
                if (this.getRestTick() == 0) {
                    this.setRestTick(RandomHelper.getMinMax(random, 100, 200));
                }
            });
            if (this.tickCount >= this.getWorkDuration()) {
                this.discard();
            }
        }
    }

    public boolean isFireTornado() {
        return ElementManager.hasElement(this, Element.FIRE, false) || this.isOnFire();
    }

    public void setOwner(@Nullable LivingEntity entity) {
        this.owner = entity;
        this.ownerUUID = entity == null ? null : entity.getUUID();
    }

    @Nullable
    @Override
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.level() instanceof ServerLevel) {
            Entity entity = ((ServerLevel) this.level()).getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity) entity;
            }
        }
        return this.owner;
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return super.getDimensions(pose).scale(this.getScale());
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return super.canBeCollidedWith();
    }

    public Optional<Entity> getTarget() {
        if (this.targetEntity == null) {
            this.targetEntity = level().getEntity(this.targetId);
        }
        return Optional.ofNullable(this.targetEntity);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.hasUUID("Owner")) {
            this.ownerUUID = tag.getUUID("Owner");
        }
        if (tag.contains("Scale")) {
            this.setScale(tag.getFloat("Scale"));
        }
        if (tag.contains("RestTick")) {
            this.setRestTick(tag.getInt("RestTick"));
        }
        if (tag.contains("TargetId")) {
            this.setTargetId(tag.getInt("TargetId"));
        }
        if (tag.contains("WorkDuration")) {
            this.setWorkDuration(tag.getInt("WorkDuration"));
        }
        if (tag.contains("ClockWise")) {
            this.setClockWise(tag.getBoolean("ClockWise"));
        }
        if (tag.contains("MoveSpeed")) {
            this.setSpeed(tag.getFloat("MoveSpeed"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        if (this.ownerUUID != null) {
            tag.putUUID("Owner", this.ownerUUID);
        }
        tag.putFloat("Scale", this.getScale());
        tag.putInt("RestTick", this.getRestTick());
        tag.putInt("TargetId", this.getTargetId());
        tag.putInt("WorkDuration", this.getWorkDuration());
        tag.putBoolean("ClockWise", this.isClockWise());
        tag.putFloat("MoveSpeed", this.getSpeed());
    }

    public void setScale(float scale) {
        entityData.set(SCALE, scale);
    }

    public float getScale() {
        return entityData.get(SCALE);
    }

    public void setRestTick(int tick) {
        entityData.set(REST_TICK, tick);
    }

    public int getRestTick() {
        return entityData.get(REST_TICK);
    }

    @Override
    protected float getGravityVelocity() {
        return 0F;
    }

    public void setClockWise(boolean clockWise) {
        this.clockWise = clockWise;
    }

    public boolean isClockWise() {
        return clockWise;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setWorkDuration(int workDuration) {
        this.workDuration = workDuration;
    }

    public int getWorkDuration() {
        return workDuration;
    }

}
