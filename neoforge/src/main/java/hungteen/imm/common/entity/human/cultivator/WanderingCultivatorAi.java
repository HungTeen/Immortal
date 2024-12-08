package hungteen.imm.common.entity.human.cultivator;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import hungteen.imm.common.entity.ai.IMMActivities;
import hungteen.imm.common.entity.ai.IMMMemories;
import hungteen.imm.common.entity.ai.behavior.*;
import hungteen.imm.common.entity.human.HumanLikeAi;
import hungteen.imm.util.BrainUtil;
import hungteen.imm.util.ItemUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetAwayFrom;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.schedule.Activity;

import java.util.Optional;
import java.util.Set;

/**
 * @author HungTeen
 * @program Immortal
 * @create 2022-12-06 23:08
 **/
public class WanderingCultivatorAi {

    protected static Brain<?> makeBrain(Brain<WanderingCultivator> brain) {
        initCoreBehaviors(brain, 1.0F);
        initIdleBehaviors(brain, 0.85F);
        initMeleeFightBehaviors(brain, 1.1F);
        initRangeFightBehaviors(brain, 1.05F);
        initEscapeBehaviors(brain, 1.2F);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.setActiveActivityIfPossible(Activity.IDLE);
        return brain;
    }

    /**
     * (Fighting -> Idle): if there is no enemy.
     * (Melee -> Escape): if no weapon.
     * (Range -> Escape): if there is no enough life.
     * (Range -> Melee): or else if there is no bullet.
     * (Escape -> Range): already keep distance
     */
    protected static void updateActivity(WanderingCultivator cultivator) {
        cultivator.getBrain().getActiveNonCoreActivity().ifPresent(activity -> {
            Optional<LivingEntity> opt = BrainUtil.getTarget(cultivator);
            if (activity.equals(IMMActivities.MELEE_FIGHT.get())) {
                if (opt.isEmpty()) {
                    cultivator.getBrain().setActiveActivityIfPossible(Activity.IDLE);
                } else if (BrainUtil.healthBelow(cultivator, 0.3D) || cultivator.getBrain().hasMemoryValue(IMMMemories.UNABLE_MELEE_ATTACK.get())) {
                    cultivator.getBrain().setActiveActivityIfPossible(IMMActivities.ESCAPE.get());
                } else if (cultivator.distanceToSqr(opt.get()) > 300 && cultivator.getRandom().nextFloat() < 0.05F) {
                    cultivator.getBrain().setActiveActivityIfPossible(IMMActivities.RANGE_FIGHT.get());
                }
            } else if (activity.equals(IMMActivities.RANGE_FIGHT.get())) {
                if (opt.isEmpty()) {
                    cultivator.getBrain().setActiveActivityIfPossible(Activity.IDLE);
                } else if (BrainUtil.healthBelow(cultivator, 0.25D)) {
                    cultivator.getBrain().setActiveActivityIfPossible(IMMActivities.ESCAPE.get());
                } else if ((cultivator.distanceToSqr(opt.get()) < 10 && cultivator.getRandom().nextFloat() < 0.05F) || cultivator.getBrain().hasMemoryValue(IMMMemories.UNABLE_RANGE_ATTACK.get())) {
                    cultivator.getBrain().setActiveActivityIfPossible(IMMActivities.MELEE_FIGHT.get());
                }
            } else if (activity.equals(IMMActivities.ESCAPE.get())) {
                if (opt.isEmpty()) {
                    cultivator.getBrain().setActiveActivityIfPossible(Activity.IDLE);
                } else if (cultivator.distanceToSqr(opt.get()) > 300 && !BrainUtil.healthBelow(cultivator, 0.5)) {
                    cultivator.getBrain().setActiveActivityIfPossible(IMMActivities.RANGE_FIGHT.get());
                }
            } else {
                // 找到目标就切换为攻击状态
                if (opt.isPresent()) {
                    cultivator.getBrain().setActiveActivityIfPossible(cultivator.getRandom().nextFloat() < 0.4F ? IMMActivities.MELEE_FIGHT.get() : IMMActivities.RANGE_FIGHT.get());
                }
            }
        });
        // Refresh inventory check.
        if (cultivator.tickCount % 40 == 5) {
            if (cultivator.getBrain().hasMemoryValue(IMMMemories.UNABLE_MELEE_ATTACK.get()) && cultivator.hasItemStack(ItemUtil::isMeleeWeapon)) {
                cultivator.getBrain().eraseMemory(IMMMemories.UNABLE_MELEE_ATTACK.get());
            }
        }
    }

