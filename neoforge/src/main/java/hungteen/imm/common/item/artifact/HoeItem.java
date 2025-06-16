package hungteen.imm.common.item.artifact;

import hungteen.imm.api.artifact.ArtifactTier;
import net.minecraft.tags.BlockTags;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-19 22:31
 **/
public class HoeItem extends DiggerItem {

    public HoeItem(ArtifactTier tier, float digSpeed) {
        super(MeleeAttackTypes.HOE, tier, digSpeed, BlockTags.MINEABLE_WITH_HOE);
    }

}
