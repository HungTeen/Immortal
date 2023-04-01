package hungteen.imm.common.entity.ai;

import hungteen.imm.util.Util;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.entity.schedule.ScheduleBuilder;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-22 19:48
 **/
public class IMMSchedules {

    public static final DeferredRegister<Schedule> SCHEDULES = DeferredRegister.create(ForgeRegistries.SCHEDULES, Util.id());

    public static final RegistryObject<Schedule> DAY_WORK = SCHEDULES.register("day_work", () -> builder()
                .changeActivityAt(10, Activity.IDLE)
                .changeActivityAt(2000, Activity.WORK)
//                .changeActivityAt(9000, Activity.MEET)
                .changeActivityAt(11000, Activity.IDLE)
//                .changeActivityAt(12000, Activity.REST)
                .build()
    );

    public static final RegistryObject<Schedule> NIGHT_WORK = SCHEDULES.register("night_work", () -> builder()
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

}
