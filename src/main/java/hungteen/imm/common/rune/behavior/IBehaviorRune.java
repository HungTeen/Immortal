package hungteen.imm.common.rune.behavior;

import hungteen.htlib.api.interfaces.ISimpleEntry;
import hungteen.imm.common.entity.ai.behavior.golem.GolemBehavior;
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
public interface IBehaviorRune extends ISimpleEntry {

    /**
     * 获取记忆类型构造器。
     */
    IBehaviorFactory getBehaviorFactory();

    /**
     * 执行此行为所需要的记忆状态。
     */
    Map<MemoryModuleType<?>, MemoryStatus> requireMemoryStatus();

    /**
     * 判断当前槽位所匹配的符文是否合适。
     * @return
     */
    List<Class<?>> getPredicateClasses();

//    default boolean match(int id, CompoundTag tag) {
//        return parse(id, tag).isPresent();
//    }

//    default Optional<?> parse(int id, CompoundTag tag) {
//        if (id >= 0 && id < maxSlot()) {
//            return getPredicateCodecs().get(id).parse(NbtOps.INSTANCE, tag)
//                    .result();
//        }
//        return Optional.empty();
//    }

    default int maxSlot() {
        return getPredicateClasses().size();
    }

    default MutableComponent getComponent() {
        return Component.translatable("rune." + getModID() + ".behavior." + this.getName());
    }

    @FunctionalInterface
    interface IBehaviorFactory {

        GolemBehavior create(ItemStack stack);
    }

}
