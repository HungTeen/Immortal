package hungteen.imm.common.block;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-11-11 20:40
 **/
public class IMMSaplingBlock extends SaplingBlock {

    public IMMSaplingBlock(TreeGrower treeGrower) {
        super(treeGrower, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING));
    }

}
