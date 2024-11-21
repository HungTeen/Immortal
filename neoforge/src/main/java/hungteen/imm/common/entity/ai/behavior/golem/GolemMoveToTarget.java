package hungteen.imm.common.entity.ai.behavior.golem;

import com.google.common.collect.ImmutableMap;
import hungteen.imm.common.entity.golem.GolemEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-04-09 15:38
 **/
public class GolemMoveToTarget extends GolemBehavior{

    private static final int MAX_COOLDOWN_BEFORE_RETRYING = 40;
    private int remainingCooldown;
    @Nullable
    private Path path;
    @Nullable
    private BlockPos lastTargetPos;
    private float speedModifier = 1F;

    public GolemMoveToTarget(ItemStack stack) {
        super(stack, ImmutableMap.of(
                MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryStatus.REGISTERED,
                MemoryModuleType.PATH, MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_PRESENT
        ), 150, 250);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, GolemEntity golem) {
        if (this.remainingCooldown > 0) {
            --this.remainingCooldown;
            return false;
        } else {
            Brain<?> brain = golem.getBrain();
            WalkTarget walktarget = brain.getMemory(MemoryModuleType.WALK_TARGET).get();
            boolean flag = this.reachedTarget(golem, walktarget);
            if (!flag && this.tryComputePath(golem, walktarget, level.getGameTime())) {
                this.lastTargetPos = walktarget.getTarget().currentBlockPosition();
                return true;
            } else {
                brain.eraseMemory(MemoryModuleType.WALK_TARGET);
                if (flag) {
                    brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
                }
                return false;
            }
        }
    }

    @Override
    protected boolean canStillUse(ServerLevel p_23586_, GolemEntity p_23587_, long p_23588_) {
        if (this.path != null && this.lastTargetPos != null) {
            Optional<WalkTarget> optional = p_23587_.getBrain().getMemory(MemoryModuleType.WALK_TARGET);
            PathNavigation pathnavigation = p_23587_.getNavigation();
            return !pathnavigation.isDone() && optional.isPresent() && !this.reachedTarget(p_23587_, optional.get());
        } else {
            return false;
        }
    }

    @Override
    protected void stop(ServerLevel p_23601_, GolemEntity p_23602_, long p_23603_) {
        if (p_23602_.getBrain().hasMemoryValue(MemoryModuleType.WALK_TARGET) && !this.reachedTarget(p_23602_, p_23602_.getBrain().getMemory(MemoryModuleType.WALK_TARGET).get()) && p_23602_.getNavigation().isStuck()) {
            this.remainingCooldown = p_23601_.getRandom().nextInt(40);
        }

        p_23602_.getNavigation().stop();
        p_23602_.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        p_23602_.getBrain().eraseMemory(MemoryModuleType.PATH);
        this.path = null;
    }

    @Override
    protected void start(ServerLevel p_23609_, GolemEntity p_23610_, long p_23611_) {
        p_23610_.getBrain().setMemory(MemoryModuleType.PATH, this.path);
        p_23610_.getNavigation().moveTo(this.path, (double)this.speedModifier);
    }

    @Override
    protected void tick(ServerLevel p_23617_, GolemEntity p_23618_, long p_23619_) {
        Path path = p_23618_.getNavigation().getPath();
        Brain<?> brain = p_23618_.getBrain();
        if (this.path != path) {
            this.path = path;
            brain.setMemory(MemoryModuleType.PATH, path);
        }

        if (path != null && this.lastTargetPos != null) {
            WalkTarget walktarget = brain.getMemory(MemoryModuleType.WALK_TARGET).get();
            if (walktarget.getTarget().currentBlockPosition().distSqr(this.lastTargetPos) > 4.0D && this.tryComputePath(p_23618_, walktarget, p_23617_.getGameTime())) {
                this.lastTargetPos = walktarget.getTarget().currentBlockPosition();
                this.start(p_23617_, p_23618_, p_23619_);
            }
        }
    }

    private boolean tryComputePath(GolemEntity p_23593_, WalkTarget p_23594_, long p_23595_) {
        BlockPos blockpos = p_23594_.getTarget().currentBlockPosition();
        this.path = p_23593_.getNavigation().createPath(blockpos, 0);
        this.speedModifier = p_23594_.getSpeedModifier();
        Brain<?> brain = p_23593_.getBrain();
        if (this.reachedTarget(p_23593_, p_23594_)) {
            brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        } else {
            boolean flag = this.path != null && this.path.canReach();
            if (flag) {
                brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
            } else if (!brain.hasMemoryValue(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE)) {
                brain.setMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, p_23595_);
            }

            if (this.path != null) {
                return true;
            }

            Vec3 vec3 = DefaultRandomPos.getPosTowards((PathfinderMob)p_23593_, 10, 7, Vec3.atBottomCenterOf(blockpos), (double)((float)Math.PI / 2F));
            if (vec3 != null) {
                this.path = p_23593_.getNavigation().createPath(vec3.x, vec3.y, vec3.z, 0);
                return this.path != null;
            }
        }

        return false;
    }

    private boolean reachedTarget(GolemEntity p_23590_, WalkTarget p_23591_) {
        return p_23591_.getTarget().currentBlockPosition().distManhattan(p_23590_.blockPosition()) <= p_23591_.getCloseEnoughDist();
    }

}