    /**
     * The Core Behaviors that will always run. <br>
     */
    public static void initCoreBehaviors(Brain<WanderingCultivator> brain, float speed) {
        brain.addActivity(Activity.CORE, 0, HumanLikeAi.createCoreBehaviors(speed));
    }

    /**
     * The Idle Behaviors that triggered when nothing to focus.
     */
    public static void initIdleBehaviors(Brain<WanderingCultivator> brain, float speed) {
        brain.addActivity(Activity.IDLE, 10, ImmutableList.of(
                //寻找目标。
//                new StartFighting<>(EmptyCultivatorAi::findNearestValidAttackTarget),
                HumanLikeAi.createIdleMovementBehaviors(speed),
                HumanLikeAi.createIdleLookBehaviors(),
                new UseShield(20, 30)
        ));
    }

    /**
     * The Melee Fight Behaviors that triggered when there exist enemy. <br>
     */
    public static void initMeleeFightBehaviors(Brain<WanderingCultivator> brain, float speed) {
        brain.addActivityWithConditions(IMMActivities.MELEE_FIGHT.get(), ImmutableList.of(
                Pair.of(0, StopAttackingIfTargetInvalid.create()),
                Pair.of(1, new SwitchMeleeAttackItem(0.05F)),
                Pair.of(1, UseSpell.create(UniformInt.of(60, 120))),
//                Pair.of(1, new MeleeKeepDistance(speed)),
                // 攻击范围内清除路径，范围外则搜索路径
                Pair.of(2, SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(speed)),
                // 对皮脆的敌人，直接冲过去近战
                Pair.of(2, new EnderPearlReach(0.2F, 100, HumanLikeAi::lowLevelLiving)),
                Pair.of(3, new HumanMeleeAttack(40)),
                Pair.of(4, new UseShield(15, 30))
        ), Set.of(Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT)));
    }

    /**
     * The Range Fight Behaviors that triggered when there exist enemy. <br>
     */
    public static void initRangeFightBehaviors(Brain<WanderingCultivator> brain, float speed) {
        brain.addActivityWithConditions(IMMActivities.RANGE_FIGHT.get(), ImmutableList.of(
                Pair.of(0, StopAttackingIfTargetInvalid.create()),
//                Pair.of(1, new BackUpIfTooClose<>(64, speed)),
                Pair.of(1, new SwitchRangeAttackItem(0.08F)),
                // 攻击范围内清除路径，范围外则搜索路径
                Pair.of(2, SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(speed)),
                // 用远程攻击
                Pair.of(3, new MobRangeAttack<>(5, 10, 20F)),
                Pair.of(4, new UseShield(20, 30))
        ), Set.of(
                Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT),
                Pair.of(IMMMemories.UNABLE_RANGE_ATTACK.get(), MemoryStatus.VALUE_ABSENT)
        ));
    }

    /**
     * The Escape Behaviors that triggered when there exist enemy. <br>
     */
    public static void initEscapeBehaviors(Brain<WanderingCultivator> brain, float speed) {
        brain.addActivityWithConditions(IMMActivities.ESCAPE.get(), ImmutableList.of(
                Pair.of(0, StopAttackingIfTargetInvalid.create()),
                Pair.of(1, UseSpell.create(UniformInt.of(60, 120))),
                Pair.of(1, SetWalkTargetAwayFrom.entity(MemoryModuleType.ATTACK_TARGET, speed, 12, true)),
                Pair.of(3, new UseShield(20, 30)),
                Pair.of(4, new HumanMeleeAttack(35))
        ), Set.of(Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT)));
    }

}
