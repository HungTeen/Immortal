package hungteen.imm.common.entity.creature.spirit;

import hungteen.htlib.util.helper.RandomHelper;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.cultivation.QiRootType;
import hungteen.imm.common.ElementManager;
import hungteen.imm.common.entity.IMMMob;
import hungteen.imm.common.cultivation.QiRootTypes;
import hungteen.imm.util.ParticleUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * @program Immortal
 * @author PangTeen
 * @create 2023/10/22 10:08
 **/
public class WaterSpirit extends ElementSpirit {

    private static final EntityDataAccessor<OptionalInt> VEHICLE_ID = SynchedEntityData.defineId(WaterSpirit.class, EntityDataSerializers.OPTIONAL_UNSIGNED_INT);

    public WaterSpirit(EntityType<? extends IMMMob> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 10, true);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VEHICLE_ID, OptionalInt.empty());
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, FireSpirit.class, true));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.1F, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ElementSpirit.createAttributes()
                .add(Attributes.MAX_HEALTH, 16.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.FLYING_SPEED, 0.45D)
                ;
    }

    public static boolean isWaterSpiritRiding(Entity entity) {
        return entity.getPassengers().stream().anyMatch(WaterSpirit.class::isInstance);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        super.onSyncedDataUpdated(accessor);
        if(accessor.equals(VEHICLE_ID)){
            getOptVehicle().ifPresentOrElse(this::startRiding, this::stopRiding);
        }
    }

    @Override
    public boolean startRiding(Entity entity) {
        if(super.startRiding(entity)){
            this.setServerVehicle(entity.getId());
            return true;
        }
        return false;
    }

    @Override
    public void stopRiding() {
        super.stopRiding();
        this.clearServerVehicle();
    }

    /**
     * See drown damage handle in {@link LivingEntity#baseTick()}.
     */
    @Override
    public boolean doHurtTarget(Entity target) {
        if(target instanceof LivingEntity living){
            this.startRiding(target);
            if(living.canDrownInFluidType(NeoForgeMod.WATER_TYPE.value())) {
                ElementManager.addElementAmount(target, Element.WATER, false, 3F, 5F);
            }
            if(living.getAirSupply() <= 0){
                living.setAirSupply(living.getAirSupply() - 5);
            } else {
                living.setAirSupply(Math.max(0, living.getAirSupply() - RandomHelper.getMinMax(living.getRandom(), 20, 40)));
            }
            ParticleUtil.spawnEntityParticle(living, ParticleTypes.BUBBLE, 8, 0.1);
            if (living.getAirSupply() <= -19) {
                living.setAirSupply(0);
                living.hurt(living.damageSources().drown(), 3.0F);
            }
            return true;
        }
        return super.doHurtTarget(target);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(source.is(DamageTypeTags.IS_FIRE)) {
            amount /= 2;
        }
        return super.hurt(source, amount);
    }

    @Nullable
    @Override
    public Entity getControlledVehicle() {
        return super.getControlledVehicle();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if(tag.contains("ServerVehicleId")){
            this.setServerVehicle(tag.getInt("ServerVehicleId"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        getServerVehicle().ifPresent(id -> {
            tag.putInt("ServerVehicleId", id);
        });
    }

    public Optional<Entity> getOptVehicle() {
        if(getServerVehicle().isPresent()){
            return Optional.ofNullable(level().getEntity(getServerVehicle().getAsInt()));
        }
        return Optional.empty();
    }

    public OptionalInt getServerVehicle(){
        return entityData.get(VEHICLE_ID);
    }

    public void setServerVehicle(int vehicle){
        entityData.set(VEHICLE_ID, OptionalInt.of(vehicle));
    }

    public void clearServerVehicle(){
        entityData.set(VEHICLE_ID, OptionalInt.empty());
    }

    @Override
    public boolean canDrownInFluidType(FluidType type) {
        if (type == NeoForgeMod.WATER_TYPE.value()) {
            return false;
        }
        return super.canDrownInFluidType(type);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new FlyingPathNavigation(this, level);
    }

    @Override
    public QiRootType getSpiritualRoot() {
        return QiRootTypes.WATER;
    }

    @Override
    public Element getElement() {
        return Element.WATER;
    }

}
