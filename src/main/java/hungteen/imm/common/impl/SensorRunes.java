package hungteen.imm.common.impl;

import hungteen.imm.common.RuneManager;
import hungteen.imm.common.entity.golem.GolemEntity;
import hungteen.imm.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-06 18:16
 **/
public class SensorRunes {

    private static final List<RuneManager.ISensorRune> TYPES = new ArrayList<>();

    public static final RuneManager.ISensorRune HURT_BY = new SensorRune("hurt_by",
            () -> SensorType.HURT_BY
    );

    public static final RuneManager.ISensorRune NEAREST_LIVING_ENTITIES = new SensorRune("nearest_living_entities",
            () -> SensorType.NEAREST_LIVING_ENTITIES
    );

    public record SensorRune(String name, Supplier<SensorType<? extends Sensor<? super GolemEntity>>> sensorTypeSupplier) implements RuneManager.ISensorRune {

        public static void register() {
            TYPES.forEach(RuneManager::registerSensorRune);
        }

        public SensorRune(String name, Supplier<SensorType<? extends Sensor<? super GolemEntity>>> sensorTypeSupplier) {
            this.name = name;
            this.sensorTypeSupplier = sensorTypeSupplier;
            TYPES.add(this);
        }

        @Override
        public Supplier<SensorType<? extends Sensor<? super GolemEntity>>> getSensorType() {
            return this.sensorTypeSupplier;
        }

        @Override
        public Set<MemoryModuleType<?>> getSensorMemories() {
            return this.sensorTypeSupplier.get() == null ? Collections.emptySet() : this.sensorTypeSupplier.get().create().requires();
        }

        @Override
        public MutableComponent getComponent() {
            return Component.translatable("rune." + getModID() + ".sensor." + this.getName()).withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.BOLD);
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public String getModID() {
            return Util.id();
        }
    }

}
