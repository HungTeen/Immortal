package hungteen.immortal.common.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import hungteen.immortal.common.entity.human.HumanEntity;
import hungteen.immortal.common.tag.ImmortalItemTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

/**
 * Modify from {@link net.minecraft.world.entity.ai.behavior.MeleeAttack}.
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-12-07 15:28
 **/
public class HumanMeleeAttack extends Behavior<HumanEntity> {
    private final int cooldownModifier;

    public HumanMeleeAttack(int cooldownModifier) {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_ABSENT));
        this.cooldownModifier = cooldownModifier;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverLevel, HumanEntity entity) {
        final LivingEntity target = this.getAttackTarget(entity);
        return isHoldingMeleeWeapon(entity) && BehaviorUtils.canSee(entity, target) && entity.isWithinMeleeAttackRange(target);
    }

    private static boolean isHoldingMeleeWeapon(HumanEntity entity) {
        return entity.isHolding(stack -> stack.is(ImmortalItemTags.MELEE_ATTACK_ITEMS));
    }

    @Override
    protected void start(ServerLevel p_23524_, HumanEntity entity, long time) {
        LivingEntity target = this.getAttackTarget(entity);
        BehaviorUtils.lookAtEntity(entity, target);
        entity.swing(InteractionHand.MAIN_HAND);
        entity.doHurtTarget(target);
        entity.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_COOLING_DOWN, true, Mth.floor(this.cooldownModifier * entity.getAttackCoolDown()));
    }

    private LivingEntity getAttackTarget(Mob mob) {
        return mob.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
    }
}
