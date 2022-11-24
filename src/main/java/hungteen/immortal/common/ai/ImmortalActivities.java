package hungteen.immortal.common.ai;

import hungteen.immortal.utils.Util;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-23 22:59
 **/
public class ImmortalActivities {

    private static final DeferredRegister<Activity> ACTIVITIES = DeferredRegister.create(ForgeRegistries.ACTIVITIES, Util.id());

    public static final RegistryObject<Activity> MELEE_FIGHT = register("melee_fight");
    public static final RegistryObject<Activity> RANGE_FIGHT = register("range_fight");

    private static RegistryObject<Activity> register(String name){
        return ACTIVITIES.register(name, () -> new Activity(Util.prefixName(name)));
    }

    public static void register(IEventBus event){
        ACTIVITIES.register(event);
    }
}
