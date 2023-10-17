package hungteen.imm.common.entity.creature.monster;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import hungteen.htlib.util.helper.MathHelper;
import hungteen.htlib.util.helper.RandomHelper;
import hungteen.htlib.util.helper.registry.BrainHelper;
import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.imm.api.enums.Elements;
import hungteen.imm.common.ElementManager;
import hungteen.imm.common.entity.IMMMob;
import hungteen.imm.common.entity.ai.IMMActivities;
import hungteen.imm.common.entity.ai.IMMMemories;
import hungteen.imm.common.entity.ai.behavior.*;
import hungteen.imm.common.entity.human.cultivator.EmptyCultivator;
import hungteen.imm.common.entity.human.cultivator.EmptyCultivatorAi;
import hungteen.imm.common.misc.IMMSounds;
import hungteen.imm.common.tag.IMMEntityTags;
import hungteen.imm.util.BehaviorUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.animal.allay.AllayAi;
import net.minecraft.world.entity.animal.axolotl.AxolotlAi;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.hoglin.HoglinAi;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.monster.warden.WardenAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.enchantment.SweepingEdgeEnchantment;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/10/14 12:41
 **/
public class BiFangAi {

    protected static Brain<?> makeBrain(Brain<BiFang> brain) {
        initCoreActivity(brain);
        initIdleActivity(brain, 0.85F);
        initMeleeFightActivity(brain, 1F);
        initRangeFightActivity(brain, 1.2F);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    public static void updateActivity(BiFang biFang) {
        biFang.setTarget(BehaviorUtil.getAttackTarget(biFang).orElse(null));
        if (biFang.tickCount % 10 == 0) {
            BehaviorUtil.getAttackTarget(biFang).ifPresentOrElse(target -> {
                final double distance = biFang.distanceTo(target);
                final float percent = biFang.getMana() / biFang.getMaxMana();
                if (biFang.getBrain().isActive(IMMActivities.MELEE_FIGHT.get())) {
                    // 当前正在近战。
                    if (biFang.getDeltaMovement().y <= 0){
                    if (percent >= 0.7 || distance >= 20) {
                        biFang.setFlying(true); // 蓝足够多或者目标太远切换为飞行模式。
                    }
                    if (distance >= 12 && biFang.getRandom().nextFloat() < 0.2F) {
                        biFang.setFlying(true); // 目标比较远切换为飞行模式。
                    }
                    }
                } else if (biFang.getBrain().isActive(IMMActivities.RANGE_FIGHT.get())) {
                    // 当前正在远程攻击并且没有受到惊吓。
                    if (! biFang.getBrain().hasMemoryValue(MemoryModuleType.IS_PANICKING) && (distance <= 6 || percent < 0.2F)) {
                        biFang.setFlying(false); // 距离很近或者没有蓝了切换为近战模式。
                    }
                }
                biFang.getBrain().setActiveActivityIfPossible(biFang.isFlying() ? IMMActivities.RANGE_FIGHT.get() : IMMActivities.MELEE_FIGHT.get());
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
                new RunOne<>(ImmutableList.of(
                        Pair.of(RandomStroll.stroll(speed), 1),
                        Pair.of(SetWalkTargetFromLookTarget.create(speed, 3), 1),
                        Pair.of(InteractWith.of(EntityType.CHICKEN, 8, MemoryModuleType.INTERACTION_TARGET, speed, 3), 1),
                        Pair.of(InteractWith.of(EntityType.PARROT, 8, MemoryModuleType.INTERACTION_TARGET, speed, 2), 1),
                        Pair.of(new DoNothing(30, 60), 1)
                )),
                new RunOne<>(ImmutableList.of(
                        Pair.of(new IdleBehavior(IMMMob.AnimationTypes.IDLING_1, 40, UniformInt.of(100, 200)), 1),
                        Pair.of(new IdleBehavior(IMMMob.AnimationTypes.IDLING_2, 40, UniformInt.of(200, 300)), 1),
                        Pair.of(new DoNothing(30, 60), 1)
                )),
                new RunOne<>(ImmutableList.of(
                        Pair.of(SetEntityLookTargetSometimes.create(8.0F, UniformInt.of(30, 60)), 1),
                        Pair.of(new DoNothing(30, 60), 1)
                ))
        ));
    }

    /**
     * The Melee Fight Behaviors that triggered when there exist enemy. <br>
     */
    public static void initMeleeFightActivity(Brain<BiFang> brain, float speed) {
        brain.addActivity(IMMActivities.MELEE_FIGHT.get(), ImmutableList.of(
                Pair.of(0, StopAttackingIfTargetInvalid.create()),
                Pair.of(0, new MoveToTargetSink()),
                Pair.of(1, UseSpell.create(UniformInt.of(10, 100))),
                Pair.of(2, SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(speed)),
                Pair.of(2, new BlowFlameAttack()),
                Pair.of(3, MeleeAttack.create(20))
        ));
    }

    /**
     * The Range Fight Behaviors that triggered when there exist enemy. <br>
     */
    public static void initRangeFightActivity(Brain<BiFang> brain, float speed) {
        brain.addActivity(IMMActivities.RANGE_FIGHT.get(), ImmutableList.of(
                Pair.of(0, StopAttackingIfTargetInvalid.create()),
                Pair.of(0, new MoveToTargetSink()),
                Pair.of(1, UseSpell.create(UniformInt.of(60, 120))),
                Pair.of(1, BackUpIfTooClose.create(64, speed)),
                Pair.of(2, SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(speed)),
                Pair.of(3, new ShootFireballAttack()),
                Pair.of(4, new ShootFlameAttack())
        ));
    }

    protected static void wasHurtBy(BiFang biFang, LivingEntity target, float amount) {
        if (!BehaviorUtils.isOtherTargetMuchFurtherAwayThanCurrentAttackTarget(biFang, target, 4.0D)) {
            if (Sensor.isEntityAttackable(biFang, target)) {
                setAttackTarget(biFang, target);
            }
        }
        if(amount > 7){
            biFang.getBrain().setMemoryWithExpiry(MemoryModuleType.IS_PANICKING, true, 200);
        }
    }

    private static void setAttackTarget(BiFang biFang, LivingEntity target) {
        biFang.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_TARGET, target, 600L);
    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(BiFang biFang) {
        return biFang.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER);
    }

    static class BlowFlameAttack extends Behavior<BiFang> {

        private static final float CONSUME_MANA = 10F;
        private static final float EACH_CONSUME_MANA = 2F;

        public BlowFlameAttack() {
            super(Map.of(
                    MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                    MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_ABSENT
            ), 80);
        }

        @Override
        protected boolean checkExtraStartConditions(ServerLevel level, BiFang biFang) {
            return biFang.isIdle() && biFang.getMana() >= CONSUME_MANA && biFang.getRandom().nextFloat() < 0.1F;
        }

        @Override
        protected boolean canStillUse(ServerLevel level, BiFang biFang, long time) {
            return biFang.getMana() >= CONSUME_MANA;
        }

        @Override
        protected void start(ServerLevel level, BiFang biFang, long time) {
            biFang.setCurrentAnimation(IMMMob.AnimationTypes.FLAP);
            biFang.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_COOLING_DOWN, true, 30);
        }

        @Override
        protected void tick(ServerLevel level, BiFang biFang, long time) {
            BehaviorUtil.getAttackTarget(biFang).ifPresent(target -> {
                if (biFang.inFlameRange() && biFang.getMana() >= EACH_CONSUME_MANA) {
                    biFang.addMana(-EACH_CONSUME_MANA);
                    ElementManager.addElementAmount(biFang, Elements.FIRE, false, 5);
                    ElementManager.addElementAmount(biFang, Elements.WOOD, true, 5);
                    biFang.playSound(IMMSounds.BI_FANG_FLAP.get());
                }
            });
        }

        @Override
        protected void stop(ServerLevel level, BiFang biFang, long time) {
            biFang.setIdle();
        }
    }

