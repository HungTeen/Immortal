package hungteen.imm.common.entity.ai.behavior.golem;

import com.google.common.collect.ImmutableMap;
import hungteen.imm.common.entity.golem.GolemEntity;
import hungteen.imm.util.BehaviorUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-04-09 18:34
 **/
public class GolemSetWalkToAttackTarget extends GolemOneShotBehavior {
    public GolemSetWalkToAttackTarget(ItemStack stack) {
        super(stack, ImmutableMap.of(
                MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED,
                MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED,
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.REGISTERED
        ));
    }

    @Override
    protected void start(ServerLevel level, GolemEntity golemEntity, long tick) {
        LivingEntity livingentity = BehaviorUtil.getAttackTarget(golemEntity).get();
        Optional<NearestVisibleLivingEntities> optional = golemEntity.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
        if (optional.isPresent() && optional.get().contains(livingentity) && BehaviorUtils.isWithinAttackRange(golemEntity, livingentity, 1)) {
            golemEntity.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        } else {
            golemEntity.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, (new EntityTracker(livingentity, true)));
            golemEntity.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new EntityTracker(livingentity, false), 1F, 0));
        }
    }
}
