package hungteen.immortal.common.item.artifacts;

import net.minecraft.tags.BlockTags;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-19 22:31
 **/
public class AxeItem extends DiggerItem {

    public AxeItem(int artifactLevel, float attackDamage, float digSpeed) {
        this(artifactLevel, false, attackDamage, 0, 0, digSpeed);
    }

    public AxeItem(int artifactLevel, boolean isAncientArtifact, float attackDamage, float attackSpeed, float attackRange, float digSpeed) {
        super(MeleeAttackTypes.AXE, artifactLevel, isAncientArtifact, attackDamage, attackSpeed, attackRange, digSpeed, BlockTags.MINEABLE_WITH_AXE);
    }

}
