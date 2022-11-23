package hungteen.immortal.common.entity.formation;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-18 22:55
 **/
public class BorderFormation extends Formation{

    public BorderFormation(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Override
    public double getMinY() {
        return this.level.getMinBuildHeight();
    }

    @Override
    public double getMaxY() {
        return this.level.getMaxBuildHeight();
    }
}
