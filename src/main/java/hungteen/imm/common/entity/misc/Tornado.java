package hungteen.imm.common.entity.misc;

import hungteen.htlib.common.entity.HTEntity;
import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.imm.api.enums.Elements;
import hungteen.imm.common.ElementManager;
import hungteen.imm.util.EntityUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/10/19 21:08
 **/
public class Tornado extends HTEntity implements TraceableEntity {

    private static final EntityDataAccessor<Float> SCALE = SynchedEntityData.defineId(Tornado.class, EntityDataSerializers.FLOAT);
    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUUID;

    public Tornado(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(SCALE, 1F);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        super.onSyncedDataUpdated(accessor);
        if(accessor.equals(SCALE)){
            this.refreshDimensions();
        }
    }

    @Override
    public void tick() {
        super.tick();
        tickMove(0.99F, 0.99F);
        if(EntityHelper.isServer(this)){
            EntityUtil.forRange(this, LivingEntity.class, getBbWidth(), getBbHeight(), target -> {
                return target != getOwner();
            }, (target, scale) -> {
                if(isFireTornado()){
                    target.hurt(this.damageSources().inFire(), Math.min(1, 3F * scale * getScale()));
                }
                ElementManager.addElementAmount(target, Elements.WOOD, false, getScale() * (1 + 5 * scale), 20);
            });
            if(this.tickCount >= 500){
                this.discard();
            }
        } else {

        }
    }

    public boolean isFireTornado(){
        return ElementManager.hasElement(this, Elements.FIRE, false);
    }

    public void setOwner(@Nullable LivingEntity entity) {
        this.owner = entity;
        this.ownerUUID = entity == null ? null : entity.getUUID();
    }

    @Nullable
    @Override
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.level() instanceof ServerLevel) {
            Entity entity = ((ServerLevel)this.level()).getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity)entity;
            }
        }
        return this.owner;
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return super.getDimensions(pose).scale(this.getScale());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.hasUUID("Owner")) {
            this.ownerUUID = tag.getUUID("Owner");
        }
        if(tag.contains("Scale")){
            this.setScale(tag.getFloat("Scale"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        if (this.ownerUUID != null) {
            tag.putUUID("Owner", this.ownerUUID);
        }
        tag.putFloat("Scale", this.getScale());
    }

    public void setScale(float scale) {
        entityData.set(SCALE, scale);
    }

    public float getScale() {
        return entityData.get(SCALE);
    }

    @Override
    protected float getGravityVelocity() {
        return 0.001F;
    }
}
