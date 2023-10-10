package hungteen.imm.common.entity.creature.spirit;

import hungteen.imm.api.enums.Elements;
import hungteen.imm.api.registry.ISpiritualType;
import hungteen.imm.common.entity.IMMMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import java.util.Collection;
import java.util.List;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/10/8 20:59
 **/
public abstract class ElementSpirit extends IMMMob {

    public ElementSpirit(EntityType<? extends IMMMob> type, Level level) {
        super(type, level);
    }

    @Override
    protected Collection<ISpiritualType> createSpiritualRoots(ServerLevelAccessor accessor) {
        return List.of(getSpiritualRoot());
    }

    public abstract ISpiritualType getSpiritualRoot();

    public abstract Elements getElement();

}
