package hungteen.imm.common.block.plants;

import hungteen.htlib.common.block.plants.HTStemBlock;
import hungteen.htlib.util.helper.registry.BlockHelper;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.item.IMMItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-06 22:31
 **/
public class GourdStemBlock extends HTStemBlock {

    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;

    public GourdStemBlock() {
        super(() -> IMMItems.GOURD_SEEDS.get(), BlockBehaviour.Properties.copy(Blocks.MELON_STEM));
    }

    @Override
    public void grow(ServerLevel serverLevel, BlockState blockState, BlockPos blockPos, RandomSource random) {
        for (Direction direction : Direction.values()) {
            final BlockPos targetPos = blockPos.relative(direction);
            final BlockState targetState = serverLevel.getBlockState(targetPos);
            // 脚手架 -> 带藤脚手架。
            if(targetState.is(Blocks.SCAFFOLDING)){
                BlockState newState = GourdScaffoldBlock.copyScaffoldState(targetState, IMMBlocks.GOURD_SCAFFOLD.get().defaultBlockState());
                serverLevel.setBlockAndUpdate(targetPos, BlockHelper.setProperty(newState, GourdScaffoldBlock.ORIGIN_FACING, direction.getOpposite()));
                serverLevel.setBlockAndUpdate(blockPos, BlockHelper.setProperty(IMMBlocks.GOURD_ATTACHED_STEM.get().defaultBlockState(), HorizontalDirectionalBlock.FACING, direction));
                return;
            }
        }
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 7;
    }

}
