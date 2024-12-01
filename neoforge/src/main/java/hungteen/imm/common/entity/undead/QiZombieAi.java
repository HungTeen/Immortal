package hungteen.imm.common.entity.undead;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import hungteen.imm.common.entity.ai.behavior.BreakBoat;
import hungteen.imm.common.entity.ai.behavior.StartFighting;
import hungteen.imm.common.entity.ai.behavior.UseShield;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;

/**
 * @author HungTeen
 * @program Immortal
 * @create 2024-11-30 15:04
 **/
public class QiZombieAi {

    protected static Brain<?> makeBrain(QiZombie zombie, Brain<QiZombie> brain) {
        initCoreActivity(brain);
        initIdleActivity(brain);
        initFightActivity(zombie, brain);
//        initRetreatActivity(brain);
//        initRideHoglinActivity(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    public static void initCoreActivity(Brain<QiZombie> brain) {
        brain.addActivity(Activity.CORE, 0, ImmutableList.of(
                new BreakBoat(),
                new Swim(0.8F),
                new LookAtTargetSink(45, 90),
                new MoveToTargetSink(),
                InteractWithDoor.create(),
                StopBeingAngryIfTargetDead.create()
        ));
    }

    public static void initIdleActivity(Brain<QiZombie> brain) {
        brain.addActivity(Activity.IDLE, 10, ImmutableList.of(
                new StartFighting<>(UndeadEntity::findNearestValidAttackTarget),
                UndeadEntity.createIdleLookBehaviors(),
                UndeadEntity.createIdleMovementBehaviors()
        ));
    }

    public static void initFightActivity(QiZombie piglin, Brain<QiZombie> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10, ImmutableList.<BehaviorControl<? super QiZombie>>of(
                StopAttackingIfTargetInvalid.create(living -> !UndeadEntity.isNearestValidAttackTarget(piglin, living)),
                SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F),
                MeleeAttack.create(20),
                new UseShield(15, 30)
        ), MemoryModuleType.ATTACK_TARGET);
    }

    public static void initRetreatActivity(Brain<QiZombie> brain) {
//        brain.addActivityAndRemoveMemoryWhenStopped(Activity.AVOID, 10, ImmutableList.of(
//                SetWalkTargetAwayFrom.entity(MemoryModuleType.AVOID_TARGET, 1.0F, 12, true),
//                UndeadEntity.createIdleLookBehaviors(),
//                UndeadEntity.createIdleMovementBehaviors(),
//                EraseMemoryIf.<Piglin>create(PiglinAi::wantsToStopFleeing, MemoryModuleType.AVOID_TARGET)
//        ), MemoryModuleType.AVOID_TARGET);
    }

    public static void initRideHoglinActivity(Brain<QiZombie> brain) {
//        brain.addActivityAndRemoveMemoryWhenStopped(
//                Activity.RIDE,
//                10,
//                ImmutableList.of(
//                        Mount.create(0.8F),
//                        SetEntityLookTarget.create(PiglinAi::isPlayerHoldingLovedItem, 8.0F),
//                        BehaviorBuilder.sequence(
//                                BehaviorBuilder.triggerIf(Entity::isPassenger),
//                                TriggerGate.triggerOneShuffled(
//                                        ImmutableList.<Pair<? extends Trigger<? super LivingEntity>, Integer>>builder()
//                                                .addAll(UndeadEntity.createLookBehaviors())
//                                                .add(Pair.of(BehaviorBuilder.triggerIf(p_258950_ -> true), 1))
//                                                .build()
//                                )
//                        ),
//                        DismountOrSkipMounting.<Piglin>create(8, PiglinAi::wantsToStopRiding)
//                ),
//                MemoryModuleType.RIDE_TARGET
//        );
    }

}
