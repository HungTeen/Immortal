package hungteen.imm.common.entity.creature.spirit;

import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.imm.api.enums.Elements;
import hungteen.imm.api.registry.ISpiritualType;
import hungteen.imm.client.render.entity.spirit.FireSpiritRender;
import hungteen.imm.common.ElementManager;
import hungteen.imm.common.entity.IMMMob;
import hungteen.imm.common.impl.registry.SpiritualTypes;
import hungteen.imm.common.misc.damage.IMMDamageSources;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.ParticleUtil;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 25.10.2023 12:16
 **/
public class FireSpirit extends ElementSpirit{

    public float targetSquish;
    public float squish;
    public float oSquish;
    private boolean wasOnGround;

    public FireSpirit(EntityType<? extends IMMMob> type, Level level) {
        super(type, level);
        this.moveControl = new SpiritJumpMoveControl(this);
    }

    @Override
    protected void registerGoals() {
        this.addTargetGoals();
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, MetalSpirit.class, true));
        this.goalSelector.addGoal(2, new SpiritAttackGoal(this));
        this.goalSelector.addGoal(3, new SpiritRandomMoveGoal(this));
        this.goalSelector.addGoal(5, new SpiritKeepOnJumpingGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ElementSpirit.createAttributes()
                .add(Attributes.MAX_HEALTH, 14.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.27D)
                ;
    }

    @Override
    public void tick() {
        this.squish += (this.targetSquish - this.squish) * 0.5F;
        this.oSquish = this.squish;
        super.tick();
        if(EntityHelper.isServer(this)){
            if(this.tickCount % 5 == 0 && this.getRandom().nextFloat() < 0.25){
                ParticleUtil.spawnEntityParticle(this, ParticleTypes.FLAME, 10, 0.1);
            }
        }
        if (this.onGround() && !this.wasOnGround) {
            this.targetSquish = -0.5F;
        } else if (!this.onGround() && this.wasOnGround) {
            this.targetSquish = 1.0F;
        }
        this.wasOnGround = this.onGround();
        this.targetSquish *= 0.6F;
    }

    @Override
    protected void disappear() {
        super.disappear();
        final float scale = Mth.sqrt(this.getRealmLevel());
        EntityUtil.forRange(this, LivingEntity.class, 4F, 3F, target -> {
            return !(target instanceof FireSpirit);
        }, (target, factor) -> {
            ElementManager.addElementAmount(target, Elements.FIRE, false, scale * 5 * factor);
            target.hurt(IMMDamageSources.fireElement(this), scale * factor * 3);
        });
        this.playSound(SoundEvents.GENERIC_EXPLODE);
    }

    @Override
    protected void tickDeath() {
        if (!this.level().isClientSide() && !this.isRemoved()) {
            this.disappear();
            this.level().broadcastEntityEvent(this, (byte)60);
            this.remove(Entity.RemovalReason.KILLED);
        }
    }

    @Override
    public void push(Entity entity) {
        super.push(entity);
        if(! entity.isOnFire()){
            entity.setSecondsOnFire(10);
        }
    }

    @Override
    protected void jumpFromGround() {
        Vec3 vec3 = this.getDeltaMovement();
        this.setDeltaMovement(vec3.x, this.getJumpPower(), vec3.z);
        this.hasImpulse = true;
    }

    protected int getJumpDelay() {
        return this.random.nextInt(20) + 10;
    }

    @Override
    public boolean isOnFire() {
        return true;
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("WasOnGround", this.wasOnGround);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.wasOnGround = tag.getBoolean("WasOnGround");
    }

    @Override
    public ISpiritualType getSpiritualRoot() {
        return SpiritualTypes.FIRE;
    }

    @Override
    public Elements getElement() {
        return Elements.FIRE;
    }

    protected SoundEvent getJumpSound() {
        return SoundEvents.SLIME_JUMP_SMALL;
    }

    static class SpiritJumpMoveControl extends MoveControl {
        private float yRot;
        private int jumpDelay;
        private final FireSpirit spirit;
        private boolean isAggressive;

        public SpiritJumpMoveControl(FireSpirit spirit) {
            super(spirit);
            this.spirit = spirit;
            this.yRot = 180.0F * spirit.getYRot() / (float)Math.PI;
        }

        public void setDirection(float p_33673_, boolean p_33674_) {
            this.yRot = p_33673_;
            this.isAggressive = p_33674_;
        }

        public void setWantedMovement(double p_33671_) {
            this.speedModifier = p_33671_;
            this.operation = MoveControl.Operation.MOVE_TO;
        }

        @Override
        public void tick() {
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), this.yRot, 90.0F));
            this.mob.yHeadRot = this.mob.getYRot();
            this.mob.yBodyRot = this.mob.getYRot();
            if (this.operation != MoveControl.Operation.MOVE_TO) {
                this.mob.setZza(0.0F);
            } else {
                this.operation = MoveControl.Operation.WAIT;
                if (this.mob.onGround()) {
                    this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                    if (this.jumpDelay-- <= 0) {
                        this.jumpDelay = this.spirit.getJumpDelay();
                        if (this.isAggressive) {
                            this.jumpDelay /= 3;
                        }

                        this.spirit.getJumpControl().jump();
                        this.spirit.playSound(this.spirit.getJumpSound(), this.spirit.getSoundVolume(), 1.1F);
                    } else {
                        this.spirit.xxa = 0.0F;
                        this.spirit.zza = 0.0F;
                        this.mob.setSpeed(0.0F);
                    }
                } else {
                    this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                }

            }
        }
    }

    static class SpiritAttackGoal extends Goal {

        private final FireSpirit spirit;
        private int growTiredTimer;

        public SpiritAttackGoal(FireSpirit spirit) {
            this.spirit = spirit;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity livingentity = this.spirit.getTarget();
            if (livingentity == null) {
                return false;
            } else {
                return this.spirit.canAttack(livingentity) && this.spirit.getMoveControl() instanceof SpiritJumpMoveControl;
            }
        }

        @Override
        public void start() {
            this.growTiredTimer = reducedTickDelay(300);
            super.start();
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity livingentity = this.spirit.getTarget();
            if (livingentity == null) {
                return false;
            } else if (!this.spirit.canAttack(livingentity)) {
                return false;
            } else {
                return --this.growTiredTimer > 0;
            }
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity livingentity = this.spirit.getTarget();
            if (livingentity != null) {
                this.spirit.lookAt(livingentity, 10.0F, 10.0F);
            }

            if (this.spirit.getMoveControl() instanceof SpiritJumpMoveControl moveControl) {
                moveControl.setDirection(this.spirit.getYRot(), this.spirit.isEffectiveAi());
            }

        }
    }

    static class SpiritRandomMoveGoal extends Goal {
        private final FireSpirit spirit;
        private float chosenDegrees;
        private int nextRandomizeTime;

        public SpiritRandomMoveGoal(FireSpirit spirit) {
            this.spirit = spirit;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return this.spirit.getTarget() == null && (this.spirit.onGround() || this.spirit.isInWater() || this.spirit.isInLava() || this.spirit.hasEffect(MobEffects.LEVITATION));
        }

        @Override
        public void tick() {
            if (--this.nextRandomizeTime <= 0) {
                this.nextRandomizeTime = this.adjustedTickDelay(40 + this.spirit.getRandom().nextInt(60));
                this.chosenDegrees = (float)this.spirit.getRandom().nextInt(360);
            }

            if (this.spirit.getMoveControl() instanceof SpiritJumpMoveControl moveControl) {
                moveControl.setDirection(this.spirit.getYRot(), this.spirit.isEffectiveAi());
            }
        }
    }

    static class SpiritKeepOnJumpingGoal extends Goal {
        private final FireSpirit spirit;

        public SpiritKeepOnJumpingGoal(FireSpirit spirit) {
            this.spirit = spirit;
            this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return !this.spirit.isPassenger();
        }

        @Override
        public void tick() {
            if (this.spirit.getMoveControl() instanceof SpiritJumpMoveControl moveControl) {
                moveControl.setWantedMovement(1.0D);
            }
        }
    }

}
