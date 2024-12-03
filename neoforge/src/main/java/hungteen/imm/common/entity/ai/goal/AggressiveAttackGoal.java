package hungteen.imm.common.entity.ai.goal;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

/**
 * Copy from {@link net.minecraft.world.entity.ai.goal.ZombieAttackGoal}.
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/3 16:04
 **/
public class AggressiveAttackGoal extends MeleeAttackGoal {

    private final PathfinderMob mob;
    private int raiseArmTicks;

    public AggressiveAttackGoal(PathfinderMob mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(mob, speedModifier, followingTargetEvenIfNotSeen);
        this.mob = mob;
    }

    @Override
    public void start() {
        super.start();
        this.raiseArmTicks = 0;
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.setAggressive(false);
    }

    @Override
    public void tick() {
        super.tick();
        this.raiseArmTicks++;
        if (this.raiseArmTicks >= 5 && this.getTicksUntilNextAttack() < this.getAttackInterval() / 2) {
            this.mob.setAggressive(true);
        } else {
            this.mob.setAggressive(false);
        }
    }
}