package hungteen.imm.common.item.artifact;

import hungteen.imm.api.artifact.ArtifactTier;
import net.minecraft.tags.BlockTags;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-19 22:31
 **/
public class ShovelItem extends DiggerItem {

    public ShovelItem(ArtifactTier tier, float digSpeed) {
        super(MeleeAttackTypes.SHOVEL, tier, digSpeed, BlockTags.MINEABLE_WITH_SHOVEL);
    }

}
