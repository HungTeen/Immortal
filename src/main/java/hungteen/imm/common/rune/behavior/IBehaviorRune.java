package hungteen.imm.common.rune.behavior;

import hungteen.htlib.api.interfaces.ISimpleEntry;
import hungteen.imm.common.entity.ai.behavior.golem.GolemBehavior;
import hungteen.imm.common.rune.ICraftableRune;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Map;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-02 21:07
 **/
public interface IBehaviorRune extends ISimpleEntry, ICraftableRune {

    /**
     * 获取记忆类型构造器。
     */
    IBehaviorFactory getBehaviorFactory();

    /**
     * 执行此行为的记忆状态变化。
     */
    Map<MemoryModuleType<?>, List<MemoryStatus>> requireMemoryStatus();

    /**
     * 判断当前槽位所匹配的符文是否合适。
     * @return
     */
    List<Class<?>> getPredicateClasses();

    default int maxSlot() {
        return getPredicateClasses().size();
    }

    default MutableComponent getComponent() {
        return Component.translatable("rune." + getModID() + ".behavior." + this.getName()).withStyle(ChatFormatting.GOLD);
    }

    @FunctionalInterface
    interface IBehaviorFactory {

        GolemBehavior create(ItemStack stack);
    }

}
