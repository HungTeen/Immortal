package hungteen.imm.common.entity.creature.monster;

import com.mojang.serialization.Dynamic;
import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.htlib.util.helper.registry.ParticleHelper;
import hungteen.imm.api.enums.Elements;
import hungteen.imm.api.records.Spell;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.api.registry.ISpiritualType;
import hungteen.imm.common.ElementManager;
import hungteen.imm.common.effect.IMMEffects;
import hungteen.imm.common.entity.IMMGrowableMob;
import hungteen.imm.common.entity.ai.IMMMemories;
import hungteen.imm.common.impl.registry.RealmTypes;
import hungteen.imm.common.impl.registry.SpiritualTypes;
import hungteen.imm.common.misc.IMMSounds;
import hungteen.imm.common.spell.SpellTypes;
import hungteen.imm.util.ParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 毕方鸟。
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/10/12 18:35
 **/
public class BiFang extends IMMGrowableMob implements Enemy {

    private static final UUID SPEED_UUID = UUID.fromString("7d543102-6cdf-11ee-b962-0242ac120002");
    private static final UUID ATTACK_UUID = UUID.fromString("7d5434b8-6cdf-11ee-b962-0242ac120002");
    private static final UUID ARMOR_UUID = UUID.fromString("7d5435e4-6cdf-11ee-b962-0242ac120002");
    private static final AttributeModifier SPEED_MODIFIER = new AttributeModifier(SPEED_UUID, "speed boost", 0.5D, AttributeModifier.Operation.MULTIPLY_BASE);
    private static final AttributeModifier ATTACK_MODIFIER = new AttributeModifier(ATTACK_UUID, "attack boost", 3D, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier ARMOR_MODIFIER = new AttributeModifier(ARMOR_UUID, "armor boost", 4D, AttributeModifier.Operation.ADDITION);
    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(BiFang.class, EntityDataSerializers.BOOLEAN);
    public final AnimationState flyAnimationState = new AnimationState();
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState idle1AnimationState = new AnimationState();
    public final AnimationState idle2AnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();
    public final AnimationState roarAnimationState = new AnimationState();
    public final AnimationState shootAnimationState = new AnimationState();
    public final AnimationState flapAnimationState = new AnimationState();
    private final MoveControl walkMoveController;
    private final MoveControl flyMoveController;
    private GroundPathNavigation groundPathNavigation;
    private FlyingPathNavigation flyingPathNavigation;
    private int groundTick = 0;

    public BiFang(EntityType<? extends IMMGrowableMob> type, Level level) {
        super(type, level);
        this.walkMoveController = new BiFangMoveControl(this);
        this.flyMoveController = new FlyingMoveControl(this, 30, true);
        this.moveControl = this.walkMoveController;
        this.getNavigation().setCanFloat(true);
        this.setPathfindingMalus(BlockPathTypes.LAVA, 8.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.0F);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(FLYING, false);
    }

    protected Brain.Provider<BiFang> brainProvider() {
        return Brain.provider(List.of(
                /* Nearest Living Entities Sensor */
                MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
                /* Nearest Players Sensor */
                MemoryModuleType.NEAREST_PLAYERS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
                /* Nearest Item Sensor */
                MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM,
                /* Nearest Bed Sensor */
                MemoryModuleType.NEAREST_BED,
                /* Hurt By Sensor */
                MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY,
                /* MoveToTargetSink Behavior*/
                MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.WALK_TARGET,
                /* GoToWantedItem Behavior */
                MemoryModuleType.LOOK_TARGET,
                /* LookAnInteract Behavior */
                MemoryModuleType.INTERACTION_TARGET,
                /* Fight */
                MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN,
                /* Custom */
                IMMMemories.SPELL_COOLING_DOWN.get(),
                IMMMemories.IDLE_COOLING_DOWN.get(),
                MemoryModuleType.IS_PANICKING
        ), List.of(
                SensorType.NEAREST_LIVING_ENTITIES,
                SensorType.NEAREST_PLAYERS,
                SensorType.HURT_BY
        ));
    }

    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return BiFangAi.makeBrain(this.brainProvider().makeBrain(dynamic));
    }

