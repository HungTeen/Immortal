package hungteen.imm.common.entity.ai.behavior.golem;

import hungteen.imm.common.entity.golem.GolemEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-05 15:27
 **/
public abstract class GolemOneShotBehavior extends GolemBehavior {

    public GolemOneShotBehavior(ItemStack stack, Map<MemoryModuleType<?>, MemoryStatus> statusMap) {
        super(stack, statusMap, 0, 0);
    }

    @Override
    protected void start(ServerLevel level, GolemEntity golemEntity, long tick) {
        // Put sensor tick code here.
    }

}
