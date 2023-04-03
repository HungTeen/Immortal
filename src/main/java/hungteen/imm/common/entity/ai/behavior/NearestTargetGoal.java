package hungteen.imm.common.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import hungteen.imm.common.entity.ai.behavior.golem.GolemStartAttacking;
import hungteen.imm.common.entity.golem.GolemEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.Sensor;

import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-25 15:10
 **/
public class NearestTargetGoal extends GolemStartAttacking<GolemEntity> {

    public NearestTargetGoal() {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryStatus.REGISTERED,
                MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT
        ), NearestTargetGoal::findNearestValidAttackTarget);
    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(GolemEntity golem) {
        return golem.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty()).findClosest((target) -> isTargetable(golem, target));
    }

    private static boolean isTargetable(GolemEntity golem, LivingEntity target) {
        EntityType<?> entitytype = target.getType();
        return entitytype != EntityType.ZOGLIN && entitytype != EntityType.CREEPER && Sensor.isEntityAttackable(golem, target);
    }

}
