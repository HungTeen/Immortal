package hungteen.imm.common.rune.sensor;

import hungteen.htlib.api.interfaces.ISimpleEntry;
import hungteen.imm.common.entity.golem.GolemEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;

import java.util.Set;
import java.util.function.Supplier;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-02 20:45
 **/
public interface ISensorRune extends ISimpleEntry {

    /**
     * 获取感知类型。
     */
    Supplier<SensorType<? extends Sensor<? super GolemEntity>>> getSensorType();

    /**
     * 会感知哪些记忆。
     */
    Set<MemoryModuleType<?>> getRequiredMemories();

    default MutableComponent getComponent() {
        return Component.translatable("rune." + getModID() + ".sensor." + this.getName());
    }

}
