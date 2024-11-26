package hungteen.imm.common.block.ambient;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HalfTransparentBlock;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/23 16:15
 **/
public class SpiritBedrock extends HalfTransparentBlock {

    public SpiritBedrock() {
        super(Properties.ofFullCopy(Blocks.BEDROCK)
                .lightLevel(state -> 15)
                .noOcclusion()
                .noLootTable()
        );
    }

}
