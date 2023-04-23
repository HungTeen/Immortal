package hungteen.imm.common.entity.ai.behavior.golem;

import com.google.common.collect.ImmutableMap;
import hungteen.imm.common.entity.golem.GolemEntity;
import hungteen.imm.util.BehaviorUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-25 12:35
 **/
public class GolemMeleeAttack extends GolemOneShotBehavior {

    public GolemMeleeAttack(ItemStack stack) {
        super(stack, ImmutableMap.of(
                MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED,
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_ABSENT
//                MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT
        ));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, GolemEntity golem) {
        LivingEntity target = BehaviorUtil.getAttackTarget(golem).get();
        return golem.canMeleeAttack() && golem.isWithinMeleeAttackRange(target);
    }

    @Override
    protected void start(ServerLevel level, GolemEntity golemEntity, long tick) {
        LivingEntity target = BehaviorUtil.getAttackTarget(golemEntity).get();
        golemEntity.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
        golemEntity.swing(InteractionHand.MAIN_HAND);
        golemEntity.doHurtTarget(target);
        golemEntity.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_COOLING_DOWN, true, 20);
    }

}
