package hungteen.immortal.utils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-24 22:44
 **/
public class BehaviorUtil {

    public static void lookAtEntity(LivingEntity entity, Entity target) {
        entity.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
    }

}
