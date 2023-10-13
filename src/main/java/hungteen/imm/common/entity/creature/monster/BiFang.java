package hungteen.imm.common.entity.creature.monster;

import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.htlib.util.helper.registry.ParticleHelper;
import hungteen.imm.api.enums.Elements;
import hungteen.imm.api.records.Spell;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.api.registry.ISpiritualType;
import hungteen.imm.common.ElementManager;
import hungteen.imm.common.effect.IMMEffects;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.entity.IMMGrowableMob;
import hungteen.imm.common.entity.ai.goal.LookAtTargetGoal;
import hungteen.imm.common.entity.ai.goal.UseSpellGoal;
import hungteen.imm.common.impl.registry.RealmTypes;
import hungteen.imm.common.impl.registry.SpiritualTypes;
import hungteen.imm.common.spell.SpellTypes;
import hungteen.imm.common.spell.spells.fire.IgnitionSpell;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

/**
 * 毕方鸟。
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/10/12 18:35
 **/
public class BiFang extends IMMGrowableMob implements Enemy {

    public final AnimationState roarAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();
    public final AnimationState shootAnimationState = new AnimationState();
    public final AnimationState flameAnimationState = new AnimationState();
    public final AnimationState flapAnimationState = new AnimationState();
    public float flap;
    public float flapSpeed;
    public float oFlapSpeed;
    public float oFlap;
    public float flapping = 1.0F;
    private float nextFlap = 1.0F;

