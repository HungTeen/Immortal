package hungteen.immortal.common.entity.ai;

import hungteen.immortal.common.entity.ai.sensor.NearestBoatSensor;
import hungteen.immortal.utils.Util;
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
public class ImmortalSensors {

    private static final DeferredRegister<SensorType<?>> SENSORS = DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, Util.id());

    public static final RegistryObject<SensorType<NearestBoatSensor<Mob>>> NEAREST_BOAT = register("nearest_boat", NearestBoatSensor::new);

    private static <U extends Sensor<?>> RegistryObject<SensorType<U>> register(String name, Supplier<U> supplier){
        return SENSORS.register(name, () -> new SensorType<>(supplier));
    }

    public static void register(IEventBus event){
        SENSORS.register(event);
    }

}
