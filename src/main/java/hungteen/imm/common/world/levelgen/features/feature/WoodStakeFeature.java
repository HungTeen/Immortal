package hungteen.imm.common.world.levelgen.features.feature;

import com.mojang.serialization.Codec;
import hungteen.imm.common.world.levelgen.features.configuration.WoodStakeConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.TreeFeature;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-06-30 21:49
 **/
public class WoodStakeFeature extends Feature<WoodStakeConfiguration> {

    public WoodStakeFeature(Codec<WoodStakeConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<WoodStakeConfiguration> context) {
        final int height = context.config().height().sample(context.random());
        for (int i = 0; i < height; ++ i){
            final BlockPos place = context.origin().above(i).immutable();
            if(! placeLog(context.level(), place, context.config().logState().getState(context.random(), place))){
                break;
            }
        }
        return true;
    }

    protected boolean placeLog(WorldGenLevel level, BlockPos pos, BlockState logBlock) {
        if (TreeFeature.validTreePos(level, pos)) {
            level.setBlock(pos, logBlock, 19);
            return true;
        } else {
            return false;
        }
    }

}
