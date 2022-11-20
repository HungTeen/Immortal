package hungteen.immortal.common.entity.formation;

import hungteen.htlib.entity.HTEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-18 22:53
 **/
public abstract class Formation extends HTEntity {

    public Formation(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        double d0 = this.getBoundingBox().getSize() * 4.0D;
        if (Double.isNaN(d0)) {
            d0 = 4.0D;
        }

        d0 *= 128.0D;
        return distance < d0 * d0;
    }

}
