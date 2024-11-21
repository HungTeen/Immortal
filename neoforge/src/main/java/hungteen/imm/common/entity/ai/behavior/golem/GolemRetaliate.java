package hungteen.imm.common.entity.ai.behavior.golem;

import com.google.common.collect.ImmutableMap;
import hungteen.htlib.util.helper.JavaHelper;
import hungteen.imm.common.entity.golem.GolemEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-04-06 20:05
 **/
public class GolemRetaliate extends GolemOneShotBehavior{

    private final Predicate<EntityType<?>> entityTypePredicate;

    public GolemRetaliate(ItemStack stack) {
        super(stack, ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryStatus.REGISTERED,
                MemoryModuleType.HURT_BY_ENTITY, MemoryStatus.VALUE_PRESENT
        ));
        this.entityTypePredicate = get(0, JavaHelper::alwaysTrue);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, GolemEntity golem) {
        Optional<LivingEntity> opt = this.getLivingEntity(golem);
        return opt.isPresent() && golem.canAttack(opt.get())
                && Sensor.isEntityAttackableIgnoringLineOfSight(golem, opt.get())
                && !BehaviorUtils.isOtherTargetMuchFurtherAwayThanCurrentAttackTarget(golem, opt.get(), 4.0D);
    }

    @Override
    protected void start(ServerLevel serverLevel, GolemEntity golem, long time) {
        this.getLivingEntity(golem).ifPresent(target -> {
            golem.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, target);
            golem.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        });
    }

    private Optional<LivingEntity> getLivingEntity(GolemEntity golem){
        return golem.getBrain().getMemory(MemoryModuleType.HURT_BY_ENTITY);
    }

}
