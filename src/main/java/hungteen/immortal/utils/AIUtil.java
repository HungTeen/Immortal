package hungteen.immortal.utils;

import hungteen.immortal.common.entity.human.cultivator.Cultivator;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Monster;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/1 19:38
 */
public class AIUtil {

    public static boolean noTarget(LivingEntity entity){
        return entity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isEmpty();
    }

    public static boolean healthBelow(LivingEntity entity, double percent){
        return entity.getHealth() < percent * Objects.requireNonNull(entity.getAttribute(Attributes.MAX_HEALTH)).getValue();
    }

    public static Optional<? extends LivingEntity> findNearestValidTarget(LivingEntity entity, Predicate<LivingEntity> predicate) {
        return entity.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).flatMap(l -> l.findClosest(predicate));
    }

    public static boolean hasCrossbow(LivingEntity p_34919_) {
        return p_34919_.isHolding(is -> is.getItem() instanceof net.minecraft.world.item.CrossbowItem);
    }

}
