package hungteen.immortal.common.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import hungteen.immortal.utils.EntityUtil;
import hungteen.immortal.utils.ItemUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.CrossbowAttack;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/2 18:31
 */
public class MobRangeAttack<E extends Mob & RangedAttackMob, T extends LivingEntity> extends Behavior<E> {
    private static final int TIMEOUT = 1200;
    private final int attackIntervalMin;
    private final int attackIntervalMax;
    private final float attackRadius;
    private int attackTime;

    public MobRangeAttack(int attackIntervalMin, int attackIntervalMax, float attackRadius) {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), 1200);
        this.attackIntervalMin = attackIntervalMin;
        this.attackIntervalMax = attackIntervalMax;
        this.attackRadius = attackRadius;
    }

    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        LivingEntity target = getAttackTarget(entity);
        return EntityUtil.isMainHolding(entity, ItemUtil::isRangeWeapon) && BehaviorUtils.canSee(entity, target) && BehaviorUtils.isWithinAttackRange(entity, target, 0);
    }

    protected boolean canStillUse(ServerLevel serverLevel, E entity, long time) {
        return entity.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET) && this.checkExtraStartConditions(serverLevel, entity);
    }

    @Override
    protected void start(ServerLevel serverLevel, E entity, long time) {
        super.start(serverLevel, entity, time);
        entity.setAggressive(true);
    }

    protected void tick(ServerLevel serverLevel, E entity, long time) {
        final LivingEntity target = getAttackTarget(entity);
        final double distance = entity.distanceToSqr(target.getX(), target.getY(), target.getZ());
        entity.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
        if (-- this.attackTime == 0) {
            float f = (float)Math.sqrt(distance) / this.attackRadius;
            float f1 = Mth.clamp(f, 0.1F, 1.0F);
            entity.performRangedAttack(target, f1);
            this.attackTime = Mth.floor(f * (float)(this.attackIntervalMax - this.attackIntervalMin) + (float)this.attackIntervalMin);
        } else if (this.attackTime < 0) {
            this.attackTime = Mth.floor(Mth.lerp(Math.sqrt(distance) / (double)this.attackRadius, this.attackIntervalMin, this.attackIntervalMax));
        }
    }

    protected void stop(ServerLevel serverLevel, E entity, long time) {
        entity.setAggressive(false);
        if (entity.isUsingItem()) {
            entity.stopUsingItem();
        }
    }

    private static LivingEntity getAttackTarget(LivingEntity entity) {
        return entity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
    }

}
