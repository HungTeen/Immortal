package hungteen.imm.common.entity.ai.behavior.golem;

import com.google.common.collect.ImmutableSet;
import hungteen.imm.common.entity.golem.GolemEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.item.ItemStack;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-04-23 23:05
 **/
public class GolemSenseHurtBy extends GolemSensorBehavior{

    public GolemSenseHurtBy(ItemStack stack) {
        super(stack, ImmutableSet.of(
                MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY
        ), 20);
    }

    @Override
    protected void start(ServerLevel level, GolemEntity golemEntity, long tick) {
        final Brain<?> brain = golemEntity.getBrain();
        final DamageSource source = golemEntity.getLastDamageSource();
        if (source != null) {
            brain.setMemory(MemoryModuleType.HURT_BY, source);
            if (source.getEntity() instanceof LivingEntity living) {
                brain.setMemory(MemoryModuleType.HURT_BY_ENTITY, living);
            }
        } else {
            brain.eraseMemory(MemoryModuleType.HURT_BY);
        }

        brain.getMemory(MemoryModuleType.HURT_BY_ENTITY).ifPresent((target) -> {
            if (! target.isAlive() || target.level() != level) {
                brain.eraseMemory(MemoryModuleType.HURT_BY_ENTITY);
            }
        });
    }

}
