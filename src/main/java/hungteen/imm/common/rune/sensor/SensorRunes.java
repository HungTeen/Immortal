package hungteen.imm.common.rune.sensor;

import hungteen.htlib.api.interfaces.IHTSimpleRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.htlib.common.registry.HTSimpleRegistry;
import hungteen.imm.ImmortalMod;
import hungteen.imm.common.entity.golem.GolemEntity;
import hungteen.imm.util.Util;
import net.minecraft.ChatFormatting;
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

    private static final HTSimpleRegistry<ISensorRune> SENSOR_RUNES = HTRegistryManager.create(Util.prefix("sensor_runes"));
    private static final List<ISensorRune> TYPES = new ArrayList<>();

    public static IHTSimpleRegistry<ISensorRune> registry() {
        return SENSOR_RUNES;
    }

    public static final ISensorRune HURT_BY = new SensorRune("hurt_by",
            () -> SensorType.HURT_BY
    );

    public static final ISensorRune NEAREST_LIVING_ENTITIES = new SensorRune("nearest_living_entities",
            () -> SensorType.NEAREST_LIVING_ENTITIES
    );

    public static class SensorRune implements ISensorRune {

        private final String name;
        private final Supplier<SensorType<? extends Sensor<? super GolemEntity>>> supplier;
        private Sensor<? super GolemEntity> sensorCache;

        /**
         * {@link ImmortalMod#coreRegister()}
         */
        public static void register() {
            registry().register(TYPES);
        }

        public SensorRune(String name, Supplier<SensorType<? extends Sensor<? super GolemEntity>>> supplier) {
            this.name = name;
            this.supplier = supplier;
            TYPES.add(this);
        }

        @Override
        public Supplier<SensorType<? extends Sensor<? super GolemEntity>>> getSensorType() {
            return this.supplier;
        }

        @Override
        public Set<MemoryModuleType<?>> getRequiredMemories() {
            if(this.supplier.get() == null) {
                return Collections.emptySet();
            }
            if(this.sensorCache == null){
                this.sensorCache = this.supplier.get().create();
            }
            return this.sensorCache.requires();
        }

        @Override
        public MutableComponent getComponent() {
            return ISensorRune.super.getComponent().withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.BOLD);
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
