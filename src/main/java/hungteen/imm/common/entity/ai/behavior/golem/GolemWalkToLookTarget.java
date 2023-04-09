package hungteen.imm.common.entity.ai.behavior.golem;

import com.google.common.collect.ImmutableMap;
import hungteen.imm.common.entity.golem.GolemEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.item.ItemStack;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-09 15:41
 **/
public class GolemWalkToLookTarget extends GolemOneShotBehavior{

    public GolemWalkToLookTarget(ItemStack stack) {
        super(stack, ImmutableMap.of(
                MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_PRESENT
        ));
    }

    @Override
    protected void start(ServerLevel level, GolemEntity golemEntity, long tick) {
        golemEntity.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(
                golemEntity.getBrain().getMemory(MemoryModuleType.LOOK_TARGET).get(), 1F, 3
        ));
    }

}
