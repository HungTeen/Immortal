package hungteen.immortal.common.entity.golem;

import hungteen.immortal.api.registry.ISpiritualRoot;
import hungteen.immortal.impl.SpiritualRoots;
import net.minecraft.world.Container;
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
    public Collection<ISpiritualRoot> getSpiritualRoots() {
        return List.of(SpiritualRoots.METAL);
    }

}
