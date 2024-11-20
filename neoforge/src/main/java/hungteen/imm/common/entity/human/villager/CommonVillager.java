package hungteen.imm.common.entity.human.villager;

import hungteen.imm.api.registry.ICultivationType;
import hungteen.imm.common.entity.human.HumanEntity;
import hungteen.imm.common.impl.registry.CultivationTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-17 12:03
 **/
public class CommonVillager extends IMMVillager {

    public CommonVillager(EntityType<? extends HumanEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public ICultivationType getCultivationType() {
        return CultivationTypes.SPIRITUAL;
    }
}
