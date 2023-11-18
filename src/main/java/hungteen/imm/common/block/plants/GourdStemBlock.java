package hungteen.imm.common.block.plants;

import hungteen.htlib.common.block.plants.HTStemBlock;
import hungteen.htlib.util.helper.registry.BlockHelper;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.item.IMMItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-06 22:31
 **/
public class GourdStemBlock extends HTStemBlock {

    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    protected static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(7.0D, 0.0D, 7.0D, 9.0D, 5.0D, 9.0D),
            Block.box(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D),
            Block.box(3.0D, 0.0D, 3.0D, 13.0D, 14.0D, 13.0D),
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D),
    };

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
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[state.getValue(AGE)];
    }

    @Override
    public int getMaxAge() {
        return 3;
    }

}
