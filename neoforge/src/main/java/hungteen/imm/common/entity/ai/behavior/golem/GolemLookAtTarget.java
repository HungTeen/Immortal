package hungteen.imm.common.entity.ai.behavior.golem;

import com.google.common.collect.ImmutableMap;
import hungteen.imm.common.entity.golem.GolemEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-04-05 15:08
 **/
public class GolemLookAtTarget extends GolemBehavior{

    public GolemLookAtTarget(ItemStack stack) {
        super(stack, ImmutableMap.of(
                MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_PRESENT
        ), 20, 30);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, GolemEntity golem, long time) {
        return golem.getBrain().getMemory(MemoryModuleType.LOOK_TARGET).filter(entity -> entity.isVisibleBy(golem)).isPresent();
    }

    @Override
    protected void stop(ServerLevel level, GolemEntity golem, long time) {
        golem.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
    }

    @Override
    protected void tick(ServerLevel level, GolemEntity golem, long time) {
        golem.getBrain().getMemory(MemoryModuleType.LOOK_TARGET).ifPresent(target -> golem.getLookControl().setLookAt(target.currentPosition()));
    }

}
