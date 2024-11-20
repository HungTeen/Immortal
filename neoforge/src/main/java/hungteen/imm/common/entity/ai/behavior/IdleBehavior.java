package hungteen.imm.common.entity.ai.behavior;

import hungteen.imm.common.entity.IMMMob;
import hungteen.imm.common.entity.ai.IMMMemories;
import hungteen.imm.common.entity.creature.monster.BiFang;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Map;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/10/17 20:16
 **/
public class IdleBehavior extends Behavior<IMMMob> {

    private final IMMMob.AnimationTypes animationType;
    private final IntProvider cooldown;

    public IdleBehavior(IMMMob.AnimationTypes animationType, int cd, IntProvider cooldown) {
        super(Map.of(
                IMMMemories.IDLE_COOLING_DOWN.get(), MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT
        ), cd);
        this.animationType = animationType;
        this.cooldown = cooldown;
    }

    @Override
    protected void start(ServerLevel serverLevel, IMMMob mob, long time) {
    }

    @Override
    protected boolean canStillUse(ServerLevel serverLevel, IMMMob mob, long time) {
        return true;
    }

    @Override
    protected void tick(ServerLevel level, IMMMob mob, long time) {
        if(mob.isIdle() && mob.onGround()){
            mob.setCurrentAnimation(animationType);
        }
        if(mob.getMoveControl() instanceof BiFang.BiFangFlyMoveControl control){
            control.setIdle();
        }
    }

    @Override
    protected void stop(ServerLevel serverLevel, IMMMob mob, long time) {
        mob.setIdle();
        mob.getBrain().setMemoryWithExpiry(IMMMemories.IDLE_COOLING_DOWN.get(), true, cooldown.sample(mob.getRandom()));
    }
}
