package hungteen.imm.common.entity.ai.behavior;

import hungteen.imm.util.BehaviorUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Map;
import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/9 20:46
 **/
public class KeepDistanceWithTarget extends Behavior<Mob> {

    private final double speedModifier;
    private final float attackRadiusSqr;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;
    
    public KeepDistanceWithTarget(float speedModifier, float attackRadius) {
        super(Map.of(
                MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED
        ), 100);
        this.speedModifier = speedModifier;
        this.attackRadiusSqr = attackRadius * attackRadius;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, Mob owner) {
        return true;
    }

    @Override
    protected boolean canStillUse(ServerLevel level, Mob entity, long gameTime) {
        return true;
    }

    @Override
    protected void tick(ServerLevel level, Mob mob, long gameTime) {
        Optional<LivingEntity> targetOpt = BehaviorUtil.getAttackTarget(mob);
        if (targetOpt.isPresent()) {
            LivingEntity livingentity = targetOpt.get();
            double distanceSqr = mob.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());

            if (distanceSqr < this.attackRadiusSqr * 1.5) {
                mob.getNavigation().stop();
                this.strafingTime++;
            } else {
                mob.getNavigation().moveTo(livingentity, this.speedModifier);
                this.strafingTime = -1;
            }

            // 随机更新闪避方向。
            if (this.strafingTime >= 20) {
                if ((double)mob.getRandom().nextFloat() < 0.3) {
                    this.strafingClockwise = !this.strafingClockwise;
                }

                if ((double)mob.getRandom().nextFloat() < 0.3) {
                    this.strafingBackwards = !this.strafingBackwards;
                }

                this.strafingTime = 0;
            }

            if (this.strafingTime > -1) {
                if (distanceSqr > (double)(this.attackRadiusSqr)) {
                    this.strafingBackwards = false;
                } else if (distanceSqr < (double)(this.attackRadiusSqr * 0.5F)) {
                    this.strafingBackwards = true;
                }

                mob.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                if (mob.getControlledVehicle() instanceof Mob mobVehicle) {
                    mobVehicle.lookAt(livingentity, 30.0F, 30.0F);
                }

            }
            mob.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(livingentity, true));
            mob.setYRot(Mth.rotateIfNecessary(mob.getYRot(), mob.yHeadRot, 0.0F));
        }
    }

}