    public BiFang(EntityType<? extends IMMGrowableMob> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 30, false);
        this.getNavigation().setCanFloat(true);
        this.setPathfindingMalus(BlockPathTypes.LAVA, 8.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.FLYING_SPEED, 0.6F)
                .add(Attributes.ATTACK_DAMAGE, 7F)
                ;
    }

    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Chicken.class, true));
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new UseSpellGoal(this));
        this.goalSelector.addGoal(1, new ShootFireballGoal(this));
        this.goalSelector.addGoal(2, new FlameAttackGoal(this));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1F, false));
        this.goalSelector.addGoal(4, new LookAtTargetGoal(this));
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, level);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
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
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> dataAccessor) {
        super.onSyncedDataUpdated(dataAccessor);
        if(dataAccessor.equals(CURRENT_ANIMATION)){
            this.resetAnimations();
            switch (this.getCurrentAnimation()){
                case ROAR -> {
                    this.roarAnimationState.startIfStopped(this.tickCount);
                }
            }
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.calculateFlapping();
    }

    @Override
    public void tick() {
        super.tick();
        if(EntityHelper.isServer(this)){
            if(this.tickCount % 50 == 0 && this.getRandom().nextFloat() < 0.25F){
                ElementManager.addElementAmount(this, Elements.WOOD, true, 20);
            }
        } else {
            switch (this.getCurrentAnimation()){
                case ROAR -> {
                    if(atAnimationTick(30)) this.removeAnimation();
                }
                case SWING -> {
                    if(inFlameRange()){
                        for(int i = 0; i < 20; ++ i){
                            final Vec3 vec = this.getLookAngle().normalize().scale(0.5F);
                            final double dx = vec.x();
                            final double dy = vec.y();
                            final double dz = vec.z();
                            level().addParticle(ParticleTypes.FLAME, getX(), getEyeY(), getZ(), dx, dy, dz);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void usedSpell(@Nullable Spell spell) {
        this.setCurrentAnimation(AnimationTypes.ROAR);
    }

    @Override
    public boolean canUseSpell(ISpellType spell) {
        return EntityHelper.isEntityValid(this.getTarget()) && this.distanceTo(this.getTarget()) < 20 && ! this.getTarget().hasEffect(IMMEffects.OPPRESSION.get());
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        this.serverChange(MELEE_ATTACK_ID);
        return super.doHurtTarget(entity);
    }

    @Override
    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);
        switch (id) {
            case MELEE_ATTACK_ID -> this.attackAnimationState.start(this.tickCount);
        }
    }

    private void calculateFlapping() {
        this.oFlap = this.flap;
        this.oFlapSpeed = this.flapSpeed;
        this.flapSpeed += (float)(!this.onGround() && !this.isPassenger() ? 4 : -1) * 0.3F;
        this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0F, 1.0F);
        if (!this.onGround() && this.flapping < 1.0F) {
            this.flapping = 1.0F;
        }

        this.flapping *= 0.9F;
        Vec3 vec3 = this.getDeltaMovement();
        if (!this.onGround() && vec3.y < 0.0D) {
            this.setDeltaMovement(vec3.multiply(1.0D, 0.6D, 1.0D));
        }

        this.flap += this.flapping * 2.0F;
    }

    @Override
    protected void onFlap() {
        this.playSound(SoundEvents.PARROT_FLY, 0.15F, 1.0F);
        this.nextFlap = this.flyDist + this.flapSpeed / 2.0F;
    }

    protected boolean inFlameRange(){
        return this.inAnimationRange(10, 30);
    }
    @Override
    public int getMaxAge() {
        return 2;
    }

    @Override
    public boolean canNaturallyGrow() {
        return false;
    }

    private void resetAnimations() {
        this.roarAnimationState.stop();
        this.attackAnimationState.stop();
        this.shootAnimationState.stop();
        this.flameAnimationState.stop();
        this.flapAnimationState.stop();
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }

    static class FlameAttackGoal extends Goal {

        private static final float CONSUME_MANA = 1F;
        private final BiFang biFang;

        FlameAttackGoal(BiFang biFang) {
            this.biFang = biFang;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return EntityHelper.isEntityValid(biFang.getTarget()) && biFang.getCurrentAnimation() == AnimationTypes.IDLING && hasMana() && biFang.getRandom().nextFloat() < 0.1F;
        }

        @Override
        public boolean canContinueToUse() {
            return hasMana();
        }

        @Override
        public void start() {
            biFang.setCurrentAnimation(AnimationTypes.SWING);
        }

        @Override
        public void tick() {
            if(biFang.inFlameRange() && hasMana()){
                final Vec3 vec = biFang.getLookAngle().normalize();
                biFang.addMana(- CONSUME_MANA);
            }
        }

        protected boolean hasMana() {
            return biFang.getMana() >= CONSUME_MANA;
        }

        @Override
        public void stop() {
            biFang.setCurrentAnimation(AnimationTypes.IDLING);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }
    }

    static class ShootFireballGoal extends Goal {

        private static final float CONSUME_MANA = 20F;
        private final BiFang biFang;

        ShootFireballGoal(BiFang biFang) {
            this.biFang = biFang;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return EntityHelper.isEntityValid(biFang.getTarget()) && biFang.getCurrentAnimation() == AnimationTypes.IDLING && hasMana();
        }

        @Override
        public boolean canContinueToUse() {
            return hasMana();
        }

        @Override
        public void start() {
            biFang.setCurrentAnimation(AnimationTypes.SHOOT);
        }

        @Override
        public void tick() {
            if(biFang.atAnimationTick(10) && hasMana()){
                biFang.addMana(- CONSUME_MANA);
                final Vec3 vec = biFang.getLookAngle();
                if(biFang.getAge() == 1){
                    SmallFireball smallfireball = new SmallFireball(biFang.level(), biFang, vec.x(), vec.y(), vec.z());
                    smallfireball.setPos(smallfireball.getX(), biFang.getY(0.5D) + 0.5D, smallfireball.getZ());
                    biFang.level().addFreshEntity(smallfireball);
                } else {
                    LargeFireball largeFireball = new LargeFireball(biFang.level(), biFang, vec.x(), vec.y(), vec.z(), biFang.getAge());
                    largeFireball.setPos(largeFireball.getX(), biFang.getY(0.5D) + 0.5D, largeFireball.getZ());
                    biFang.level().addFreshEntity(largeFireball);
                }

            }
        }

        protected boolean hasMana() {
            return biFang.getMana() >= CONSUME_MANA;
        }

        @Override
        public void stop() {
            biFang.removeAnimation();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }
    }
}
