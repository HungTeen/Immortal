package hungteen.imm.common.entity.ai;

import hungteen.htlib.api.registry.HTHolder;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.common.impl.registry.HTVanillaRegistry;
import hungteen.htlib.util.NeoHelper;
import hungteen.imm.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.schedule.Activity;
import net.neoforged.bus.api.IEventBus;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-11-23 22:59
 **/
public class IMMActivities {

    private static final HTVanillaRegistry<Activity> ACTIVITIES = HTRegistryManager.vanilla(Registries.ACTIVITY, Util.id());

    public static final HTHolder<Activity> MELEE_FIGHT = register("melee_fight");
    public static final HTHolder<Activity> KEEP_DISTANCE = register("keep_distance");
    public static final HTHolder<Activity> RANGE_FIGHT = register("range_fight");
    public static final HTHolder<Activity> ESCAPE = register("escape");
    public static final HTHolder<Activity> TRADE = register("trade");
    public static final HTHolder<Activity> EAT = register("eat");
    public static final HTHolder<Activity> HOME = register("home");

    private static HTHolder<Activity> register(String name){
        return ACTIVITIES.register(name, () -> new Activity(Util.prefixName(name)));
    }

    public static void initialize(IEventBus event){
        NeoHelper.initRegistry(ACTIVITIES, event);
    }
}
