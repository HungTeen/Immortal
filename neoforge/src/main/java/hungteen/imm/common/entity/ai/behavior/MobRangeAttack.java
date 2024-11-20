package hungteen.imm.common.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import hungteen.imm.util.BehaviorUtil;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.ItemUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.monster.RangedAttackMob;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/2 18:31
 */
public class MobRangeAttack<E extends Mob & RangedAttackMob, T extends LivingEntity> extends Behavior<E> {

    private final int attackIntervalMin;
    private final int attackIntervalMax;
    private final float attackRadius;

    public MobRangeAttack(int attackIntervalMin, int attackIntervalMax, float attackRadius) {
        super(ImmutableMap.of(
                MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED,
                MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT
        ), 20);
        this.attackIntervalMin = attackIntervalMin;
        this.attackIntervalMax = attackIntervalMax;
        this.attackRadius = attackRadius;
    }

    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        LivingEntity target = getAttackTarget(entity);
        return ! entity.isUsingItem() && EntityUtil.isMainHolding(entity, ItemUtil::isRangeWeapon) && BehaviorUtils.canSee(entity, target) && BehaviorUtils.isWithinAttackRange(entity, target, 0);
    }

    protected boolean canStillUse(ServerLevel serverLevel, E entity, long time) {
        if(BehaviorUtil.getAttackTarget(entity).isEmpty()) return false;
        LivingEntity target = getAttackTarget(entity);
        return EntityUtil.isMainHolding(entity, ItemUtil::isRangeWeapon) && BehaviorUtils.canSee(entity, target) && BehaviorUtils.isWithinAttackRange(entity, target, -10);
    }

    @Override
    protected void start(ServerLevel serverLevel, E entity, long time) {
        super.start(serverLevel, entity, time);
        entity.startUsingItem(InteractionHand.MAIN_HAND);
        entity.setAggressive(true);
        this.setAttackTime(entity);
    }

    protected void tick(ServerLevel serverLevel, E entity, long time) {
        final LivingEntity target = getAttackTarget(entity);
        entity.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
        if (timedOut(time + 1)) {
            entity.performRangedAttack(target, this.getStrength(entity));
            entity.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_COOLING_DOWN, true, 20);
        }
    }

    protected void stop(ServerLevel serverLevel, E entity, long time) {
        entity.setAggressive(false);
        if (entity.isUsingItem()) {
            entity.stopUsingItem();
        }
    }

    private int setAttackTime(E entity) {
        final double distance = entity.distanceToSqr(getAttackTarget(entity));
        final float f = (float)Math.sqrt(distance) / this.attackRadius;
        return Mth.floor(f * (float)(this.attackIntervalMax - this.attackIntervalMin) + (float)this.attackIntervalMin);
    }

    private float getStrength(E entity){
        final double distance = entity.distanceToSqr(getAttackTarget(entity));
        final float f = (float)Math.sqrt(distance) / this.attackRadius;
        return Mth.clamp(f, 0.1F, 1.0F);
    }
    private static LivingEntity getAttackTarget(LivingEntity entity) {
        return entity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
    }

}
