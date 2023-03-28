package hungteen.imm.utils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;

import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-24 22:44
 **/
public class BehaviorUtil {

    public static boolean isIdle(LivingEntity entity){
        return isInActivity(entity, Activity.IDLE);
    }

    public static boolean isInActivity(LivingEntity entity, Activity activity){
        return entity.getBrain().getActiveNonCoreActivity().isEmpty() || entity.getBrain().getActiveNonCoreActivity().get().equals(activity);
    }

    public static void lookAtEntity(LivingEntity entity, Entity target) {
        entity.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
    }

    public static Optional<LivingEntity> getAttackTarget(LivingEntity entity){
        return entity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
    }

}
