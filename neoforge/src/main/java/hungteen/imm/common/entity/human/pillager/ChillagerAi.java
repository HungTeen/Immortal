package hungteen.imm.common.entity.human.pillager;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import hungteen.imm.common.entity.ai.IMMActivities;
import hungteen.imm.common.entity.ai.IMMMemories;
import hungteen.imm.common.entity.ai.behavior.KeepDistanceWithTarget;
import hungteen.imm.common.entity.ai.behavior.StartFighting;
import hungteen.imm.common.entity.ai.behavior.StartUsingItem;
import hungteen.imm.common.entity.human.HumanLikeAi;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.schedule.Activity;

import java.util.Set;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/8 16:53
 **/
public class ChillagerAi {

    protected static Brain<?> makeBrain(Brain<Chillager> brain) {
        initCoreBehaviors(brain, 1.0F);
        initIdleBehaviors(brain, 0.85F);
        initRangeFightBehaviors(brain, 1.05F);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.setActiveActivityIfPossible(Activity.IDLE);
        return brain;
    }

    /**
     * The Core Behaviors that will always run. <br>
     */
    public static void initCoreBehaviors(Brain<Chillager> brain, float speed) {
        brain.addActivity(Activity.CORE, 0, HumanLikeAi.createCoreBehaviors(speed));
    }

    /**
     * The Idle Behaviors that triggered when nothing to focus.
     */
    public static void initIdleBehaviors(Brain<Chillager> brain, float speed) {
        brain.addActivity(Activity.IDLE, 10, ImmutableList.of(
                new StartFighting<>(HumanLikeAi::findNearestValidAttackTarget),
                HumanLikeAi.createIdleMovementBehaviors(speed),
                HumanLikeAi.createIdleLookBehaviors()
        ));
    }

    /**
     * The Range Fight Behaviors that triggered when there exist enemy. <br>
     */
    public static void initRangeFightBehaviors(Brain<Chillager> brain, float speed) {
        brain.addActivityWithConditions(IMMActivities.RANGE_FIGHT.get(), ImmutableList.of(
                Pair.of(0, StopAttackingIfTargetInvalid.create()),
                Pair.of(1, StartUsingItem.useTalisman(UniformInt.of(60, 80))),
                Pair.of(3, new KeepDistanceWithTarget(speed, 15))
        ), Set.of(
                Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT),
                Pair.of(IMMMemories.UNABLE_RANGE_ATTACK.get(), MemoryStatus.VALUE_ABSENT)
        ));
    }

}
