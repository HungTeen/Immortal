package hungteen.immortal.common.entity.human.cultivator;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import hungteen.immortal.common.entity.ai.ImmortalActivities;
import hungteen.immortal.common.entity.ai.ImmortalMemories;
import hungteen.immortal.common.entity.ai.behavior.*;
import hungteen.immortal.utils.BrainUtil;
import hungteen.immortal.utils.ItemUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.schedule.Activity;

import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-12-06 23:08
 **/
public class SpiritualCultivatorAi {

    protected static Brain<?> makeBrain(Brain<SpiritualCultivator> brain) {
        return brain;
    }

    /**
     * (Fighting -> Idle): if there is no enemy.
     */
    protected static void updateActivity(SpiritualCultivator cultivator) {

    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(Cultivator cultivator) {
        return cultivator.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).map(l -> {
            return l.findClosest(Monster.class::isInstance).orElse(null);
        });
    }

}
