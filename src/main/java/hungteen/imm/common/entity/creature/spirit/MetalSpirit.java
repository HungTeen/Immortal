package hungteen.imm.common.entity.creature.spirit;

import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.imm.api.enums.Elements;
import hungteen.imm.api.registry.ISpiritualType;
import hungteen.imm.common.ElementManager;
import hungteen.imm.common.entity.IMMMob;
import hungteen.imm.common.impl.registry.SpiritualTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.level.Level;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 25.10.2023 22:39
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
        ElementManager.addElementAmount(target, Elements.METAL, false, 3, 5);
        return super.doHurtTarget(target);
    }

    protected boolean finishPreAggressive(){
        return this.getCurrentAnimation() == AnimationTypes.IDLE_TO_ATTACK && this.afterAnimationTick(40);
    }

    protected boolean finishPreIdle(){
        return this.getCurrentAnimation() == AnimationTypes.ATTACK_TO_IDLE && this.afterAnimationTick(60);
    }

    @Override
    public ISpiritualType getSpiritualRoot() {
        return SpiritualTypes.METAL;
    }

    @Override
    public Elements getElement() {
        return Elements.METAL;
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
        protected void checkAndPerformAttack(LivingEntity target, double distance) {
            double d0 = this.getAttackReachSqr(target);
            // 距离合适 并且 准备动画完成才可以开始攻击动画。
            if (distance <= d0 && (this.spirit.finishPreAggressive() || this.spirit.getCurrentAnimation() == AnimationTypes.SWING)){
                if(this.animatedTick <= 0){
                    this.animatedTick = this.attackCD;
                    if(! this.spirit.finishPreAggressive()){
                        this.spirit.serverChange(MELEE_ATTACK_ID);
                    }
                    this.spirit.setCurrentAnimation(AnimationTypes.SWING);
                    this.mob.swing(InteractionHand.MAIN_HAND);
                }
            }
            // 攻击帧。
            if(distance <= d0 + 10 && this.animatedTick == this.attackCD - 12){
                this.mob.doHurtTarget(target);
            }
        }

        @Override
        public void stop() {
            super.stop();
            this.spirit.setCurrentAnimation(AnimationTypes.ATTACK_TO_IDLE);
        }

        @Override
        protected double getAttackReachSqr(LivingEntity target) {
            return (this.mob.getBbWidth() + 1) * 2.0F * (this.mob.getBbWidth() + 1) * 2.0F + target.getBbWidth();
        }
    }
}