    public Brain<BiFang> getBrain() {
        return (Brain<BiFang>)super.getBrain();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5)
                .add(Attributes.MOVEMENT_SPEED, 0.6D)
                .add(Attributes.FLYING_SPEED, 1F)
                .add(Attributes.ATTACK_DAMAGE, 7F)
                .add(Attributes.FOLLOW_RANGE, 60)
                .add(Attributes.ARMOR, 4.0D)
                ;
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return this.getGroundPathNavigation(level);
    }

    @Override
    protected Collection<ISpiritualType> createSpiritualRoots(ServerLevelAccessor accessor) {
        return List.of(SpiritualTypes.FIRE, SpiritualTypes.WOOD);
    }

    @Override
    protected List<Spell> createLearnSpells() {
        return List.of(Spell.create(SpellTypes.INTIMIDATE));
    }

    @Override
    public void updateRealmByAge(int age) {
        switch (age) {
            case 1 -> this.setRealm(RealmTypes.MONSTER_LEVEL_2);
            default -> this.setRealm(RealmTypes.MONSTER_LEVEL_3);
        }
        this.addMana(this.getMaxMana());
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> dataAccessor) {
        super.onSyncedDataUpdated(dataAccessor);
        if(dataAccessor.equals(CURRENT_ANIMATION)){
            this.resetAnimations();
            switch (this.getCurrentAnimation()){
                case IDLING -> {
                    this.idleAnimationState.startIfStopped(this.tickCount);
                }
                case IDLING_1 -> {
                    this.idle1AnimationState.startIfStopped(this.tickCount);
                }
                case IDLING_2 -> {
                    this.idle2AnimationState.startIfStopped(this.tickCount);
                }
                case FLAP -> {
                    this.flapAnimationState.startIfStopped(this.tickCount);
                }
                case SHOOT -> {
                    this.shootAnimationState.startIfStopped(this.tickCount);
                }
                case ROAR -> {
                    this.roarAnimationState.startIfStopped(this.tickCount);
                }
            }
        }
    }

    @Override
    protected void customServerAiStep() {
        this.level().getProfiler().push("biFangBrain");
        this.getBrain().tick((ServerLevel)this.level(), this);
        this.level().getProfiler().pop();
        this.level().getProfiler().push("biFangActivityUpdate");
        BiFangAi.updateActivity(this);
        this.level().getProfiler().pop();
        super.customServerAiStep();
    }

    @Override
    public void tick() {
        super.tick();
        if(this.isFlying()){
            this.moveControl = this.flyMoveController;
            this.navigation = this.getFlyingPathNavigation(level());
        } else {
            this.moveControl = this.walkMoveController;
            this.navigation = this.getGroundPathNavigation(level());
        }
        if(EntityHelper.isServer(this)){
            if(this.tickCount % 50 == 0 && this.getRandom().nextFloat() < 0.35F){
                ElementManager.addElementAmount(this, Elements.WOOD, false, 20);
            }
        } else {
            if(this.onGround()){
                if(this.groundTick < 5){
                    ++ this.groundTick;
                }
            } else {
                this.groundTick = 0;
            }
            this.flyAnimationState.animateWhen(! (this.onGround() && this.groundTick >= 5), this.tickCount);
            switch (this.getCurrentAnimation()){
                case ROAR -> {
                    if(atAnimationTick(15)) this.playSound(IMMSounds.BI_FANG_ROAR.get());
                    else if(atAnimationTick(30)) this.setIdle();
                }
                case FLAP -> {
                    if(inFlameRange()){
                        ParticleUtil.spawnEntityParticle(this, ParticleTypes.FLAME, 20, 0.15F);
                    }
                }
            }
            if(this.isFlying()){
                if(this.getMana() / this.getMaxMana() >= 0.5F){
                    ParticleUtil.spawnEntityParticle(this, ParticleTypes.FLAME, 5, 0.15F);
                }
            }
        }
    }

    @Override
    protected void usedSpell(@NotNull Spell spell) {
        this.setCurrentAnimation(AnimationTypes.ROAR);
    }

    @Override
    public boolean canUseSpell(ISpellType spell) {
        return EntityHelper.isEntityValid(this.getTarget()) && this.distanceTo(this.getTarget()) < 20 && ! this.getTarget().hasEffect(IMMEffects.OPPRESSION.get());
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(super.hurt(source, amount)){
            if(source.getEntity() instanceof LivingEntity living){
                BiFangAi.wasHurtBy(this, living, amount);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        this.serverChange(MELEE_ATTACK_ID);
        this.addMana(this.getRandom().nextFloat() * 5);
        return super.doHurtTarget(entity);
    }

    @Override
    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);
        switch (id) {
            case MELEE_ATTACK_ID -> this.attackAnimationState.start(this.tickCount);
        }
    }

    protected boolean inFlameRange(){
        return this.inAnimationRange(3, 7);
    }

    @Override
    public void setAge(int age) {
        super.setAge(age);
        if (this.level() != null && !this.level().isClientSide) {
            AttributeInstance attackAttributes = this.getAttribute(Attributes.ATTACK_DAMAGE);
            AttributeInstance armorAttributes = this.getAttribute(Attributes.ARMOR);
            attackAttributes.removeModifier(ATTACK_MODIFIER);
            armorAttributes.removeModifier(ARMOR_MODIFIER);
            if (isMature()) {
                attackAttributes.addTransientModifier(ATTACK_MODIFIER);
                armorAttributes.addTransientModifier(ARMOR_MODIFIER);
            }
        }
    }

    @Override
    public int getMaxAge() {
        return 2;
    }

    @Override
    public boolean canNaturallyGrow() {
        return false;
    }

    @Override
    protected void checkFallDamage(double p_20990_, boolean p_20991_, BlockState p_20992_, BlockPos p_20993_) {
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.8F;
    }

    private void resetAnimations() {
        this.idleAnimationState.stop();
        this.idle1AnimationState.stop();
        this.idle2AnimationState.stop();
        this.roarAnimationState.stop();
        this.shootAnimationState.stop();
        this.flapAnimationState.stop();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return IMMSounds.BI_FANG_AMBIENT.get();
    }

    @Override
    public int getAmbientSoundInterval() {
        return 160;
    }

    @Override
    public boolean ignoreExplosion() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }

    @Override
    protected void jumpFromGround() {
        this.getBrain().getMemory(MemoryModuleType.WALK_TARGET).ifPresentOrElse(walkTarget -> {
            final Vec3 vec3 = walkTarget.getTarget().currentPosition().subtract(this.position()).normalize().scale(this.getAttributeValue(Attributes.MOVEMENT_SPEED));
            this.setDeltaMovement(vec3.x, this.getJumpPower(), vec3.z);
        }, super::jumpFromGround);
    }

    public FlyingPathNavigation getFlyingPathNavigation(Level level) {
        if(this.flyingPathNavigation == null){
            this.flyingPathNavigation = new FlyingPathNavigation(this, level);
        }
        return this.flyingPathNavigation;
    }

    public GroundPathNavigation getGroundPathNavigation(Level level) {
        if(this.groundPathNavigation == null){
            this.groundPathNavigation = new GroundPathNavigation(this, level);
        }
        return this.groundPathNavigation;
    }

    @Override
    public float getScale() {
        return this.getAge() == 1 ? 0.6F : 1;
    }

    @Override
    public double getMeleeAttackRangeSqr(LivingEntity entity) {
        return (this.getBbWidth() + 1) * 2.0F * (this.getBbWidth() + 1) * 2.0F + entity.getBbWidth();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if(tag.contains("BiFangFlying")){
            this.setFlying(tag.getBoolean("BiFangFlying"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("BiFangFlying", this.isFlying());
    }

    public void setFlying(boolean flying){
        entityData.set(FLYING, flying);
    }

    public boolean isFlying() {
        return entityData.get(FLYING);
    }

    static class BiFangMoveControl extends MoveControl {

        private int jumpDelay;
        private final BiFang biFang;

        public BiFangMoveControl(BiFang biFang) {
            super(biFang);
            this.biFang = biFang;
        }

        public void tick() {
            if (this.operation == MoveControl.Operation.MOVE_TO) {
                this.operation = MoveControl.Operation.WAIT;
                double d0 = this.wantedX - this.mob.getX();
                double d1 = this.wantedY - this.mob.getY();
                double d2 = this.wantedZ - this.mob.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                if (d3 < (double)2.5000003E-7F) {
                    this.mob.setYya(0.0F);
                    this.mob.setZza(0.0F);
                    return;
                }

                float f = (float)(Mth.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
                this.mob.setYRot(this.rotlerp(this.mob.getYRot(), f, 90.0F));
                this.mob.yHeadRot = this.mob.getYRot();
                this.mob.yBodyRot = this.mob.getYRot();
                if (this.mob.onGround() && d3 >= 10) {
                    if (this.jumpDelay-- <= 0) {
                        this.jumpDelay = 20;

                        this.biFang.getJumpControl().jump();
//                        if (this.biFang.doPlayJumpSound()) {
//                            this.biFang.playSound(this.biFang.getJumpSound(), this.biFang.getSoundVolume(), this.biFang.getSoundPitch());
//                        }
                    } else {
                        this.biFang.xxa = 0.0F;
                        this.biFang.zza = 0.0F;
                        this.mob.setSpeed(0.0F);
                    }
                } else {
                    this.mob.setSpeed(0.05F);
                }
            }
        }
    }

}