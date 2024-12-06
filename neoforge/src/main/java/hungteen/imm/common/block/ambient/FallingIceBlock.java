package hungteen.imm.common.block.ambient;

import hungteen.htlib.common.block.HTBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/6 9:31
 **/
public class FallingIceBlock extends HTBlock {

    /**
     * 雪块和蓝冰的混合版。
     */
    public FallingIceBlock() {
        super(Properties.ofFullCopy(Blocks.SNOW_BLOCK).strength(2.8F).friction(0.989F).sound(SoundType.GLASS));
    }

    public FallingIceBlock(Properties properties) {
        super(properties);
    }

}
