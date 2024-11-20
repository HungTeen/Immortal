package hungteen.imm.common.entity.ai.behavior.golem;

import hungteen.imm.common.entity.golem.GolemEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-05 11:42
 **/
public abstract class GolemSensorBehavior extends GolemBehavior{

    public GolemSensorBehavior(ItemStack stack, Set<MemoryModuleType<?>> memorySet, int duration) {
        super(stack, memorySet.stream().collect(Collectors.toMap(l -> l, l -> MemoryStatus.REGISTERED)), duration, duration);
    }

    @Override
    protected void start(ServerLevel level, GolemEntity golemEntity, long tick) {
        // Put sensor tick code here.
    }

}
