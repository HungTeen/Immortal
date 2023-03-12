package hungteen.immortal.common.entity.human.cultivator;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import hungteen.immortal.common.entity.ai.ImmortalActivities;
import hungteen.immortal.common.entity.ai.ImmortalMemories;
import hungteen.immortal.common.entity.ai.behavior.*;
import hungteen.immortal.common.tag.ImmortalEntityTags;
import hungteen.immortal.utils.BrainUtil;
import hungteen.immortal.utils.ItemUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.schedule.Activity;

import java.util.Optional;
import java.util.Set;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-12-06 23:08
 **/
public class EmptyCultivatorAi {

    protected static Brain<?> makeBrain(Brain<EmptyCultivator> brain) {
        initCoreBehaviors(brain, 1.0F);
        initIdleBehaviors(brain, 0.85F);
        initMeleeFightBehaviors(brain, 1.1F);
        initRangeFightBehaviors(brain, 1.05F);
        brain.addActivityWithConditions(ImmortalActivities.ESCAPE.get(), getEscapeBehaviors(1.2F), Set.of(Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT)));
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
    protected static void updateActivity(EmptyCultivator cultivator) {
        cultivator.getBrain().getActiveNonCoreActivity().ifPresent(activity -> {
            Optional<LivingEntity> opt = BrainUtil.getTarget(cultivator);
            if (activity.equals(ImmortalActivities.MELEE_FIGHT.get())) {
                if (opt.isEmpty()) {
                    cultivator.getBrain().setActiveActivityIfPossible(Activity.IDLE);
                } else if (BrainUtil.healthBelow(cultivator, 0.3D) || cultivator.getBrain().hasMemoryValue(ImmortalMemories.UNABLE_MELEE_ATTACK.get())) {
                    cultivator.getBrain().setActiveActivityIfPossible(ImmortalActivities.ESCAPE.get());
                } else if(cultivator.distanceToSqr(opt.get()) > 300 && cultivator.getRandom().nextFloat() < 0.05F){
                    cultivator.getBrain().setActiveActivityIfPossible(ImmortalActivities.RANGE_FIGHT.get());
                }
            } else if (activity.equals(ImmortalActivities.RANGE_FIGHT.get())) {
                if (opt.isEmpty()) {
                    cultivator.getBrain().setActiveActivityIfPossible(Activity.IDLE);
                } else if (BrainUtil.healthBelow(cultivator, 0.25D)) {
                    cultivator.getBrain().setActiveActivityIfPossible(ImmortalActivities.ESCAPE.get());
                } else if ((cultivator.distanceToSqr(opt.get()) < 10 && cultivator.getRandom().nextFloat() < 0.05F) || cultivator.getBrain().hasMemoryValue(ImmortalMemories.UNABLE_RANGE_ATTACK.get())) {
                    cultivator.getBrain().setActiveActivityIfPossible(ImmortalActivities.MELEE_FIGHT.get());
                }
            } else if (activity.equals(ImmortalActivities.ESCAPE.get())) {
                if (opt.isEmpty()) {
                    cultivator.getBrain().setActiveActivityIfPossible(Activity.IDLE);
                } else if(cultivator.distanceToSqr(opt.get()) > 300 && ! BrainUtil.healthBelow(cultivator, 0.5)){
                    cultivator.getBrain().setActiveActivityIfPossible(ImmortalActivities.RANGE_FIGHT.get());
                }
            } else {
                // 找到目标就切换为攻击状态
                if (opt.isPresent()) {
                    cultivator.getBrain().setActiveActivityIfPossible(cultivator.getRandom().nextFloat() < 0.4F ? ImmortalActivities.MELEE_FIGHT.get() : ImmortalActivities.RANGE_FIGHT.get());
                }
            }
        });
        // Refresh inventory check.
        if(cultivator.tickCount % 40 == 5){
            if(cultivator.getBrain().hasMemoryValue(ImmortalMemories.UNABLE_MELEE_ATTACK.get()) && cultivator.hasItemStack(ItemUtil::isMeleeWeapon)){
                cultivator.getBrain().eraseMemory(ImmortalMemories.UNABLE_MELEE_ATTACK.get());
            }
        }
    }

    /**
     * The Core Behaviors that will always run. <br>
     */
    public static void initCoreBehaviors(Brain<EmptyCultivator> brain, float speed) {
        brain.addActivity(Activity.CORE, 0, ImmutableList.of(
                new BreakBoat(),
                new Swim(0.8F),
                new LookAtTargetSink(45, 90),
                new MoveToTargetSink(),
                new InteractWithDoor(),
                new GoToWantedItem<>(speed, false, 4)
//                new StopHoldingItemIfNoLongerAdmiring<>(),
//                new StartAdmiringItemIfSeen<>(120)
                //new StartCelebratingIfTargetDead(300, PiglinAi::wantsToDance),
                //new StopBeingAngryIfTargetDead<>()
        ));
    }

