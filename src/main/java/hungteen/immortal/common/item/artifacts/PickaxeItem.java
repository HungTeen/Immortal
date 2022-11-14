package hungteen.immortal.common.item.artifacts;

import hungteen.immortal.api.registry.IArtifactTier;
import net.minecraft.tags.BlockTags;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-19 22:31
 **/
public class PickaxeItem extends DiggerItem {

    public PickaxeItem(boolean isAncientArtifact, IArtifactTier tier, float digSpeed) {
        super(MeleeAttackTypes.PICKAXE, isAncientArtifact, tier, digSpeed, BlockTags.MINEABLE_WITH_PICKAXE);
    }

}
