package hungteen.imm.common.block.plants;

import hungteen.htlib.common.block.plants.HTAttachedStemBlock;
import hungteen.imm.common.item.ImmortalItems;
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
        super(() -> ImmortalItems.GOURD_SEEDS.get(), BlockBehaviour.Properties.copy(Blocks.ATTACHED_MELON_STEM));
    }

    @Override
    protected boolean isGrownFruit(BlockState blockState) {
        return blockState.getBlock() instanceof GourdGrownBlock;
    }

    @Override
    protected BlockState getStemState() {
        return GourdStemBlock.getFinalAge();
    }
}
