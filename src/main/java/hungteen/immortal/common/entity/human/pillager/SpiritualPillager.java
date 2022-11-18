package hungteen.immortal.common.entity.human.pillager;

import hungteen.immortal.common.entity.human.HumanEntity;
import hungteen.immortal.common.entity.human.VillagerLikeEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-17 10:50
 **/
public abstract class SpiritualPillager extends VillagerLikeEntity {

    public SpiritualPillager(EntityType<? extends HumanEntity> type, Level level) {
        super(type, level);
    }
}
