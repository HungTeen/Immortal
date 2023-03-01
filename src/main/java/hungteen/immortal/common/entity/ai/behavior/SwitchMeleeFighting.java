package hungteen.immortal.common.entity.ai.behavior;

import hungteen.immortal.common.entity.ai.ImmortalActivities;
import hungteen.immortal.common.entity.human.cultivator.Cultivator;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/1 19:30
 */
public class SwitchMeleeFighting extends StartFighting<Cultivator>{

    public SwitchMeleeFighting(Predicate<Cultivator> predicate, Function<Cultivator, Optional<? extends LivingEntity>> function) {
        super(predicate, function);
    }

    public SwitchMeleeFighting(Predicate<Cultivator> predicate, Function<Cultivator, Optional<? extends LivingEntity>> function, int time) {
        super(predicate, function, time);
    }

    public SwitchMeleeFighting(Function<Cultivator, Optional<? extends LivingEntity>> function) {
        super(function);
    }

    @Override
    public <E extends Mob> void setAttackTarget(E mob, LivingEntity target) {
        super.setAttackTarget(mob, target);
        mob.getBrain().setActiveActivityIfPossible(ImmortalActivities.MELEE_FIGHT.get());
    }
}
