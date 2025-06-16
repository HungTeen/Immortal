package hungteen.imm.common.item.artifact;

import hungteen.imm.api.artifact.ArtifactTier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-19 22:31
 **/
public class SwordItem extends MeleeAttackItem {

    public SwordItem(ArtifactTier tier) {
        super(MeleeAttackTypes.SWORD, tier);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return state.is(Blocks.COBWEB);
    }

}
