package hungteen.imm.common.entity.ai.sensor;

import com.google.common.collect.ImmutableSet;
import hungteen.imm.common.entity.ai.IMMMemories;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.phys.AABB;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-24 22:13
 **/
public class NearestBoatSensor<T extends Mob> extends Sensor<T> {

    public NearestBoatSensor(){
        super(30);
    }

    @Override
    protected void doTick(ServerLevel serverLevel, T owner) {
        Entity boat = null;
        if(isBoat(owner.getVehicle())){
            boat = owner.getVehicle();
        } else{
            AABB aabb = owner.getBoundingBox().inflate(8, 4, 8);

            List<Entity> list = serverLevel.getEntitiesOfClass(Entity.class, aabb, (target) -> {
                return target != owner && target.isAlive() && isBoat(target) && owner.getSensing().hasLineOfSight(target);
            });
            list.sort(Comparator.comparingDouble(owner::distanceToSqr));
            boat = list.isEmpty() ? null : list.get(0);
        }
        Brain<?> brain = owner.getBrain();
        brain.setMemory(IMMMemories.NEAREST_BOAT.get(), boat);
    }

    public static boolean isBoat(Entity target) {
        //TODO Boat Tag In HTLib.
        return target instanceof Boat;
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(IMMMemories.NEAREST_BOAT.get());
    }
}
