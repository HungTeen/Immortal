package hungteen.imm.common.entity.creature.spirit;

import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.cultivation.QiRootType;
import hungteen.imm.common.ElementManager;
import hungteen.imm.common.entity.IMMMob;
import hungteen.imm.common.impl.registry.QiRootTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.level.Level;

/**
 * @program Immortal
 * @author PangTeen
 * @create 25.10.2023 22:39
 **/
public class MetalSpirit extends ElementSpirit{

    public final AnimationState idlingAnimationState = new AnimationState();
    public final AnimationState preAttackAnimationState = new AnimationState();
    public final AnimationState agressiveAnimationState = new AnimationState();
    public final AnimationState preIdleAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();

    public MetalSpirit(EntityType<? extends IMMMob> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ElementSpirit.createAttributes()
                .add(Attributes.MAX_HEALTH, 28.0D)
                .add(Attributes.ARMOR, 6)
                .add(Attributes.ARMOR_TOUGHNESS, 2)
                .add(Attributes.ATTACK_DAMAGE, 8)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                ;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, WoodSpirit.class, true));
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SpiritMeleeAttackGoal(this, 60, 1.2F, true));
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        super.onSyncedDataUpdated(accessor);
        if(accessor.equals(CURRENT_ANIMATION)){
            this.idlingAnimationState.stop();
            this.preAttackAnimationState.stop();
            this.preIdleAnimationState.stop();
            this.attackAnimationState.stop();
            switch (this.getCurrentAnimation()){
                case IDLING -> {
                    this.idlingAnimationState.startIfStopped(tickCount);
                }
                case IDLE_TO_ATTACK -> {
                    this.preAttackAnimationState.startIfStopped(tickCount);
                }
                case ATTACK_TO_IDLE -> {
                    this.preIdleAnimationState.startIfStopped(tickCount);
                }
                case SWING -> {
                    this.attackAnimationState.startIfStopped(tickCount);
                }
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if(EntityHelper.isServer(this)){
            if(finishPreIdle()){
                this.setIdle();
            }
        } else {
            this.agressiveAnimationState.animateWhen(this.finishPreAggressive(), tickCount);
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);
        switch (id) {
            case MELEE_ATTACK_ID -> {
                this.attackAnimationState.start(tickCount);
            }
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        ElementManager.addElementAmount(target, Element.METAL, false, 3, 5);
        return super.doHurtTarget(target);
    }

    protected boolean finishPreAggressive(){
        return this.getCurrentAnimation() == AnimationTypes.IDLE_TO_ATTACK && this.afterAnimationTick(40);
    }

    protected boolean finishPreIdle(){
        return this.getCurrentAnimation() == AnimationTypes.ATTACK_TO_IDLE && this.afterAnimationTick(60);
    }

    @Override
    public QiRootType getSpiritualRoot() {
        return QiRootTypes.METAL;
    }

    @Override
    public Element getElement() {
        return Element.METAL;
    }

    static class SpiritMeleeAttackGoal extends MeleeAttackGoal {

        private final MetalSpirit spirit;
        private final int attackCD;
        private int animatedTick = 0;

        public SpiritMeleeAttackGoal(MetalSpirit spirit, int attackCD, double speedModifier, boolean mustSeen) {
            super(spirit, speedModifier, mustSeen);
            this.spirit = spirit;
            this.attackCD = attackCD;
        }

        @Override
        public boolean canUse() {
            return this.spirit.getCurrentAnimation() != AnimationTypes.ATTACK_TO_IDLE && super.canUse();
        }

        @Override
        public void start() {
            super.start();
            this.spirit.setCurrentAnimation(AnimationTypes.IDLE_TO_ATTACK);
        }

        @Override
        public boolean canContinueToUse() {
            return this.animatedTick > 0 || super.canContinueToUse();
        }

        @Override
        public void tick() {
            super.tick();
            if(this.animatedTick > 0) {
                -- this.animatedTick;
            }
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity target) {
//            double d0 = this.getAttackReachSqr(target);
//            double distance = this.mob.distanceToSqr(target);
//            // 距离合适 并且 准备动画完成才可以开始攻击动画。
//            if (this.mob.isWithinMeleeAttackRange(target) && (this.spirit.finishPreAggressive() || this.spirit.getCurrentAnimation() == AnimationTypes.SWING)){
//                if(this.animatedTick <= 0){
//                    this.animatedTick = this.attackCD;
//                    if(! this.spirit.finishPreAggressive()){
//                        this.spirit.serverChange(MELEE_ATTACK_ID);
//                    }
//                    this.spirit.setCurrentAnimation(AnimationTypes.SWING);
//                    this.mob.swing(InteractionHand.MAIN_HAND);
//                }
//            }
//            // 攻击帧。
//            if(distance <= d0 + 10 && this.animatedTick == this.attackCD - 12){
//                this.mob.doHurtTarget(target);
//            }
        }

        @Override
        public void stop() {
            super.stop();
            this.spirit.setCurrentAnimation(AnimationTypes.ATTACK_TO_IDLE);
        }

    }
}
