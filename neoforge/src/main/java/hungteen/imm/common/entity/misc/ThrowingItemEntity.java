package hungteen.imm.common.entity.misc;

import hungteen.htlib.util.helper.impl.ItemHelper;
import hungteen.htlib.util.helper.impl.ParticleHelper;
import hungteen.imm.client.particle.IMMParticles;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.util.EntityUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/8/17 16:52
 */
public class ThrowingItemEntity extends ThrowableItemProjectile {

    private static final EntityDataAccessor<Boolean> WORK_FINISHED = SynchedEntityData.defineId(ThrowingItemEntity.class, EntityDataSerializers.BOOLEAN);

    public ThrowingItemEntity(EntityType<? extends ThrowableItemProjectile> type, Level level) {
        super(type, level);
        this.refreshDimensions();
    }

    public ThrowingItemEntity(LivingEntity thrower, Level level) {
        super(IMMEntities.THROWING_ITEM.get(), thrower, level);
        this.refreshDimensions();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(WORK_FINISHED, false);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            if (this.random.nextFloat() < 0.1F) {
                ParticleHelper.spawnParticles(this.level(), IMMParticles.QI.get(), this.position(), 1, 0, 0.08);
            }
        }
        if (this.workFinished()) {
            if (!this.noPhysics) {
                this.noPhysics = true;
            }
            Optional.ofNullable(this.getOwner()).ifPresentOrElse(entity -> {
                this.setDeltaMovement(entity.getEyePosition().subtract(this.position()).normalize().scale(1F));
                if (!this.level().isClientSide() && entity.distanceTo(this) < 2) {
                    EntityUtil.addItem(entity, this.getItem());
                    this.discard();
                }
            }, () -> {
                if(!this.level().isClientSide()){
                    this.spawnAtLocation(this.getItem());
                    this.discard();
                }
            });
        } else {
            if(this.tickCount > 100){
                this.setWorkFinished(true);
            }
        }
    }

    @Override
    protected void onHit(HitResult result) {
        if (!this.workFinished()) {
            this.setWorkFinished(true);
            super.onHit(result);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        double damage = 1F + ItemHelper.getItemBonusDamage(this.getItem(), EquipmentSlot.MAINHAND);
        result.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), (float) damage);
        if(EntityUtil.ownerOrSelf(this) instanceof LivingEntity living && living.level() instanceof ServerLevel serverLevel){
            this.getItem().hurtAndBreak(3, serverLevel, living, (e) -> {
                this.discard();
            });
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("WorkFinished")) {
            this.setWorkFinished(tag.getBoolean("WorkFinished"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("WorkFinished", this.workFinished());
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return this.getItem().getItem() instanceof BlockItem ? EntityDimensions.scalable(1F, 1F) : EntityDimensions.scalable(0.5F, 0.5F);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.SNOWBALL;
    }

    public boolean workFinished() {
        return entityData.get(WORK_FINISHED);
    }

    public void setWorkFinished(boolean finished) {
        entityData.set(WORK_FINISHED, finished);
    }
}
