package hungteen.imm.common.entity.creature.monster;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import hungteen.htlib.util.helper.MathHelper;
import hungteen.htlib.util.helper.RandomHelper;
import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.imm.api.enums.Elements;
import hungteen.imm.common.ElementManager;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.entity.IMMMob;
import hungteen.imm.common.entity.ai.IMMActivities;
import hungteen.imm.common.entity.ai.IMMMemories;
import hungteen.imm.common.entity.ai.behavior.*;
import hungteen.imm.common.entity.misc.Tornado;
import hungteen.imm.common.misc.IMMSounds;
import hungteen.imm.util.BehaviorUtil;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.MathUtil;
import hungteen.imm.util.RandomUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/10/14 12:41
 **/
public class BiFangAi {

    private static final double MAX_REACH_RANGE = 3600;
    private static final double BACK_HOME_RANGE = 1600;
    private static final double CLOSE_TO_HOME = 10;

    protected static Brain<?> makeBrain(Brain<BiFang> brain) {
        initCoreActivity(brain);
        initIdleActivity(brain, 0.85F);
        initHomeActivity(brain, 1.5F);
        initMeleeFightActivity(brain, 1F);
        initRangeFightActivity(brain, 1.2F);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    public static void updateActivity(BiFang biFang) {
        final Optional<LivingEntity> targetOpt = BehaviorUtil.getAttackTarget(biFang);
        biFang.setTarget(targetOpt.orElse(null));
        if (biFang.tickCount % 10 == 0) {
            final double homeDistance = homeDistance(biFang);
            if(homeDistance >= BACK_HOME_RANGE){
                biFang.getBrain().setActiveActivityIfPossible(IMMActivities.HOME.get());
            }
            if(homeDistance >= CLOSE_TO_HOME){
                return;
            }
            targetOpt.ifPresentOrElse(target -> {
                final double distance = biFang.distanceTo(target);
                final float percent = biFang.getMana() / biFang.getMaxMana();
                if (biFang.getBrain().isActive(IMMActivities.MELEE_FIGHT.get()) && !biFang.getBrain().hasMemoryValue(IMMMemories.UNABLE_RANGE_ATTACK.get())) {
                    // 当前正在近战，可以考虑切换为远程攻击。
                    int cd = 0;
                    if (distance >= 20) {
                        cd = 150; // 目标太远。
                    } else if (percent >= 0.7 && biFang.getRandom().nextFloat() < 0.3F) {
                        cd = 60; // 蓝足够多。
                    } else if (distance >= 12 && biFang.getRandom().nextFloat() < 0.2F) {
                        cd = 100; // 目标比较远切换为飞行模式。
                    } else if (biFang.getHealth() < 10) {
                        cd = 50; // 血量不足。
                    }
                    if (cd != 0) {
                        biFang.getBrain().setMemoryWithExpiry(IMMMemories.UNABLE_MELEE_ATTACK.get(), true, UniformInt.of(cd / 2, cd).sample(biFang.getRandom()));
                        biFang.getBrain().setActiveActivityIfPossible(IMMActivities.RANGE_FIGHT.get());
                    }
                } else if (biFang.getBrain().isActive(IMMActivities.RANGE_FIGHT.get()) && !biFang.getBrain().hasMemoryValue(IMMMemories.UNABLE_MELEE_ATTACK.get())) {
                    // 当前正在远程攻击并且没有受到惊吓。
                    int cd = 0;
                    if (distance <= 5) {
                        cd = 120;
                    } else if (percent <= 0.1 && biFang.getRandom().nextFloat() < 0.3F) {
                        cd = 200;
                    } else if (percent <= 0.4 && biFang.getRandom().nextFloat() < 0.1F) {
                        cd = 160;
                    }
                    if (cd != 0) {
                        biFang.getBrain().setMemoryWithExpiry(IMMMemories.UNABLE_RANGE_ATTACK.get(), true, UniformInt.of(cd / 2, cd).sample(biFang.getRandom()));
                        biFang.getBrain().setActiveActivityIfPossible(IMMActivities.MELEE_FIGHT.get());
                    }
                }
                if (!biFang.getBrain().isActive(IMMActivities.MELEE_FIGHT.get()) && !biFang.getBrain().isActive(IMMActivities.RANGE_FIGHT.get())) {
                    biFang.getBrain().setActiveActivityIfPossible(IMMActivities.MELEE_FIGHT.get());
                }
            }, () -> {
                biFang.getBrain().setActiveActivityIfPossible(Activity.IDLE);
            });
        }
    }

    private static void initCoreActivity(Brain<BiFang> brain) {
        brain.addActivity(Activity.CORE, 0, ImmutableList.of(
                new Swim(0.7F),
                new LookAtTargetSink(45, 90)
        ));
    }

    private static void initIdleActivity(Brain<BiFang> brain, float speed) {
        brain.addActivity(Activity.IDLE, 10, ImmutableList.of(
                StartAttacking.create(BiFangAi::findNearestValidAttackTarget),
                new MoveToTargetSink(),
                UseSpell.create(UniformInt.of(60, 120)),
                new RunOne<>(ImmutableList.of(
                        Pair.of(RandomStroll.stroll(speed), 1),
                        Pair.of(SetWalkTargetFromLookTarget.create(speed, 3), 1),
                        Pair.of(InteractWith.of(EntityType.CHICKEN, 8, MemoryModuleType.INTERACTION_TARGET, speed, 3), 1),
                        Pair.of(InteractWith.of(EntityType.PARROT, 8, MemoryModuleType.INTERACTION_TARGET, speed, 2), 1),
                        Pair.of(new IdleBehavior(IMMMob.AnimationTypes.IDLING_1, 40, UniformInt.of(100, 200)), 1),
                        Pair.of(new IdleBehavior(IMMMob.AnimationTypes.IDLING_2, 40, UniformInt.of(200, 300)), 1),
                        Pair.of(SetEntityLookTargetSometimes.create(8.0F, UniformInt.of(30, 60)), 1),
                        Pair.of(new DoNothing(30, 60), 1)
                ))
        ));
    }

    private static void initHomeActivity(Brain<BiFang> brain, float speed) {
        brain.addActivity(IMMActivities.HOME.get(), 10, ImmutableList.of(
                setHome(speed, 3),
                new MoveToTargetSink(),
                UseSpell.create(UniformInt.of(40, 80))
        ));
    }

    /**
     * The Melee Fight Behaviors that triggered when there exist enemy. <br>
     */
    public static void initMeleeFightActivity(Brain<BiFang> brain, float speed) {
        brain.addActivity(IMMActivities.MELEE_FIGHT.get(), ImmutableList.of(
                Pair.of(0, StopAttackingIfTargetInvalid.create()),
                Pair.of(0, new MoveToTargetSink()),
                Pair.of(2, SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(speed)),
                Pair.of(3, new RunOne<>(ImmutableList.of(
                        Pair.of(UseSpell.create(UniformInt.of(10, 100)), 0),
                        Pair.of(new FlapAttack(), 1),
                        Pair.of(MeleeAttack.create(20), 2),
                        Pair.of(burnReaction(20, UniformInt.of(30, 50)), 4),
                        Pair.of(new BlowWindAttack(0.5F), 4),
                        Pair.of(new ShootFlameAttack(0.05F), 5)
                )))
        ));
    }

    /**
     * The Range Fight Behaviors that triggered when there exist enemy. <br>
     */
    public static void initRangeFightActivity(Brain<BiFang> brain, float speed) {
        brain.addActivity(IMMActivities.RANGE_FIGHT.get(), ImmutableList.of(
                Pair.of(0, StopAttackingIfTargetInvalid.create()),
                Pair.of(1, UseSpell.create(UniformInt.of(60, 120))),
                Pair.of(2, BackUpIfTooClose.create(20, speed)),
                Pair.of(3, new RunOne<>(ImmutableList.of(
                        Pair.of(new ShootFireballAttack(), 0),
                        Pair.of(new ShootFlameAttack(0.2F), 1),
                        Pair.of(burnReaction(20, UniformInt.of(20, 30)), 2),
                        Pair.of(new BlowWindAttack(0.15F), 4)
                )))
        ));
    }

    protected static void wasHurtBy(BiFang biFang, LivingEntity target, float amount) {
        if (!BehaviorUtils.isOtherTargetMuchFurtherAwayThanCurrentAttackTarget(biFang, target, 4.0D)) {
            if (Sensor.isEntityAttackable(biFang, target)) {
                setAttackTarget(biFang, target);
            }
        }
    }

    private static void setAttackTarget(BiFang biFang, LivingEntity target) {
        biFang.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, target);
    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(BiFang biFang) {
        return biFang.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER);
    }

