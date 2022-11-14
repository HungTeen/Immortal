package hungteen.immortal.common.item.artifacts;

import hungteen.immortal.api.registry.IArtifactTier;
import net.minecraft.tags.BlockTags;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-19 22:31
 **/
public class AxeItem extends DiggerItem {

    public AxeItem(boolean isAncientArtifact, IArtifactTier tier, float digSpeed) {
        super(MeleeAttackTypes.AXE, isAncientArtifact, tier, digSpeed, BlockTags.MINEABLE_WITH_AXE);
    }

}
