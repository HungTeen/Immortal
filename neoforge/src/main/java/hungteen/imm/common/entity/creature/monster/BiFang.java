package hungteen.imm.common.entity.creature.monster;

import com.mojang.serialization.Dynamic;
import hungteen.htlib.util.helper.MathHelper;
import hungteen.htlib.util.helper.RandomHelper;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.cultivation.QiRootType;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.api.spell.Spell;
import hungteen.imm.api.spell.SpellType;
import hungteen.imm.common.cultivation.ElementManager;
import hungteen.imm.common.cultivation.QiRootTypes;
import hungteen.imm.common.cultivation.RealmTypes;
import hungteen.imm.common.cultivation.SpellTypes;
import hungteen.imm.common.cultivation.spell.basic.IntimidationSpell;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.entity.IMMGrowableMob;
import hungteen.imm.common.entity.ai.IMMActivities;
import hungteen.imm.common.entity.ai.IMMMemories;
import hungteen.imm.common.misc.IMMSounds;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.ParticleUtil;
import hungteen.imm.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * 毕方鸟。
 *
 * @program Immortal
 * @author PangTeen
 * @create 2023/10/12 18:35
 **/
public class BiFang extends IMMGrowableMob implements Enemy {

    private static final AttributeModifier SPEED_MODIFIER = new AttributeModifier(Util.prefix("speed_boost"), 0.5D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    private static final AttributeModifier ATTACK_MODIFIER = new AttributeModifier(Util.prefix("attack_boost"), 3D, AttributeModifier.Operation.ADD_VALUE);
    private static final AttributeModifier ARMOR_MODIFIER = new AttributeModifier(Util.prefix("armor_boost"), 4D, AttributeModifier.Operation.ADD_VALUE);
    public final AnimationState flyAnimationState = new AnimationState();
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState idle1AnimationState = new AnimationState();
    public final AnimationState idle2AnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();
    public final AnimationState roarAnimationState = new AnimationState();
    public final AnimationState shootAnimationState = new AnimationState();
    public final AnimationState flapAnimationState = new AnimationState();
    private final ServerBossEvent bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(false);
    private BlockPos home = BlockPos.ZERO;
    private boolean spawnFlame = false;
    private int groundTick = 0;

    public BiFang(EntityType<? extends IMMGrowableMob> type, Level level) {
        super(type, level);
        this.moveControl = new BiFangFlyMoveControl(this);
        this.getNavigation().setCanFloat(true);
        this.setPathfindingMalus(PathType.LAVA, 8.0F);
        this.setPathfindingMalus(PathType.DAMAGE_FIRE, 0.0F);
        this.setPathfindingMalus(PathType.DANGER_FIRE, 0.0F);
    }

    @Override
    public void serverFinalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficultyInstance, MobSpawnType spawnType) {
        super.serverFinalizeSpawn(accessor, difficultyInstance, spawnType);
        if(spawnType == MobSpawnType.STRUCTURE){
            this.spawnFlame = true;
        }
    }

    @Override
    protected Brain.Provider<BiFang> brainProvider() {
        return Brain.provider(List.of(
                /* Nearest Living Entities Sensor */
                MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
                /* Nearest Players Sensor */
                MemoryModuleType.NEAREST_PLAYERS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
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
                IMMMemories.UNABLE_MELEE_ATTACK.get(),
                IMMMemories.UNABLE_RANGE_ATTACK.get(),
                IMMMemories.HOME.get()
        ), List.of(
                SensorType.NEAREST_LIVING_ENTITIES,
                SensorType.NEAREST_PLAYERS,
                SensorType.HURT_BY
        ));
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return BiFangAi.makeBrain(this.brainProvider().makeBrain(dynamic));
    }

