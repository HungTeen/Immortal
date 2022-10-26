package hungteen.immortal.common.ai.behavior;

import com.google.common.collect.ImmutableMap;
import hungteen.immortal.common.entity.golem.GolemEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-25 15:06
 **/
public class GolemStartAttacking <E extends GolemEntity> extends Behavior<E> {
    private final Predicate<E> canAttackPredicate;
    private final Function<E, Optional<? extends LivingEntity>> targetFinderFunction;

    public GolemStartAttacking(Map<MemoryModuleType<?>, MemoryStatus> map, Predicate<E> predicate, Function<E, Optional<? extends LivingEntity>> function) {
        super(map);
        this.canAttackPredicate = predicate;
        this.targetFinderFunction = function;
    }

    public GolemStartAttacking(Map<MemoryModuleType<?>, MemoryStatus> map, Function<E, Optional<? extends LivingEntity>> function) {
        this(map, (entity) -> {
            return true;
        }, function);
    }

    protected boolean checkExtraStartConditions(ServerLevel serverLevel, E golem) {
        if (!this.canAttackPredicate.test(golem)) {
            return false;
        } else {
            Optional<? extends LivingEntity> optional = this.targetFinderFunction.apply(golem);
            return optional.isPresent() && golem.canAttack(optional.get());
        }
    }

    protected void start(ServerLevel serverLevel, E golem, long time) {
        this.targetFinderFunction.apply(golem).ifPresent((p_24218_) -> {
            this.setAttackTarget(golem, p_24218_);
        });
    }

    private void setAttackTarget(E golem, LivingEntity target) {
        golem.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, target);
        golem.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
    }
}