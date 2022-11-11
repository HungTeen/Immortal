package hungteen.immortal.common.item.artifacts;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-19 22:31
 **/
public class SwordItem extends MeleeAttackItem {

    public SwordItem(int artifactLevel, float attackDamage) {
        this(artifactLevel, false, attackDamage, 0, 0);
    }

    public SwordItem(int artifactLevel, boolean isAncientArtifact, float attackDamage, float attackSpeed, float attackRange) {
        super(MeleeAttackTypes.SWORD, artifactLevel, isAncientArtifact, attackDamage, attackSpeed, attackRange);
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState state) {
        return state.is(Blocks.COBWEB);
    }

}
