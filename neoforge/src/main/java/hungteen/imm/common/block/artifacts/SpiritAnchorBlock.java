package hungteen.imm.common.block.artifacts;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HalfTransparentBlock;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/25 9:45
 **/
public class SpiritAnchorBlock extends HalfTransparentBlock {

    public SpiritAnchorBlock() {
        super(Properties.ofFullCopy(Blocks.BEDROCK)
                .lightLevel(state -> 15)
                .noOcclusion()
                .noLootTable()
        );
    }

}
