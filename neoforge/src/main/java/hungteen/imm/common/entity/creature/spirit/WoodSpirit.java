package hungteen.imm.common.entity.creature.spirit;

import hungteen.imm.api.enums.Elements;
import hungteen.imm.api.registry.ISpiritualType;
import hungteen.imm.common.entity.IMMMob;
import hungteen.imm.common.entity.golem.SnowGolem;
import hungteen.imm.common.entity.misc.PoisonWind;
import hungteen.imm.common.impl.registry.SpiritualTypes;
import hungteen.imm.util.EntityUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/10/27 12:48
 **/
public class WoodSpirit extends ElementSpirit implements RangedAttackMob {

    private float allowedHeightOffset = 0.5F;
    private int nextHeightOffsetChangeTick;

    public WoodSpirit(EntityType<? extends IMMMob> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 30, false);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, EarthSpirit.class, true));
        this.goalSelector.addGoal(1, new RangedAttackGoal(this, 1.1F, 40, 120, 10));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ElementSpirit.createAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.FLYING_SPEED, 0.5D)
                ;
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new FlyingPathNavigation(this, level);
    }

    @Override
    public void aiStep() {
        if (!this.onGround() && this.getDeltaMovement().y < 0.0D) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
        }
        super.aiStep();
    }

    @Override
    protected void customServerAiStep() {
        --this.nextHeightOffsetChangeTick;
        if (this.nextHeightOffsetChangeTick <= 0) {
            this.nextHeightOffsetChangeTick = 100;
            this.allowedHeightOffset = (float)this.random.triangle(0.5D, 6.891D);
        }

        LivingEntity livingentity = this.getTarget();
        if (livingentity != null && livingentity.getEyeY() > this.getEyeY() + (double)this.allowedHeightOffset && this.canAttack(livingentity)) {
            Vec3 vec3 = this.getDeltaMovement();
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, ((double)0.3F - vec3.y) * (double)0.3F, 0.0D));
            this.hasImpulse = true;
        }

        super.customServerAiStep();
    }

    @Override
    public void performRangedAttack(LivingEntity target, float power) {
        PoisonWind wind = new PoisonWind(this.level(), this);
        EntityUtil.shootProjectile(wind, target.getEyePosition().subtract(wind.getEyePosition()), 1F, 2F);
        this.playSound(SoundEvents.EVOKER_CAST_SPELL);
        this.level().addFreshEntity(wind);
    }

    @Override
    public boolean canBeAffected(MobEffectInstance instance) {
        return super.canBeAffected(instance) && ! instance.getEffect().equals(MobEffects.POISON);
    }

    @Override
    public ISpiritualType getSpiritualRoot() {
        return SpiritualTypes.WOOD;
    }

    @Override
    public Elements getElement() {
        return Elements.WOOD;
    }

}
