package hungteen.imm.common.entity.human.villager;

import hungteen.imm.common.entity.human.HumanLikeEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-11-17 12:03
 **/
public class CommonVillager extends IMMVillager {

    public CommonVillager(EntityType<? extends HumanLikeEntity> type, Level level) {
        super(type, level);
    }

}
