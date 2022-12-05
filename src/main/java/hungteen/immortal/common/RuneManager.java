package hungteen.immortal.common;

import hungteen.htlib.api.interfaces.ISimpleEntry;
import hungteen.immortal.common.entity.golem.GolemEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-24 22:58
 **/
public class RuneManager {

    private static final Map<String, IMemoryRune> MEMORY_RUNE_MAP = new HashMap<>();
    private static final Map<MemoryModuleType<?>, IMemoryRune> MEMORY_CACHE = new HashMap<>();
    private static final Map<String, RuneManager.ISensorRune> SENSOR_RUNE_MAP = new HashMap<>();
    private static final Map<String, RuneManager.IBehaviorRune> BEHAVIOR_RUNE_MAP = new HashMap<>();

    public static void registerMemoryRune(RuneManager.IMemoryRune type) {
        MEMORY_RUNE_MAP.put(type.getRegistryName(), type);
    }

    public static Collection<IMemoryRune> getMemoryRunes() {
        return Collections.unmodifiableCollection(MEMORY_RUNE_MAP.values());
    }

    public static Optional<IMemoryRune> getMemoryRune(String type) {
        return Optional.ofNullable(MEMORY_RUNE_MAP.getOrDefault(type, null));
    }

    public static void registerSensorRune(RuneManager.ISensorRune type) {
        SENSOR_RUNE_MAP.put(type.getRegistryName(), type);
    }

    public static Collection<RuneManager.ISensorRune> getSensorRunes() {
        return Collections.unmodifiableCollection(SENSOR_RUNE_MAP.values());
    }

    public static Optional<RuneManager.ISensorRune> getSensorRune(String type) {
        return Optional.ofNullable(SENSOR_RUNE_MAP.getOrDefault(type, null));
    }

    public static void registerBehaviorRune(RuneManager.IBehaviorRune type) {
        BEHAVIOR_RUNE_MAP.put(type.getRegistryName(), type);
    }

    public static Collection<RuneManager.IBehaviorRune> getBehaviorRunes() {
        return Collections.unmodifiableCollection(BEHAVIOR_RUNE_MAP.values());
    }

    public static Optional<RuneManager.IBehaviorRune> getBehaviorRune(String type) {
        return Optional.ofNullable(BEHAVIOR_RUNE_MAP.getOrDefault(type, null));
    }

    public static RuneManager.IMemoryRune getMemoryRune(MemoryModuleType<?> memory) {
        if(! MEMORY_CACHE.containsKey(memory)){
            getMemoryRunes().forEach(rune -> {
                if(memory.equals(rune.getMemoryType().get())){
                    MEMORY_CACHE.put(memory, rune);
                }
            });
        }
        return MEMORY_CACHE.get(memory);
    }

    public static Component getStatusText(MemoryModuleType<?> type, MemoryStatus status){
        final MutableComponent component = getMemoryRune(type).getComponent();
        final Component statusComponent = Component.translatable("rune.immortal.status." + status.toString().toLowerCase(Locale.ROOT)).withStyle(
                status == MemoryStatus.REGISTERED ? ChatFormatting.YELLOW :
                        status == MemoryStatus.VALUE_ABSENT ? ChatFormatting.RED :
                                ChatFormatting.GREEN
        );
        return component.append(" : ").append(statusComponent);
    }

    /**
     * @program: Immortal
     * @author: HungTeen
     * @create: 2022-10-24 10:05
     **/
    public interface IMemoryRune extends ISimpleEntry {

        /**
         * 获取行为类型。
         */
        Supplier<MemoryModuleType<?>> getMemoryType();

    }

    /**
     * @program: Immortal
     * @author: HungTeen
     * @create: 2022-10-06 17:54
     *
     * 取值符文
     **/
    public interface ISensorRune extends ISimpleEntry {

        /**
         * 获取感知类型。
         */
        Supplier<SensorType<? extends Sensor<? super GolemEntity>>> getSensorType();

        /**
         * 会感知哪些记忆。
         */
        Set<MemoryModuleType<?>> getSensorMemories();
    }

    /**
     * @program: Immortal
     * @author: HungTeen
     * @create: 2022-10-06 17:14
     *
     * 效果符文。
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
    }

}
