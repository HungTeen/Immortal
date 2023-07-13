package hungteen.imm.common.block.plants;

import hungteen.htlib.util.helper.MathHelper;
import hungteen.htlib.util.helper.registry.ParticleHelper;
import hungteen.imm.client.particle.IMMParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/13 16:35
 */
public class SpiritualPlantBlock extends BushBlock {

    public SpiritualPlantBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource source) {
        for(int i = 0; i < 1; ++i) {
            if (source.nextBoolean()) {
                ParticleHelper.spawnRandomSpeedParticle(level, IMMParticles.SPIRIT.get(), MathHelper.toVec3(pos),0.06F);
            }
        }
    }
}
