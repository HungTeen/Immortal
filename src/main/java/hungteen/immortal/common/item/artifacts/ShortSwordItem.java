package hungteen.immortal.common.item.artifacts;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-19 22:31
 **/
public class ShortSwordItem extends MeleeAttackItem {

    public ShortSwordItem(int artifactLevel, float attackDamage) {
        this(artifactLevel, false, attackDamage, 0, 0);
    }

    public ShortSwordItem(int artifactLevel, boolean isAncientArtifact, float attackDamage, float attackSpeed, float attackRange) {
        super(MeleeAttackTypes.SHORT_SWORD, artifactLevel, isAncientArtifact, attackDamage, attackSpeed, attackRange);
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
