package hungteen.imm.common.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import hungteen.imm.common.entity.human.HumanLikeEntity;
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
 * @program Immortal
 * @author HungTeen
 * @create 2022-12-07 15:28
 **/
public class HumanMeleeAttack extends Behavior<HumanLikeEntity> {
    private final int cooldown;

    public HumanMeleeAttack(int cooldown) {
        super(ImmutableMap.of(
                MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED,
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_ABSENT
        ));
        this.cooldown = cooldown;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverLevel, HumanLikeEntity entity) {
        final LivingEntity target = this.getAttackTarget(entity);
        return BehaviorUtils.canSee(entity, target) && entity.isWithinMeleeAttackRange(target);
    }

    @Override
    protected void start(ServerLevel level, HumanLikeEntity entity, long time) {
        LivingEntity target = this.getAttackTarget(entity);
        BehaviorUtils.lookAtEntity(entity, target);
        entity.swing(InteractionHand.MAIN_HAND);
        entity.doHurtTarget(target); //TODO 消耗耐久
        entity.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_COOLING_DOWN, true, Mth.floor(this.cooldown * entity.getAttackCoolDown()));
    }

    private LivingEntity getAttackTarget(Mob mob) {
        return mob.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
    }
}
