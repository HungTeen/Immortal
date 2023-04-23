package hungteen.imm.common.entity.ai.behavior.golem;

import com.google.common.collect.ImmutableMap;
import hungteen.htlib.util.helper.JavaHelper;
import hungteen.imm.common.entity.golem.GolemEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-06 20:05
 **/
public class GolemFindAttackTarget extends GolemOneShotBehavior{

    private final Predicate<GolemEntity> attackPredicate;
    private final Predicate<EntityType<?>> entityTypePredicate;
    private final Function<GolemEntity, Optional<? extends LivingEntity>> targetFinderFunction;

    public GolemFindAttackTarget(ItemStack stack) {
        super(stack, ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryStatus.REGISTERED,
                MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT
        ));
        this.attackPredicate = JavaHelper::alwaysTrue;
        this.entityTypePredicate = get(0, JavaHelper::alwaysTrue);
        this.targetFinderFunction = this::findNearestValidAttackTarget;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, GolemEntity golem) {
        if (!this.attackPredicate.test(golem)) {
            return false;
        } else {
            Optional<? extends LivingEntity> optional = this.targetFinderFunction.apply(golem);
            return optional.isPresent() && golem.canAttack(optional.get());
        }
    }

    @Override
    protected void start(ServerLevel serverLevel, GolemEntity golem, long time) {
        this.targetFinderFunction.apply(golem).ifPresent(target -> {
            golem.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, target);
            golem.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        });
    }

    private Optional<? extends LivingEntity> findNearestValidAttackTarget(GolemEntity golem) {
        return golem.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty()).findClosest((target) -> {
            return this.entityTypePredicate.test(target.getType()) && Sensor.isEntityAttackable(golem, target);
        });
    }

}
