package hungteen.imm.common.entity.golem;

import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.cultivation.QiRootType;
import hungteen.imm.common.cultivation.QiRootTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.List;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-04-25 21:48
 **/
public class CreeperGolem extends GolemEntity {

    private static final EntityDataAccessor<Integer> SWELL_DIR = SynchedEntityData.defineId(CreeperGolem.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_IGNITED = SynchedEntityData.defineId(CreeperGolem.class, EntityDataSerializers.BOOLEAN);
    private int oldSwell;
    private int swell;
    private int maxSwell = 30;
    private int explosionRadius = 3;

    public CreeperGolem(EntityType<? extends GolemEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SWELL_DIR, -1);
        builder.define(IS_IGNITED, false);
    }

    public static AttributeSupplier createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .build();
    }

    @Override
    public void tick() {
        if (this.isAlive()) {
            this.oldSwell = this.swell;
            if (this.isIgnited()) {
                this.setSwellDir(1);
            }

            int i = this.getSwellDir();
            if (i > 0 && this.swell == 0) {
                this.playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.5F);
                this.gameEvent(GameEvent.PRIME_FUSE);
            }

            this.swell += i;
            if (this.swell < 0) {
                this.swell = 0;
            }

            if (this.swell >= this.maxSwell) {
                this.swell = this.maxSwell;
                this.explodeCreeper();
            }
        }
        super.tick();
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        this.setIgnite(true);
        return true;
    }

    private void explodeCreeper() {
        if (EntityHelper.isServer(this)) {
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), this.explosionRadius, Level.ExplosionInteraction.MOB);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putShort("Fuse", (short)this.maxSwell);
        tag.putByte("ExplosionRadius", (byte)this.explosionRadius);
        tag.putBoolean("Ignited", this.isIgnited());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Fuse", 99)) {
            this.maxSwell = tag.getShort("Fuse");
        }

        if (tag.contains("ExplosionRadius", 99)) {
            this.explosionRadius = tag.getByte("ExplosionRadius");
        }
        if (tag.getBoolean("Ignited")) {
            this.setIgnite(tag.getBoolean("Ignited"));
        }

    }

    public float getSwelling(float partialTick) {
        return Mth.lerp(partialTick, (float)this.oldSwell, (float)this.swell) / (float)(this.maxSwell - 2);
    }

    public int getSwellDir() {
        return this.entityData.get(SWELL_DIR);
    }

    public void setSwellDir(int swellDir) {
        this.entityData.set(SWELL_DIR, swellDir);
    }

    public boolean isIgnited() {
        return this.entityData.get(IS_IGNITED);
    }

    public void setIgnite(boolean ignite) {
        this.entityData.set(IS_IGNITED, ignite);
    }

    @Override
    public int getMeleeAttackCD() {
        return 1200;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.CREEPER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.CREEPER_DEATH;
    }

    @Override
    public List<QiRootType> getRoots() {
        return List.of(QiRootTypes.FIRE);
    }

    @Override
    public int getRuneInventorySize() {
        return 9;
    }
}
