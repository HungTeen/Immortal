package hungteen.imm.common.rune.memory;

import hungteen.htlib.api.interfaces.ISimpleEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

import java.util.function.Supplier;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-02 20:37
 **/
public interface IMemoryRune extends ISimpleEntry {

    Supplier<MemoryModuleType<?>> getMemoryModule();

    default MutableComponent getComponent() {
        return Component.translatable("rune." + getModID() + ".memory." + this.getName());
    }
}
