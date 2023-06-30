package hungteen.imm.common.world.levelgen.features.feature;

import com.mojang.serialization.Codec;
import hungteen.imm.common.world.levelgen.features.configuration.HorizontalStakeConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.TreeFeature;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-06-30 21:49
 **/
public class HorizontalStakeFeature extends Feature<HorizontalStakeConfiguration> {

    public HorizontalStakeFeature(Codec<HorizontalStakeConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<HorizontalStakeConfiguration> context) {
        final int length = context.config().length().sample(context.random());
        final int width = context.config().width().sample(context.random());
        final int mid = width >> 1;
        final boolean x = context.random().nextFloat() < 0.5F;
        final Direction.Axis axis = x ? Direction.Axis.X : Direction.Axis.Z;
        List<BlockPos> positions = new ArrayList<>();
        for (int i = 1; i <= width; ++ i){
            final int height = i > mid ? width - i + 1 : i;
            for(int j = 0; j < height; ++ j){
                for(int k = 0; k < length; ++ k){
                    final int dl = k - length / 2;
                    final int dw = i - mid;
                    final BlockPos place = context.origin().offset(x ? dl : dw, j, x ? dw : dl).immutable();
                    positions.add(place);
                }
            }
        }
        List<BlockPos> base = positions.stream().filter(pos -> pos.getY() == context.origin().getY()).toList();
        final long solidCount = base.stream().filter(pos -> context.level().getBlockState(pos.below()).getMaterial().isSolid()).count();
        if(solidCount > base.size() * 0.9){
            positions.forEach(pos -> {
                final BlockState logState = context.config().logState().getState(context.random(), pos).setValue(RotatedPillarBlock.AXIS, axis);
                placeLog(context.level(), pos, logState);
            });
            return true;
        }
        return false;
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
