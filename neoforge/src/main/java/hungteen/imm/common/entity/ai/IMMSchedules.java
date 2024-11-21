package hungteen.imm.common.entity.ai;

import hungteen.htlib.api.registry.HTHolder;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.common.impl.registry.HTVanillaRegistry;
import hungteen.htlib.util.NeoHelper;
import hungteen.imm.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.entity.schedule.ScheduleBuilder;
import net.neoforged.bus.api.IEventBus;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-22 19:48
 **/
public class IMMSchedules {

    private static final HTVanillaRegistry<Schedule> SCHEDULES = HTRegistryManager.vanilla(Registries.SCHEDULE, Util.id());

    public static final HTHolder<Schedule> DAY_WORK = SCHEDULES.register("day_work", () -> builder()
                .changeActivityAt(10, Activity.IDLE)
                .changeActivityAt(2000, Activity.WORK)
//                .changeActivityAt(9000, Activity.MEET)
                .changeActivityAt(11000, Activity.IDLE)
//                .changeActivityAt(12000, Activity.REST)
                .build()
    );

    public static final HTHolder<Schedule> NIGHT_WORK = SCHEDULES.register("night_work", () -> builder()
            .changeActivityAt(12100, Activity.IDLE)
            .changeActivityAt(14000, Activity.WORK)
//            .changeActivityAt(21000, Activity.MEET)
            .changeActivityAt(23000, Activity.IDLE)
//            .changeActivityAt(23800, Activity.REST)
            .build()
    );

    private static ScheduleBuilder builder() {
        return new ScheduleBuilder(new Schedule());
    }

    public static void initialize(IEventBus modBus){
        NeoHelper.initRegistry(SCHEDULES, modBus);
    }

}
