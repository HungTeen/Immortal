package hungteen.immortal.common.item.artifacts;

import hungteen.immortal.api.registry.IArtifactTier;
import net.minecraft.tags.BlockTags;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-19 22:31
 **/
public class HoeItem extends DiggerItem {

    public HoeItem(boolean isAncientArtifact, IArtifactTier tier, float digSpeed) {
        super(MeleeAttackTypes.HOE, isAncientArtifact, tier, digSpeed, BlockTags.MINEABLE_WITH_HOE);
    }

}
