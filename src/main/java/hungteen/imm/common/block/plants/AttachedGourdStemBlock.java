package hungteen.imm.common.block.plants;

import hungteen.htlib.common.block.plants.HTAttachedStemBlock;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.item.IMMItems;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-07 10:52
 **/
public class AttachedGourdStemBlock extends HTAttachedStemBlock {

    public AttachedGourdStemBlock() {
        super(() -> IMMItems.GOURD_SEEDS.get(), BlockBehaviour.Properties.copy(Blocks.ATTACHED_MELON_STEM));
    }

    @Override
    protected boolean isGrownFruit(BlockState selfState, BlockState nearbyState) {
        return nearbyState.getBlock() instanceof GourdScaffoldBlock gourdScaffold && gourdScaffold.getOriginFacing(nearbyState).getOpposite() == getFacing(selfState);
    }

    public Direction getFacing(BlockState blockState) {
        return blockState.getValue(FACING);
    }

    @Override
    protected BlockState getStemState() {
        return IMMBlocks.GOURD_STEM.get().maxAgeState();
    }
}
