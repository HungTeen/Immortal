package hungteen.imm.common.item.artifacts;

import hungteen.imm.api.interfaces.IArtifactTier;
import net.minecraft.tags.BlockTags;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-19 22:31
 **/
public class HoeItem extends DiggerItem {

    public HoeItem(IArtifactTier tier, float digSpeed) {
        super(MeleeAttackTypes.HOE, tier, digSpeed, BlockTags.MINEABLE_WITH_HOE);
    }

}