    static class ShootFlameAttack extends Behavior<BiFang> {

        private static final float CONSUME_MANA = 5F;

        public ShootFlameAttack() {
            super(Map.of(
                    MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT
            ), 40);
        }

        @Override
        protected boolean checkExtraStartConditions(ServerLevel level, BiFang biFang) {
            return biFang.isIdle() && biFang.getMana() >= CONSUME_MANA && biFang.getRandom().nextFloat() < 0.2F;
        }

        @Override
        protected boolean canStillUse(ServerLevel level, BiFang biFang, long time) {
            return biFang.getMana() >= CONSUME_MANA;
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
            return biFang.isIdle() && biFang.getMana() >= CONSUME_MANA && biFang.getRandom().nextFloat() < 0.1F;
        }

        @Override
        protected boolean canStillUse(ServerLevel level, BiFang biFang, long time) {
            return biFang.getMana() >= CONSUME_MANA;
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
                    LargeFireball largeFireball = new LargeFireball(biFang.level(), biFang, vec.x(), vec.y(), vec.z(), biFang.getAge() + 1);
                    largeFireball.setPos(largeFireball.getX(), biFang.getEyeY(), largeFireball.getZ());
                    largeFireball.setDeltaMovement(largeFireball.getDeltaMovement().scale(2F));
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
