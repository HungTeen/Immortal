package hungteen.imm.common.entity.ai.behavior.golem;

import com.google.common.collect.ImmutableMap;
import hungteen.htlib.util.helper.JavaHelper;
import hungteen.imm.common.entity.golem.GolemEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-04-05 15:12
 **/
public class GolemFindLookTarget extends GolemOneShotBehavior {

    private final double lookDistance = 10;
    private final Predicate<EntityType<?>> entityTypePredicate;

    public GolemFindLookTarget(ItemStack stack) {
        super(stack, ImmutableMap.of(
                MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT
        ));
        this.entityTypePredicate = get(0, JavaHelper::alwaysTrue);
    }

    @Override
    protected void start(ServerLevel level, GolemEntity golemEntity, long tick) {
        golemEntity.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).flatMap(livings -> livings.findClosest(entity -> {
            return this.entityTypePredicate.test(entity.getType()) && entity.distanceToSqr(golemEntity) <= this.lookDistance && ! golemEntity.hasPassenger(entity);
        })).ifPresent(entity -> {
            golemEntity.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(entity, true));
        });
    }

}