    /**
     * The Idle Behaviors that triggered when nothing to focus.
     */
    public static void initIdleBehaviors(Brain<EmptyCultivator> brain, float speed) {
        brain.addActivity(Activity.IDLE, 10, ImmutableList.of(
                //寻找目标
                new StartFighting<>(EmptyCultivatorAi::findNearestValidAttackTarget),
                //吃东西
                new EatFood(),
                //四处逛逛
                new RunOne<>(ImmutableList.of(
                        Pair.of(new RandomStroll(speed), 1),
                        Pair.of(new SetWalkTargetFromLookTarget(speed, 3), 1),
                        Pair.of(InteractWith.of(EntityType.CAT, 8, MemoryModuleType.INTERACTION_TARGET, speed, 3), 1),
                        Pair.of(InteractWith.of(EntityType.WOLF, 8, MemoryModuleType.INTERACTION_TARGET, speed, 2), 1),
                        Pair.of(new DoNothing(30, 60), 1)
                )),
                //瞅你咋地
                new RunOne<>(ImmutableList.of(
                        Pair.of(new SetEntityLookTarget(EntityType.PLAYER, 12.0F), 1),
                        Pair.of(new SetEntityLookTarget(ImmortalEntityTags.HUMAN_BEINGS, 8.0F), 1),
                        Pair.of(new SetEntityLookTarget(8.0F), 1),
                        Pair.of(new DoNothing(30, 60), 1)
                )),
                new UseShield(20, 30)
        ));
    }

    /**
     * The Melee Fight Behaviors that triggered when there exist enemy. <br>
     */
    public static void initMeleeFightBehaviors(Brain<EmptyCultivator> brain, float speed) {
        brain.addActivityWithConditions(ImmortalActivities.MELEE_FIGHT.get(), ImmutableList.of(
                Pair.of(0, new StopAttackingIfTargetInvalid<>()),
                Pair.of(1, new SwitchMeleeAttackItem(0.05F)),
                Pair.of(1, new WearArmor()),
//                Pair.of(1, new MeleeKeepDistance(speed)),
                // 攻击范围内清除路径，范围外则搜索路径
                Pair.of(2, new SetWalkTargetFromAttackTargetIfTargetOutOfReach(speed)),
                // 对皮脆的敌人，直接冲过去近战
                Pair.of(2, new EnderPearlReach(0.2F, 100, EmptyCultivatorAi::lowLevelLiving)),
                Pair.of(3, new HumanMeleeAttack(40)),
                Pair.of(4, new UseShield(15, 30))
        ), Set.of(Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT)));
    }

    /**
     * The Range Fight Behaviors that triggered when there exist enemy. <br>
     */
    public static void initRangeFightBehaviors(Brain<EmptyCultivator> brain, float speed) {
        brain.addActivityWithConditions(ImmortalActivities.RANGE_FIGHT.get(), ImmutableList.of(
                Pair.of(0, new StopAttackingIfTargetInvalid<>()),
//                Pair.of(1, new BackUpIfTooClose<>(64, speed)),
                Pair.of(1, new SwitchRangeAttackItem(0.08F)),
                Pair.of(1, new WearArmor()),
                // 攻击范围内清除路径，范围外则搜索路径
                Pair.of(2, new SetWalkTargetFromAttackTargetIfTargetOutOfReach(speed)),
                // 用远程攻击
                Pair.of(3, new MobRangeAttack<>(5, 10, 20F)),
                Pair.of(4, new UseShield(20, 30))
        ), Set.of(
                Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT),
                Pair.of(ImmortalMemories.UNABLE_RANGE_ATTACK.get(), MemoryStatus.VALUE_ABSENT)
        ));
    }

    /**
     * The Escape Behaviors that triggered when there exist enemy. <br>
     */
    public static ImmutableList<Pair<Integer, ? extends Behavior<? super EmptyCultivator>>> getEscapeBehaviors(float speed) {
        return ImmutableList.of(
                Pair.of(0, new StopAttackingIfTargetInvalid<>()),
                Pair.of(1, SetWalkTargetAwayFrom.entity(MemoryModuleType.ATTACK_TARGET, speed, 12, true)),
                Pair.of(1, new WearArmor()),
                Pair.of(2, new EatFood()),
                Pair.of(3, new UseShield(20, 30)),
                Pair.of(4, new HumanMeleeAttack(35))
        );
    }

    protected static void wasHurtBy(EmptyCultivator cultivator, LivingEntity livingEntity) {
        Brain<EmptyCultivator> brain = cultivator.getBrain();
        if (cultivator.isBaby()) {
//            brain.setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, livingEntity, 100L);
//            if (Sensor.isEntityAttackableIgnoringLineOfSight(cultivator, livingEntity)) {
//                broadcastAngerTarget(cultivator, livingEntity);
//            }
        } else {
            if (!BehaviorUtils.isOtherTargetMuchFurtherAwayThanCurrentAttackTarget(cultivator, livingEntity, 4.0D)) {
                if (Sensor.isEntityAttackable(cultivator, livingEntity)) {
                    setAttackTarget(cultivator, livingEntity);
                }
            }
        }
    }

    private static void setAttackTarget(EmptyCultivator cultivator, LivingEntity livingEntity) {
        Brain<EmptyCultivator> brain = cultivator.getBrain();
        brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        brain.eraseMemory(MemoryModuleType.BREED_TARGET);
        brain.setMemory(MemoryModuleType.ATTACK_TARGET, livingEntity);
    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(EmptyCultivator cultivator) {
        return cultivator.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).map(l -> {
            return l.findClosest(Monster.class::isInstance).orElse(null);
        });
    }

    public static boolean lowLevelLiving(LivingEntity livingEntity){
        return ((! livingEntity.getAttributes().hasAttribute(Attributes.ARMOR) || livingEntity.getArmorValue() < 8) && livingEntity.getHealth() < 20) || livingEntity.getHealth() < 5;
    }

    public static boolean highLevelLiving(LivingEntity livingEntity){
        return (livingEntity.getAttributes().hasAttribute(Attributes.ARMOR) && livingEntity.getArmorValue() > 16) || livingEntity.getHealth() > 30;
    }

}
