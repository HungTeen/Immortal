package hungteen.imm.common.item.artifact;

import hungteen.imm.api.artifact.IArtifactTier;
import net.minecraft.tags.BlockTags;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-19 22:31
 **/
public class AxeItem extends DiggerItem {

    public AxeItem(IArtifactTier tier, float digSpeed) {
        super(MeleeAttackTypes.AXE, tier, digSpeed, BlockTags.MINEABLE_WITH_AXE);
    }

}
