package hungteen.immortal.common.item.artifacts;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-19 22:31
 **/
public class SwordItem extends MeleeAttackItem {

    public SwordItem(int artifactLevel, float attackDamage, float attackSpeed) {
        super(artifactLevel, attackDamage, attackSpeed);
    }

    public SwordItem(int artifactLevel, boolean isAncientArtifact, float attackDamage, float attackSpeed) {
        super(artifactLevel, isAncientArtifact, attackDamage, attackSpeed);
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState state) {
        return state.is(Blocks.COBWEB);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return net.minecraftforge.common.ToolActions.DEFAULT_SWORD_ACTIONS.contains(toolAction);
    }

}
