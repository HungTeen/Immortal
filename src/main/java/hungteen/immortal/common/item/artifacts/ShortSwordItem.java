package hungteen.immortal.common.item.artifacts;

import hungteen.immortal.api.interfaces.IArtifactTier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-19 22:31
 **/
public class ShortSwordItem extends MeleeAttackItem {

    public ShortSwordItem(IArtifactTier tier) {
        super(MeleeAttackTypes.SHORT_SWORD, tier);
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
