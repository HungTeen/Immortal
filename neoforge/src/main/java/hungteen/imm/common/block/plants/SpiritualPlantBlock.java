package hungteen.imm.common.block.plants;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BushBlock;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/13 16:35
 */
public class SpiritualPlantBlock extends BushBlock {

    public static final MapCodec<SpiritualPlantBlock> CODEC = simpleCodec(SpiritualPlantBlock::new);

    public SpiritualPlantBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

}
