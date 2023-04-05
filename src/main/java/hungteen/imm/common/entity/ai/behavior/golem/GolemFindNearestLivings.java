package hungteen.imm.common.entity.ai.behavior.golem;

import com.google.common.collect.ImmutableSet;
import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.imm.common.entity.golem.GolemEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import java.util.Comparator;
import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-05 11:39
 **/
public class GolemFindNearestLivings extends GolemSensorBehavior {

    public GolemFindNearestLivings(ItemStack stack) {
        super(stack, ImmutableSet.of(
                MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES
        ), 20);
    }

    @Override
    protected void start(ServerLevel level, GolemEntity golemEntity, long tick) {
        final double distance = golemEntity.getAttributeValue(Attributes.FOLLOW_RANGE);
        final AABB aabb = EntityHelper.getEntityAABB(golemEntity, distance, distance / 2);
        List<LivingEntity> list = EntityHelper.getPredicateEntities(golemEntity, aabb, LivingEntity.class, entity -> {
            return entity != golemEntity && EntityHelper.isEntityValid(entity);
        });
        list.sort(Comparator.comparingDouble(golemEntity::distanceToSqr));
        golemEntity.getBrain().setMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES, list);
        golemEntity.getBrain().setMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, new NearestVisibleLivingEntities(golemEntity, list));
    }

}
