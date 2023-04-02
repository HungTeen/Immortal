package hungteen.imm.common.rune.behavior;

import hungteen.htlib.api.interfaces.ISimpleEntry;
import hungteen.imm.common.entity.golem.GolemEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-02 21:07
 **/
public interface IBehaviorRune extends ISimpleEntry {

    /**
     * 获取记忆类型构造器。
     */
    Function<GolemEntity, Behavior<? super GolemEntity>> getBehaviorFunction();

    /**
     * 执行此行为所需要的记忆状态。
     */
    Map<MemoryModuleType<?>, MemoryStatus> requireMemoryStatus(Level level);

    /**
     * 判断当前槽位所匹配的符文是否合适。
     * @return
     */
    List<Class<?>> getPredicateClasses();

    default MutableComponent getComponent() {
        return Component.translatable("rune." + getModID() + ".behavior." + this.getName());
    }

}
