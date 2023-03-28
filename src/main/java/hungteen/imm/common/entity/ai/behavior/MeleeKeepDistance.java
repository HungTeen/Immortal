package hungteen.imm.common.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import hungteen.imm.common.entity.human.HumanEntity;
import hungteen.imm.utils.BehaviorUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-03-12 21:11
 **/
public class MeleeKeepDistance extends Behavior<HumanEntity> {
    private final float strafeSpeed;
    private int strafingTime = -1;

    public MeleeKeepDistance(float speed) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT));
        this.strafeSpeed = speed;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, HumanEntity entity) {
        return this.isTargetVisible(entity);
    }

    @Override
    protected void start(ServerLevel level, HumanEntity entity, long time) {
        entity.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(this.getTarget(entity), true));
        entity.getMoveControl().strafe(this.isTargetTooClose(entity) ? - this.strafeSpeed : this.strafeSpeed, 0.0F);
        entity.setYRot(Mth.rotateIfNecessary(entity.getYRot(), entity.yHeadRot, 0.0F));
    }

    private boolean isTargetVisible(HumanEntity entity) {
        return entity.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).get().contains(this.getTarget(entity));
    }

    private boolean isTargetTooClose(HumanEntity entity) {
        return entity.distanceToSqr(this.getTarget(entity)) < entity.getMeleeAttackRangeSqr(entity) - 8;
    }

    private LivingEntity getTarget(HumanEntity entity) {
        return BehaviorUtil.getAttackTarget(entity).get();
    }
}