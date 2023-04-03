package hungteen.imm.common.rune.behavior;

import com.mojang.serialization.Codec;
import hungteen.htlib.api.interfaces.ISimpleEntry;
import hungteen.imm.common.entity.ai.behavior.golem.GolemBehavior;
import hungteen.imm.common.entity.golem.GolemEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    Function<GolemEntity, GolemBehavior> getBehaviorFunction();

    /**
     * 执行此行为所需要的记忆状态。
     */
    Map<MemoryModuleType<?>, MemoryStatus> requireMemoryStatus(Level level);

    /**
     * 判断当前槽位所匹配的符文是否合适。
     * @return
     */
    List<Codec<?>> getPredicateCodecs();

    default boolean match(int id, CompoundTag tag) {
        return parse(id, tag).isPresent();
    }

    default Optional<?> parse(int id, CompoundTag tag) {
        if (id >= 0 && id < maxSlot()) {
            return getPredicateCodecs().get(id).parse(NbtOps.INSTANCE, tag)
                    .result();
        }
        return Optional.empty();
    }

    default int maxSlot() {
        return getPredicateCodecs().size();
    }

    default MutableComponent getComponent() {
        return Component.translatable("rune." + getModID() + ".behavior." + this.getName());
    }

}
