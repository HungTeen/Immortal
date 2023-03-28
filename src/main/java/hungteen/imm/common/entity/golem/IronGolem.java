package hungteen.imm.common.entity.golem;

import hungteen.imm.api.registry.ICultivationType;
import hungteen.imm.api.registry.ISpiritualType;
import hungteen.imm.common.impl.CultivationTypes;
import hungteen.imm.common.impl.registry.SpiritualTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.Collection;
import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-24 12:39
 **/
public class IronGolem extends GolemEntity{
    public IronGolem(EntityType<? extends GolemEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public Collection<ISpiritualType> getSpiritualTypes() {
        return List.of(SpiritualTypes.METAL);
    }

    @Override
    public ICultivationType getCultivationType() {
        return CultivationTypes.SPIRITUAL;
    }
}
