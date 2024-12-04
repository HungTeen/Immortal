package hungteen.imm.common.item.artifacts;

import hungteen.imm.api.artifact.IArtifactTier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-19 22:31
 **/
public class ShortSwordItem extends MeleeAttackItem {

    public ShortSwordItem(IArtifactTier tier) {
        super(MeleeAttackTypes.SHORT_SWORD, tier);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return state.is(Blocks.COBWEB);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility toolAction) {
        return ItemAbilities.DEFAULT_SWORD_ACTIONS.contains(toolAction);
    }

}
