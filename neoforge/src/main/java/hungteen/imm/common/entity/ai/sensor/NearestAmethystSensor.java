package hungteen.imm.common.entity.ai.sensor;

import com.google.common.collect.ImmutableSet;
import hungteen.imm.common.entity.ai.IMMMemories;
import hungteen.imm.common.entity.misc.ElementAmethyst;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.phys.AABB;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * @program Immortal
 * @author PangTeen
 * @create 2023/10/27 21:54
 **/
public class NearestAmethystSensor<T extends Mob> extends Sensor<T> {

    public NearestAmethystSensor(){
        super(40);
    }

    @Override
    protected void doTick(ServerLevel serverLevel, T owner) {
        AABB aabb = owner.getBoundingBox().inflate(12, 4, 12);
        List<ElementAmethyst> list = serverLevel.getEntitiesOfClass(ElementAmethyst.class, aabb, (target) -> {
            return owner.getSensing().hasLineOfSight(target);
        });
        list.sort(Comparator.comparingDouble(owner::distanceToSqr));
        owner.getBrain().setMemory(IMMMemories.ELEMENT_AMETHYST.get(), list.isEmpty() ? null : list.get(0));
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(IMMMemories.ELEMENT_AMETHYST.get());
    }
}
