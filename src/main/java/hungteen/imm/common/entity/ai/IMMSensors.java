package hungteen.imm.common.entity.ai;

import hungteen.htlib.api.registry.HTHolder;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.common.impl.registry.HTVanillaRegistry;
import hungteen.htlib.util.NeoHelper;
import hungteen.imm.common.entity.ai.sensor.NearestAmethystSensor;
import hungteen.imm.common.entity.ai.sensor.NearestBoatSensor;
import hungteen.imm.common.entity.ai.sensor.NearestProjectileSensor;
import hungteen.imm.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.neoforged.bus.api.IEventBus;

import java.util.function.Supplier;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-24 22:10
 **/
public class IMMSensors {

    private static final HTVanillaRegistry<SensorType<?>> SENSORS = HTRegistryManager.vanilla(Registries.SENSOR_TYPE, Util.id());

    public static final HTHolder<SensorType<NearestBoatSensor<Mob>>> NEAREST_BOAT = register("nearest_boat", NearestBoatSensor::new);
    public static final HTHolder<SensorType<NearestProjectileSensor<Mob>>> HAS_PROJECTILE_NEARBY = register("has_projectile_nearby", NearestProjectileSensor::new);
    public static final HTHolder<SensorType<NearestAmethystSensor<Mob>>> NEAREST_AMETHYST = register("nearest_amethyst", NearestAmethystSensor::new);

    private static <U extends Sensor<?>> HTHolder<SensorType<U>> register(String name, Supplier<U> supplier){
        return SENSORS.register(name, () -> new SensorType<>(supplier));
    }

    public static void initialize(IEventBus event){
        NeoHelper.initRegistry(SENSORS, event);
    }

}
