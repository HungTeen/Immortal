package hungteen.immortal.common.item.artifacts;

import hungteen.immortal.api.registry.IArtifactTier;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-19 22:31
 **/
public class SwordItem extends MeleeAttackItem {

    public SwordItem(boolean isAncientArtifact, IArtifactTier tier) {
        super(MeleeAttackTypes.SWORD, isAncientArtifact, tier);
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState state) {
        return state.is(Blocks.COBWEB);
    }

}
