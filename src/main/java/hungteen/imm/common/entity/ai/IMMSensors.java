package hungteen.imm.common.entity.ai;

import hungteen.imm.common.entity.ai.sensor.NearestAmethystSensor;
import hungteen.imm.common.entity.ai.sensor.NearestBoatSensor;
import hungteen.imm.common.entity.ai.sensor.NearestProjectileSensor;
import hungteen.imm.util.Util;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-24 22:10
 **/
public class IMMSensors {

    private static final DeferredRegister<SensorType<?>> SENSORS = DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, Util.id());

    public static final RegistryObject<SensorType<NearestBoatSensor<Mob>>> NEAREST_BOAT = register("nearest_boat", NearestBoatSensor::new);
    public static final RegistryObject<SensorType<NearestProjectileSensor<Mob>>> HAS_PROJECTILE_NEARBY = register("has_projectile_nearby", NearestProjectileSensor::new);
    public static final RegistryObject<SensorType<NearestAmethystSensor<Mob>>> NEAREST_AMETHYST = register("nearest_amethyst", NearestAmethystSensor::new);

    private static <U extends Sensor<?>> RegistryObject<SensorType<U>> register(String name, Supplier<U> supplier){
        return SENSORS.register(name, () -> new SensorType<>(supplier));
    }

    public static void register(IEventBus event){
        SENSORS.register(event);
    }

}
