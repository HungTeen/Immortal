package hungteen.imm.common.entity.ai.behavior.golem;

import com.google.common.collect.ImmutableMap;
import hungteen.imm.common.entity.golem.GolemEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.RandomStroll;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-09 15:49
 **/
public class GolemRandomStroll extends GolemOneShotBehavior{

    public GolemRandomStroll(ItemStack stack) {
        super(stack, ImmutableMap.of(
                MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT
        ));
    }

    @Override
    protected void start(ServerLevel level, GolemEntity golemEntity, long tick) {
        Vec3 destination = LandRandomPos.getPos(golemEntity, 10, 7);
        //TODO 飞行或游泳
        if(false && golemEntity.isInWaterOrBubble()){
            destination = RandomStroll.getTargetSwimPos(golemEntity);
        }
        if(false){
            destination = RandomStroll.getTargetFlyPos(golemEntity, 10, 7);
        }
        golemEntity.getBrain().setMemory(MemoryModuleType.WALK_TARGET, Optional.ofNullable(destination).map(
                l -> new WalkTarget(l, 1F, 0)
        ));
    }
}
