package hungteen.imm.common.entity.human;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import hungteen.imm.common.entity.ai.behavior.BreakBoat;
import hungteen.imm.common.entity.ai.behavior.EatFood;
import hungteen.imm.common.entity.ai.behavior.WearArmor;
import hungteen.imm.common.tag.IMMEntityTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;

import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/7 23:06
 **/
public class HumanLikeAi {

    /**
     * 创建核心行为。
     */
    public static ImmutableList<? extends BehaviorControl<? super HumanLikeEntity>> createCoreBehaviors(float speed) {
        return ImmutableList.of(
                new BreakBoat(),
                new EatFood(),
                new Swim(0.8F),
                new LookAtTargetSink(45, 90),
                new MoveToTargetSink(),
                new WearArmor(),
                InteractWithDoor.create(),
                GoToWantedItem.create(speed, false, 4)
        );
    }

    /**
     * 四处逛逛。
     */
    public static RunOne<HumanLikeEntity> createIdleMovementBehaviors(float speed) {
        return new RunOne<>(
                ImmutableList.of(
                        Pair.of(RandomStroll.stroll(speed), 1),
                        Pair.of(SetWalkTargetFromLookTarget.create(speed, 3), 1),
                        Pair.of(InteractWith.of(EntityType.CAT, 8, MemoryModuleType.INTERACTION_TARGET, speed, 3), 1),
                        Pair.of(InteractWith.of(EntityType.WOLF, 8, MemoryModuleType.INTERACTION_TARGET, speed, 2), 1),
                        Pair.of(new DoNothing(30, 60), 1)
                )
        );
    }

    /**
     * 瞅你咋地？
     */
    public static RunOne<LivingEntity> createIdleLookBehaviors() {
        return new RunOne<>(
                ImmutableList.<Pair<? extends BehaviorControl<? super LivingEntity>, Integer>>builder()
                        .addAll(createLookBehaviors())
                        .add(Pair.of(new DoNothing(30, 60), 1))
                        .build()
        );
    }

    public static ImmutableList<Pair<OneShot<LivingEntity>, Integer>> createLookBehaviors() {
        return ImmutableList.of(
                Pair.of(SetEntityLookTarget.create(EntityType.PLAYER, 12.0F), 1),
                Pair.of(SetEntityLookTarget.create(type -> type.getType().is(IMMEntityTags.HUMAN_BEINGS), 8.0F), 1),
                Pair.of(SetEntityLookTarget.create(8.0F), 1)
        );
    }

    public static void wasHurtBy(HumanLikeEntity cultivator, LivingEntity livingEntity) {
        Brain<?> brain = cultivator.getBrain();
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

    public static void setAttackTarget(HumanLikeEntity cultivator, LivingEntity livingEntity) {
        Brain<?> brain = cultivator.getBrain();
        brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        brain.eraseMemory(MemoryModuleType.BREED_TARGET);
        brain.setMemory(MemoryModuleType.ATTACK_TARGET, livingEntity);
    }

    public static Optional<? extends LivingEntity> findNearestValidAttackTarget(HumanLikeEntity cultivator) {
        return cultivator.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)
                .flatMap(l -> l.findClosest(cultivator::canTargetLiving));
    }

    public static boolean lowLevelLiving(LivingEntity livingEntity) {
        return ((!livingEntity.getAttributes().hasAttribute(Attributes.ARMOR) || livingEntity.getArmorValue() < 8) && livingEntity.getHealth() < 20) || livingEntity.getHealth() < 5;
    }

    public static boolean highLevelLiving(LivingEntity livingEntity) {
        return (livingEntity.getAttributes().hasAttribute(Attributes.ARMOR) && livingEntity.getArmorValue() > 16) || livingEntity.getHealth() > 30;
    }

}
