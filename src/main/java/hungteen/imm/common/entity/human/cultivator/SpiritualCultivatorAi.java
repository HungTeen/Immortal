package hungteen.imm.common.entity.human.cultivator;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Monster;

import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-12-06 23:08
 **/
public class SpiritualCultivatorAi {

    protected static Brain<?> makeBrain(Brain<SpiritualBeginnerCultivator> brain) {
        return brain;
    }

    /**
     * (Fighting -> Idle): if there is no enemy.
     */
    protected static void updateActivity(SpiritualBeginnerCultivator cultivator) {

    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(Cultivator cultivator) {
        return cultivator.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).map(l -> {
            return l.findClosest(Monster.class::isInstance).orElse(null);
        });
    }

}
