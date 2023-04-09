package hungteen.imm.common.entity.ai.behavior.golem;

import com.google.common.collect.ImmutableMap;
import hungteen.htlib.util.helper.JavaHelper;
import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.imm.common.entity.golem.GolemEntity;
import hungteen.imm.util.BehaviorUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-09 18:41
 **/
public class GolemStopAttackingIfTargetInvalid extends GolemOneShotBehavior{

    private static final Predicate<LivingEntity> targetPredicate = JavaHelper::alwaysTrue;

    public GolemStopAttackingIfTargetInvalid(ItemStack stack) {
        super(stack, ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryStatus.REGISTERED
        ));
    }

    @Override
    protected void start(ServerLevel level, GolemEntity golemEntity, long tick) {
        LivingEntity target = BehaviorUtil.getAttackTarget(golemEntity).get();
        final boolean closeAttack = true;
        if (golemEntity.canAttack(target) && (!closeAttack || !isTiredOfTryingToReachTarget(golemEntity))
                && EntityHelper.isEntityValid(target) && golemEntity.level == target.level && targetPredicate.test(target)) {
            // Pass.
        } else {
            golemEntity.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
        }
    }

    private static boolean isTiredOfTryingToReachTarget(GolemEntity golem) {
        final Optional<Long> opt = golem.getBrain().getMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        return opt.isPresent() && golem.level.getGameTime() - opt.get() > 200L;
    }
}