    @Override
    public Brain<BiFang> getBrain() {
        return (Brain<BiFang>) super.getBrain();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5)
                .add(Attributes.MOVEMENT_SPEED, 0.55D)
                .add(Attributes.FLYING_SPEED, 0.6F)
                .add(Attributes.ATTACK_DAMAGE, 7F)
                .add(Attributes.FOLLOW_RANGE, 60)
                .add(Attributes.ARMOR, 4.0D)
                ;
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new FlyingPathNavigation(this, level);
    }

    @Override
    protected Collection<QiRootType> getInitialRoots(ServerLevelAccessor accessor) {
        return List.of(QiRootTypes.FIRE, QiRootTypes.WOOD);
    }

    @Override
    public List<Spell> getRandomSpells(RandomSource random, Element element, RealmType realm) {
        return List.of(Spell.create(SpellTypes.INTIMIDATION), Spell.create(SpellTypes.WOOD_HEALING));
    }

    @Override
    public void updateRealmByAge(int age) {
        switch (age) {
            case 1 -> this.setRealm(RealmTypes.YAOGUAI_LEVEL_2.pre());
            default -> this.setRealm(RealmTypes.YAOGUAI_LEVEL_3.pre());
        }
        this.addQiAmount(this.getMaxQiAmount());
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> dataAccessor) {
        super.onSyncedDataUpdated(dataAccessor);
        if (dataAccessor.equals(CURRENT_ANIMATION)) {
            this.resetAnimations();
            switch (this.getCurrentAnimation()) {
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
        this.getBrain().tick((ServerLevel) this.level(), this);
        this.level().getProfiler().pop();
        this.level().getProfiler().push("biFangActivityUpdate");
        BiFangAi.updateActivity(this);
        this.level().getProfiler().pop();
        super.customServerAiStep();
        if(this.getBrain().isActive(IMMActivities.HOME.get()) && this.tickCount % 5 == 0){
            this.heal(1);
        }
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public void tick() {
        super.tick();
        if (EntityHelper.isServer(this)) {
            if (this.tickCount % 50 == 0 && this.getRandom().nextFloat() < 0.35F) {
                ElementManager.addElementAmount(this, Element.WOOD, true, 20);
            }
            switch (this.getCurrentAnimation()) {
                case ROAR -> {
                    if (atAnimationTick(15)) {
                        this.playSound(IMMSounds.BI_FANG_ROAR.get());
                    } else if (atAnimationTick(30)) {
                        this.setIdle();
                    }
                }
            }
        } else {
            this.flyAnimationState.animateWhen(this.flyPredicate(), this.tickCount);
            switch (this.getCurrentAnimation()) {
                case FLAP -> {
                    if (inAnimationRange(3, 7)) {
                        for(int i = 0; i < 10; ++ i){
                            final Vec3 pos = getEyePosition().add(this.getLookAngle().normalize().scale(0.5F)).offsetRandom(getRandom(), 1.5F);
                            final Vec3 speed = this.getLookAngle().normalize().scale(0.15F);
                            level().addParticle(ParticleTypes.CLOUD, pos.x, pos.y, pos.z, speed.x, speed.y, speed.z);
                        }
                    }
                }
            }
            if (this.getQiAmount() / this.getMaxQiAmount() >= 0.5F) {
                ParticleUtil.spawnEntityParticle(this, ParticleTypes.FLAME, 5, 0.15F);
            }
        }
    }

    @Override
    protected void usedSpell(@NotNull Spell spell) {
        if(spell.spell() == SpellTypes.INTIMIDATION){
            this.setCurrentAnimation(AnimationTypes.ROAR);
        } else if(spell.spell() == SpellTypes.WOOD_HEALING){
            this.heal(10);
            ParticleUtil.spawnEntityParticle(this, ParticleTypes.HEART, 20, 0.15F);
        }
    }

    @Override
    public boolean canUseSpell(SpellType spell) {
        if(spell == SpellTypes.INTIMIDATION){
            return IntimidationSpell.canUseOn(this, this.getTarget());
        } else if(spell == SpellTypes.WOOD_HEALING){
            return (this.getTarget() == null && this.getHealth() < this.getMaxHealth()) || (this.getTarget() != null && ! this.closerThan(this.getTarget(), 20)) || this.getHealth() < 10;
        }
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        // 近战时免疫投掷物。
        if((this.getBrain().isActive(IMMActivities.MELEE_FIGHT.get()) || this.getBrain().isActive(IMMActivities.HOME.get())) && source.is(DamageTypeTags.IS_PROJECTILE)){
            return false;
        }
        if (super.hurt(source, amount)) {
            if (source.getEntity() instanceof LivingEntity living) {
                BiFangAi.wasHurtBy(this, living, amount);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        this.serverChange(MELEE_ATTACK_ID);
        this.addQiAmount(this.getRandom().nextFloat() * 5);
        return super.doHurtTarget(entity);
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if(level() instanceof ServerLevel level && this.canSpawnFlame()){
            this.getBrain().getMemory(IMMMemories.HOME.get()).flatMap(home -> EntityUtil.spawn(level, IMMEntities.SPIRITUAL_FLAME.get(), MathHelper.toVec3(home), true)).ifPresent(flame -> {
                if (this.getAge() == 1) {
                    flame.setFlameAmount(1);
                } else {
                    flame.setFullAmount();
                }
            });

        }
    }

    public int getPhase() {
        final double percent = getHealth() / getMaxHealth();
        return percent >= 0.5 ? 1 : 2;
    }

    @Override
    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);
        switch (id) {
            case MELEE_ATTACK_ID -> this.attackAnimationState.start(this.tickCount);
        }
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
    public double getEyeY() {
        return getY(0.8);
    }

    private void resetAnimations() {
        this.idleAnimationState.stop();
        this.idle1AnimationState.stop();
        this.idle2AnimationState.stop();
        this.roarAnimationState.stop();
        this.shootAnimationState.stop();
        this.flapAnimationState.stop();
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
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
    public boolean ignoreExplosion(Explosion explosion) {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }

    @Override
    public float getScale() {
        return this.getAge() == 1 ? 0.6F : 1;
    }

    @Override
    protected AABB getAttackBoundingBox() {
        return super.getAttackBoundingBox().inflate(1, 0, 1);
    }

    private boolean flyPredicate(){
        if (this.groundTick == 0) {
            if(this.onGround()){
                return false;
            }
            this.groundTick = -1;
        } else if(this.groundTick < 0){
            this.groundTick = 20;
        } else {
            -- this.groundTick;
        }
        return true;
    }

    @Override
    protected float getFlyingSpeed() {
        return this.getControllingPassenger() instanceof Player ? this.getSpeed() * 0.1F : 0.04F;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if(tag.contains("Home")){
            this.setHome(BlockPos.of(tag.getLong("Home")));
        }
        if(tag.contains("SpawnFlame")){
            this.setSpawnFlame(tag.getBoolean("SpawnFlame"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putLong("Home", this.getHome().asLong());
        tag.putBoolean("SpawnFlame", this.canSpawnFlame());
    }

    public void setHome(BlockPos home) {
        this.home = home;
    }

    public BlockPos getHome() {
        return home;
    }

    public void setSpawnFlame(boolean spawnFlame) {
        this.spawnFlame = spawnFlame;
    }

    public boolean canSpawnFlame() {
        return spawnFlame;
    }

    public static class BiFangFlyMoveControl extends MoveControl {

        private final BiFang biFang;
        private final int maxTurn;
        private final boolean hoversInPlace;
        private long nextChangeTick = 0;
        private float variantSpeed = 0;

        public BiFangFlyMoveControl(BiFang biFang) {
            super(biFang);
            this.biFang = biFang;
            this.maxTurn = 30;
            this.hoversInPlace = false;
        }

        public void setIdle(){
            this.operation = MoveControl.Operation.WAIT;
        }

        @Override
        public void tick() {
            if (this.operation == MoveControl.Operation.MOVE_TO) {
                this.operation = MoveControl.Operation.WAIT;
                this.mob.setNoGravity(true);
                final double dx = this.wantedX - this.mob.getX();
                final double dy = this.wantedY - this.mob.getY();
                final double dz = this.wantedZ - this.mob.getZ();
                final double dis = dx * dx + dy * dy + dz * dz;
                // Too close.
                if (dis < (double) 2.5000003E-7F) {
                    this.mob.setYya(0.0F);
                    this.mob.setZza(0.0F);
                    return;
                }

                final float yRot = (float) (Mth.atan2(dz, dx) * (double) (180F / (float) Math.PI)) - 90.0F;
                this.mob.setYRot(this.rotlerp(this.mob.getYRot(), yRot, 90.0F));
                final float speed = this.getSpeed();
                this.mob.setSpeed(speed);
//                if (this.mob.onGround()) {
//                    this.mob.setZza(0);
//                    this.mob.setXxa(0);
//                }
                final double d = Math.sqrt(dx * dx + dz * dz);
                if (Math.abs(dy) > (double) 1.0E-5F || Math.abs(d) > (double) 1.0E-5F) {
                    final float xRot = (float) (-(Mth.atan2(dy, d) * (double) (180F / (float) Math.PI)));
                    this.mob.setXRot(this.rotlerp(this.mob.getXRot(), xRot, (float) this.maxTurn));
                    this.mob.setYya(dy > 0.0D ? speed : -speed);
                }
            } else if (this.operation == Operation.STRAFE && this.mob.getTarget() != null) {
                this.mob.setNoGravity(true);
                final float speed = this.getSpeed();
                if(this.mob.level().getGameTime() >= this.nextChangeTick){
                    this.nextChangeTick = this.mob.level().getGameTime() + this.mob.getRandom().nextIntBetweenInclusive(5, 20);
                    this.variantSpeed = (float) RandomHelper.getMinMax(this.mob.getRandom(), 2F, 3F) * RandomHelper.getSide(this.mob.getRandom());
                }
                final float dz = this.strafeForwards;
                final float dx = this.strafeRight + this.variantSpeed;
                final float dy = (this.mob.getTarget().getEyeY() + 5 - biFang.getY() > 0) ? 1F : -1F;

//                final float d = Math.min(1, Mth.sqrt(dx * dx + dy * dy + dz * dz));
//                f2 *= f4;
//                f3 *= f4;
//                float f5 = Mth.sin(this.mob.getYRot() * ((float)Math.PI / 180F));
//                float f6 = Mth.cos(this.mob.getYRot() * ((float)Math.PI / 180F));
//                float f7 = f2 * f6 - f3 * f5;
//                float f8 = f3 * f6 + f2 * f5;
//                if (!this.isWalkable(f7, f8)) {
//                    this.strafeForwards = 1.0F;
//                    this.strafeRight = 0.0F;
//                }
                if(! this.mob.closerThan(this.mob.getTarget(), 15)){
                    this.strafeForwards *= -1;
                }
                this.mob.setSpeed(speed);
                this.mob.setZza(this.strafeForwards);
                this.mob.setXxa(dx);
                this.mob.setYya(dy);
//                if (this.mob.onGround()) {
//                    this.mob.setZza(0);
//                    this.mob.setXxa(0);
//                }
                this.operation = MoveControl.Operation.WAIT;
            } else {
                if (!this.hoversInPlace) {
                    this.mob.setNoGravity(false);
                }

                this.mob.setXxa(0);
                this.mob.setYya(0.0F);
                this.mob.setZza(0.0F);
            }
        }

        private float getSpeed(){
            return (float) (this.speedModifier * (this.mob.onGround() ? this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED) : this.mob.getAttributeValue(Attributes.FLYING_SPEED)));
        }

//        private boolean isWalkable(float p_24997_, float p_24998_) {
//            PathNavigation pathnavigation = this.mob.getNavigation();
//            if (pathnavigation != null) {
//                NodeEvaluator nodeevaluator = pathnavigation.getNodeEvaluator();
//                if (nodeevaluator != null && nodeevaluator.getBlockPathType(this.mob.level(), Mth.floor(this.mob.getX() + (double) p_24997_), this.mob.getBlockY(), Mth.floor(this.mob.getZ() + (double) p_24998_)) != BlockPathTypes.WALKABLE) {
//                    return false;
//                }
//            }
//
//            return true;
//        }
    }

}
