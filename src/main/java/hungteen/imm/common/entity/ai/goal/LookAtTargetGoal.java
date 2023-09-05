package hungteen.imm.common.entity.ai.goal;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;
import java.util.Objects;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-09-04 21:49
 **/
public class LookAtTargetGoal extends Goal {

    private final Mob mob;

    public LookAtTargetGoal(Mob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return mob.getTarget() != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void tick() {
        mob.getLookControl().setLookAt(Objects.requireNonNull(this.mob.getTarget()), 30F, 30F);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}
