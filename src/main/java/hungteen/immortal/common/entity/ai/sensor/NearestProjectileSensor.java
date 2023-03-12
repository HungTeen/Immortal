package hungteen.immortal.common.entity.ai.sensor;

import com.google.common.collect.ImmutableSet;
import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.immortal.common.entity.ai.ImmortalMemories;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.AABB;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-24 22:13
 **/
public class NearestProjectileSensor<T extends Mob> extends Sensor<T> {

    public NearestProjectileSensor(){
        super(5);
    }

    @Override
    protected void doTick(ServerLevel serverLevel, T owner) {
        final AABB aabb = owner.getBoundingBox().inflate(10, 8, 10);
        List<Projectile> list = EntityHelper.getPredicateEntities(
                owner, aabb, Projectile.class, target -> isProjectile(target) && owner.getSensing().hasLineOfSight(target)
        );
        list.sort(Comparator.comparingDouble(owner::distanceToSqr));
        owner.getBrain().setMemory(ImmortalMemories.NEAREST_PROJECTILE.get(), list.isEmpty() ? null : list.get(0));
    }

    public static boolean isProjectile(Entity target) {
        return target instanceof Projectile && ! target.isOnGround();
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(ImmortalMemories.NEAREST_PROJECTILE.get());
    }
}
