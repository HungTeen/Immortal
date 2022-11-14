package hungteen.immortal.common.item.artifacts;

import hungteen.immortal.api.registry.IArtifactTier;
import net.minecraft.tags.BlockTags;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-19 22:31
 **/
public class ShovelItem extends DiggerItem {

    public ShovelItem(boolean isAncientArtifact, IArtifactTier tier, float digSpeed) {
        super(MeleeAttackTypes.SHOVEL, isAncientArtifact, tier, digSpeed, BlockTags.MINEABLE_WITH_SHOVEL);
    }

}
