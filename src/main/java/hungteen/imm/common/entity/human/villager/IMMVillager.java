package hungteen.imm.common.entity.human.villager;

import hungteen.imm.common.entity.human.HumanEntity;
import hungteen.imm.common.entity.human.VillagerLikeEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-24 23:16
 **/
public abstract class IMMVillager extends VillagerLikeEntity {

    public IMMVillager(EntityType<? extends HumanEntity> type, Level level) {
        super(type, level);
    }

}