    private static double homeDistance(BiFang biFang) {
        if(biFang.getBrain().getMemory(IMMMemories.HOME.get()).isEmpty()){
            biFang.getBrain().setMemory(IMMMemories.HOME.get(), biFang.blockPosition());
        }
        final double distance = biFang.distanceToSqr(MathHelper.toVec3(biFang.getBrain().getMemory(IMMMemories.HOME.get()).get()));
        if(distance >= MAX_REACH_RANGE){
            biFang.getBrain().setMemory(IMMMemories.HOME.get(), biFang.blockPosition());
            return 0;
        }
        return distance;
    }

    public static OneShot<Mob> keepDistance(int distance, float speed) {
        return BehaviorBuilder.create(instance -> instance.group(
                instance.absent(MemoryModuleType.WALK_TARGET),
                instance.registered(MemoryModuleType.LOOK_TARGET),
                instance.present(MemoryModuleType.ATTACK_TARGET),
                instance.present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)
        ).apply(instance, (walkTarget, lookTarget, attackTarget, livings) -> (level, mob, time) -> {
            LivingEntity livingentity = instance.get(attackTarget);
            if (livingentity.closerThan(mob, distance) && instance.get(livings).contains(livingentity)) {
                lookTarget.set(new EntityTracker(livingentity, true));
                mob.getMoveControl().strafe(-speed, 0.0F);
                mob.setYRot(Mth.rotateIfNecessary(mob.getYRot(), mob.yHeadRot, 0.0F));
            } else {
                lookTarget.set(new EntityTracker(livingentity, true));
                mob.getMoveControl().strafe(speed, 0.0F);
            }
            return true;
        }));
    }

    public static <E extends IMMMob> BehaviorControl<E> burnReaction(int manaCost, IntProvider cdProvider) {
        return BehaviorBuilder.create(instance -> instance.group(
                instance.present(MemoryModuleType.ATTACK_TARGET),
                instance.absent(IMMMemories.SPELL_COOLING_DOWN.get())
        ).apply(instance, (target, cd) -> (level, mob, time) -> {
            if (mob.distanceTo(instance.get(target)) < 10 && mob.getMana() >= manaCost) {
                ElementManager.addElementAmount(mob, Elements.FIRE, false, 20);
                ElementManager.addElementAmount(mob, Elements.WOOD, true, 20);
                mob.addMana(-manaCost);
                mob.getBrain().setMemoryWithExpiry(IMMMemories.SPELL_COOLING_DOWN.get(), true, cdProvider.sample(mob.getRandom()));
                return true;
            }
            return false;
        }));
    }

    public static OneShot<LivingEntity> setHome(float speed, int distance) {
        return BehaviorBuilder.create(instance -> instance.group(
                instance.absent(MemoryModuleType.WALK_TARGET),
                instance.present(IMMMemories.HOME.get())
        ).apply(instance, (walkTarget, home) -> (level, living, time) -> {
            walkTarget.set(new WalkTarget(instance.get(home), speed, distance));
            return true;
        }));
    }

    static class BlowWindAttack extends Behavior<BiFang> {

        private static final float CONSUME_MANA = 30F;
        private final float chance;

        public BlowWindAttack(float chance) {
            super(Map.of(
                    MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT
            ), 50);
            this.chance = chance;
        }

        @Override
        protected boolean checkExtraStartConditions(ServerLevel level, BiFang biFang) {
            return biFang.isIdle() && biFang.getMana() >= CONSUME_MANA && biFang.getRandom().nextFloat() < chance;
        }

        @Override
        protected boolean canStillUse(ServerLevel level, BiFang biFang, long time) {
            return true;
        }

        @Override
        protected void start(ServerLevel level, BiFang biFang, long time) {
            biFang.setCurrentAnimation(IMMMob.AnimationTypes.FLAP);
        }

        @Override
        protected void tick(ServerLevel level, BiFang biFang, long time) {
            if (biFang.atAnimationTick(10) && biFang.getMana() >= CONSUME_MANA) {
                biFang.addMana(-CONSUME_MANA);
                for(int i = -1; i <= 1; ++ i){
                    Tornado tornado = IMMEntities.TORNADO.get().create(level);
                    if(tornado != null){
                        Vec3 vec = biFang.getLookAngle();
                        if(biFang.getTarget() != null){
                            vec = biFang.getTarget().position().subtract(biFang.position());
                        }
                        vec = MathUtil.rotateHorizontally(vec, i * RandomHelper.getMinMax(biFang.getRandom(), 30, 60)).normalize();
                        tornado.setPos(biFang.position().add(vec.x * 0.5, 0, vec.z * 0.5));
                        final float speed = 0.35F;
                        final int duration = 600;
                        final boolean inverse = biFang.getRandom().nextBoolean();
                        tornado.setDeltaMovement(vec.scale(speed));
                        tornado.setScale(biFang.getScale() * 2);
                        tornado.setOwner(biFang);
                        if(i == 0){
                            tornado.shootTo(null, duration, biFang.getRandom().nextBoolean(), speed);
                        } else {
                            tornado.shootTo(biFang.getTarget(), duration, (i > 0) ^ inverse, speed);
                        }
                        level.addFreshEntity(tornado);
                    }
                }
                biFang.playSound(IMMSounds.BI_FANG_FLAP.get());
            }
        }

        @Override
        protected void stop(ServerLevel level, BiFang biFang, long time) {
            biFang.setIdle();
        }
    }

    static class FlapAttack extends Behavior<BiFang> {

        public FlapAttack() {
            super(Map.of(
                    MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT
            ), 30);
        }

        @Override
        protected boolean checkExtraStartConditions(ServerLevel level, BiFang biFang) {
            return biFang.isIdle() && biFang.getTarget() != null && biFang.closerThan(biFang.getTarget(), 4);
        }

        @Override
        protected boolean canStillUse(ServerLevel level, BiFang biFang, long time) {
            return true;
        }

        @Override
        protected void start(ServerLevel level, BiFang biFang, long time) {
            biFang.setCurrentAnimation(IMMMob.AnimationTypes.FLAP);
        }

        @Override
        protected void tick(ServerLevel level, BiFang biFang, long time) {
            if (biFang.atAnimationTick(8)) {
                final AABB aabb = EntityHelper.getEntityAABB(biFang, 4, 2);
                EntityHelper.getPredicateEntities(biFang, aabb, LivingEntity.class, target -> {
                    final Vec3 vec = target.getEyePosition().subtract(biFang.getEyePosition());
                    final Vec3 lookVec = biFang.getLookAngle();
                    if(vec.length() > 0 && lookVec.length() > 0){
                        double cos = vec.dot(lookVec) / vec.length() / lookVec.length();
                        double angle = Math.acos(cos);
                        if (Math.abs(angle) < 1) {
                            biFang.doHurtTarget(target);
                            EntityUtil.knockback(target, 5 / vec.length(), -lookVec.x, -lookVec.z);
                            return true;
                        }
                    }
                    return false;
                }).forEach(target -> {
                    ElementManager.addElementAmount(target, Elements.WOOD, false, 6);
                });
                biFang.playSound(IMMSounds.BI_FANG_FLAP.get());
            }
        }

        @Override
        protected void stop(ServerLevel level, BiFang biFang, long time) {
            biFang.setIdle();
        }
    }

    static class ShootFlameAttack extends Behavior<BiFang> {

        private static final float CONSUME_MANA = 5F;
        private final float chance;

        public ShootFlameAttack(float chance) {
            super(Map.of(
                    MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT
            ), 40);
            this.chance = chance;
        }

        @Override
        protected boolean checkExtraStartConditions(ServerLevel level, BiFang biFang) {
            return biFang.isIdle() && biFang.getMana() >= CONSUME_MANA && biFang.getRandom().nextFloat() < chance;
        }

        @Override
        protected boolean canStillUse(ServerLevel level, BiFang biFang, long time) {
            return true;
        }

        @Override
        protected void start(ServerLevel level, BiFang biFang, long time) {
            biFang.setCurrentAnimation(IMMMob.AnimationTypes.SHOOT);
        }

        @Override
        protected void tick(ServerLevel level, BiFang biFang, long time) {
            BehaviorUtil.getAttackTarget(biFang).ifPresent(target -> {
                if (biFang.atAnimationTick(10) && biFang.getMana() >= CONSUME_MANA) {
                    biFang.addMana(-CONSUME_MANA);
                    final Vec3 vec = target.getEyePosition().subtract(biFang.getEyePosition());
                    final double distance = target.distanceTo(biFang);
                    final double d = Math.sqrt(Math.sqrt(distance)) * 0.5;
                    final int cnt = RandomHelper.getMinMax(biFang.getRandom(), 5, 10);
                    for (int i = 0; i < cnt; ++i) {
                        SmallFireball smallfireball = new SmallFireball(biFang.level(), biFang, biFang.getRandom().triangle(vec.x(), d * 4), biFang.getRandom().triangle(vec.y(), d), biFang.getRandom().triangle(vec.z(), d * 4));
                        smallfireball.setPos(smallfireball.getX(), biFang.getEyeY(), smallfireball.getZ());
                        biFang.level().addFreshEntity(smallfireball);
                    }
                    biFang.playSound(SoundEvents.BLAZE_SHOOT);
                }
            });

        }

        @Override
        protected void stop(ServerLevel level, BiFang biFang, long time) {
            biFang.setIdle();
        }
    }

    static class ShootFireballAttack extends Behavior<BiFang> {

        private static final float CONSUME_MANA = 25F;

        public ShootFireballAttack() {
            super(Map.of(
                    MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT
            ), 60);
        }

        @Override
        protected boolean checkExtraStartConditions(ServerLevel level, BiFang biFang) {
            return biFang.isIdle() && biFang.getMana() >= CONSUME_MANA && biFang.getRandom().nextFloat() < 0.2F;
        }

        @Override
        protected boolean canStillUse(ServerLevel level, BiFang biFang, long time) {
            return true;
        }

        @Override
        protected void start(ServerLevel level, BiFang biFang, long time) {
            biFang.setCurrentAnimation(IMMMob.AnimationTypes.SHOOT);
        }

        @Override
        protected void tick(ServerLevel level, BiFang biFang, long time) {
            BehaviorUtil.getAttackTarget(biFang).ifPresent(target -> {
                if (biFang.atAnimationTick(10) && biFang.getMana() >= CONSUME_MANA) {
                    biFang.addMana(-CONSUME_MANA);
                    final Vec3 vec = target.getEyePosition().subtract(biFang.getEyePosition());
                    LargeFireball largeFireball = new LargeFireball(biFang.level(), biFang, vec.x(), vec.y(), vec.z(), biFang.getAge() + biFang.getPhase());
                    largeFireball.setPos(largeFireball.getX(), biFang.getEyeY(), largeFireball.getZ());
                    largeFireball.setDeltaMovement(largeFireball.getDeltaMovement().scale(2F + biFang.getPhase() * 0.5F));
                    biFang.level().addFreshEntity(largeFireball);
                    biFang.playSound(SoundEvents.BLAZE_SHOOT);
                }
            });
        }

        @Override
        protected void stop(ServerLevel level, BiFang biFang, long time) {
            biFang.setIdle();
        }
    }

}
