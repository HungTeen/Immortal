package hungteen.imm.common.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-12-07 10:52
 **/
public class StartFighting<E extends Mob> extends Behavior<E> {
    private final Predicate<E> canAttackPredicate;
    private final Function<E, Optional<? extends LivingEntity>> targetFinderFunction;

    public StartFighting(Predicate<E> predicate, Function<E, Optional<? extends LivingEntity>> function) {
        this(predicate, function, 60);
    }

    public StartFighting(Predicate<E> predicate, Function<E, Optional<? extends LivingEntity>> function, int time) {
        super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryStatus.REGISTERED), time);
        this.canAttackPredicate = predicate;
        this.targetFinderFunction = function;
    }

    public StartFighting(Function<E, Optional<? extends LivingEntity>> function) {
        this((p_24212_) -> {
            return true;
        }, function);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E mob) {
        if (!this.canAttackPredicate.test(mob)) {
            return false;
        } else {
            Optional<? extends LivingEntity> optional = this.targetFinderFunction.apply(mob);
            return optional.filter(mob::canAttack).isPresent();
        }
    }

    @Override
    protected void start(ServerLevel level, E mob, long time) {
        this.targetFinderFunction.apply(mob).ifPresent((target) -> {
            this.setAttackTarget(mob, target);
        });
    }

    public <E extends Mob> void setAttackTarget(E mob, LivingEntity target) {
        mob.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, target);
        mob.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
    }

}
