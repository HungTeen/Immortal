package hungteen.immortal.common.entity.human.cultivator;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import hungteen.immortal.common.entity.ai.ImmortalActivities;
import hungteen.immortal.common.entity.ai.behavior.*;
import hungteen.immortal.utils.BrainUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.schedule.Activity;

import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-12-06 23:08
 **/
public class SpiritualCultivatorAi {

    protected static Brain<?> makeBrain(Brain<SpiritualCultivator> brain) {
        brain.addActivity(Activity.CORE, getCoreBehaviors(1F));
        brain.addActivity(Activity.IDLE, getIdleBehaviors(0.9F));
        brain.addActivity(ImmortalActivities.MELEE_FIGHT.get(), getMeleeFightBehaviors(1.2F));
        brain.addActivity(ImmortalActivities.RANGE_FIGHT.get(), getRangeFightBehaviors(1.1F));
        brain.addActivity(ImmortalActivities.KEEP_DISTANCE.get(), getKeepDistanceBehaviors(1.2F));
        brain.addActivity(ImmortalActivities.ESCAPE.get(), getEscapeBehaviors(1.25F));
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.setActiveActivityIfPossible(Activity.IDLE);
        return brain;
    }

    /**
     * (Fighting -> Idle): if there is no enemy.
     */
    protected static void updateActivity(SpiritualCultivator cultivator) {
        cultivator.getBrain().getActiveNonCoreActivity().ifPresent(activity -> {
            if(activity.equals(ImmortalActivities.MELEE_FIGHT.get())){
                if(BrainUtil.noTarget(cultivator)){
                    cultivator.getBrain().setActiveActivityIfPossible(Activity.IDLE);
                } else if(BrainUtil.healthBelow(cultivator, 0.3D)){
                    cultivator.getBrain().setActiveActivityIfPossible(ImmortalActivities.KEEP_DISTANCE.get());
                }
            }
        });
        if(cultivator.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isEmpty()){
            if(cultivator.getBrain().getActiveNonCoreActivity().isPresent() && (
                    cultivator.getBrain().getActiveNonCoreActivity().get().equals(ImmortalActivities.MELEE_FIGHT.get()) ||
                            cultivator.getBrain().getActiveNonCoreActivity().get().equals(ImmortalActivities.RANGE_FIGHT.get())
            )){
                cultivator.getBrain().setActiveActivityIfPossible(Activity.IDLE);
            }
        }
    }

    /**
     * The Core Behaviors that will always run. <br>
     * Swim <br>
     * InteractWithDoor(PATH, DOORS_TO_CLOSE) : Open doors in path. <br>
     * LookAtTargetSink(LOOK_TARGET) : Look at the target. <br>
     * BreakBoat(LOOK_TARGET, NEAREST_BOAT) : Break boat to avoid stuck ! <br>
     * MoveToTargetSink(CANT_REACH_WALK_TARGET_SINCE, PATH, WALK_TARGET) : Walk to target. <br>
     */
    public static ImmutableList<Pair<Integer, ? extends Behavior<? super SpiritualCultivator>>> getCoreBehaviors(float speed) {
        return ImmutableList.of(
                Pair.of(0, new Swim(0.8F)),
                Pair.of(0, new InteractWithDoor()),
                Pair.of(0, new LookAtTargetSink(45, 90)),
                Pair.of(0, new BreakBoat()),
                Pair.of(1, new MoveToTargetSink()),
//                Pair.of(3, new LookAndFollowTradingPlayerSink(p_24587_)),
                Pair.of(5, new GoToWantedItem<>(speed, false, 4))
        );
    }

    /**
     * The Idle Behaviors that triggered when nothing to focus. <br>
     * Mock : perform mock action. <br>
     * SetLookAndInteract(LOOK_TARGET, INTERACTION_TARGET, NEAREST_VISIBLE_LIVING_ENTITIES) : Look at specific entity. <br>
     */
    public static ImmutableList<Pair<Integer, ? extends Behavior<? super SpiritualCultivator>>> getIdleBehaviors(float speed) {
        return ImmutableList.of(
                Pair.of(1, new StartFighting<>(SpiritualCultivatorAi::findNearestValidAttackTarget)),
                Pair.of(2, new RunOne<>(ImmutableList.of(
                                Pair.of(new RandomStroll(speed), 2),
                                Pair.of(new SetWalkTargetFromLookTarget(speed, 3), 2),
                                Pair.of(new DoNothing(30, 60), 1)
                        ))
                ),
                Pair.of(10, new SetLookAndInteract(EntityType.PLAYER, 4))
//                Pair.of(3, new ShowTradesToPlayer(400, 1600)),

        );
    }

    /**
     * The Melee Fight Behaviors that triggered when there exist enemy. <br>
     */
    public static ImmutableList<Pair<Integer, ? extends Behavior<? super SpiritualCultivator>>> getMeleeFightBehaviors(float speed) {
        return ImmutableList.of(
                Pair.of(0, new StopAttackingIfTargetInvalid<>()),
                Pair.of(0, new SwitchMeleeAttackItem(0.05F)),
                Pair.of(4, new EnderPearlReach(0.2F, 100)),
                Pair.of(5, new SetWalkTargetFromAttackTargetIfTargetOutOfReach(speed)),
                Pair.of(10, new HumanMeleeAttack(40))
//                Pair.of(99, new UpdateActivityFromSchedule())
        );
    }

    /**
     * The Melee Fight Behaviors that triggered when there exist enemy. <br>
     */
    public static ImmutableList<Pair<Integer, ? extends Behavior<? super SpiritualCultivator>>> getRangeFightBehaviors(float speed) {
        return ImmutableList.of(
                Pair.of(0, new StopAttackingIfTargetInvalid<>())
//                Pair.of(99, new UpdateActivityFromSchedule())
        );
    }

    /**
     * The Melee Fight Behaviors that triggered when there exist enemy. <br>
     */
    public static ImmutableList<Pair<Integer, ? extends Behavior<? super SpiritualCultivator>>> getKeepDistanceBehaviors(float speed) {
        return ImmutableList.of(
                Pair.of(0, new StopAttackingIfTargetInvalid<>())
//                Pair.of(99, new UpdateActivityFromSchedule())
        );
    }

    /**
     * The Melee Fight Behaviors that triggered when there exist enemy. <br>
     */
    public static ImmutableList<Pair<Integer, ? extends Behavior<? super SpiritualCultivator>>> getEscapeBehaviors(float speed) {
        return ImmutableList.of(
                Pair.of(0, new StopAttackingIfTargetInvalid<>())
//                Pair.of(99, new UpdateActivityFromSchedule())
        );
    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(Cultivator cultivator) {
        return cultivator.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).map(l -> {
            return l.findClosest(Monster.class::isInstance).orElse(null);
        });
    }

}