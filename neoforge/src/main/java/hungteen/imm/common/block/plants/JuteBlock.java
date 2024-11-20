package hungteen.imm.common.block.plants;

import hungteen.imm.common.item.IMMItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/5/31 10:22
 */
public class JuteBlock extends IMMCropBlock {

    private static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    private static final IntegerProperty AGE = BlockStateProperties.AGE_7;
    private static final int MAX_AGE = 7;

    private static final VoxelShape[] LOWER_SHAPE = new VoxelShape[]{
            Block.box(4.0D, 0.0D, 4.0D, 12.0D, 8.0D, 12.0D),
            Block.box(4.0D, 0.0D, 4.0D, 12.0D, 14.0D, 12.0D),
            Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D),
            Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D)
    };
    private static final VoxelShape[] UPPER_SHAPE = new VoxelShape[]{
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 0.0D, 14.0D),
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 0.0D, 14.0D),
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 8.0D, 14.0D),
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 14.0D, 14.0D)
    };

    public JuteBlock() {
        super(Properties.ofFullCopy(Blocks.SUGAR_CANE));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(AGE, 0)
                .setValue(HALF, DoubleBlockHalf.LOWER)
        );
    }

    @Override
    public boolean canNaturalGrow(ServerLevel level, BlockState state, BlockPos pos) {
        return !isUpperState(state) && super.canNaturalGrow(level, state, pos);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter getter, BlockPos pos) {
        return state.is(BlockTags.DIRT) || state.is(Blocks.FARMLAND);
    }

    /**
     * Modify from {@link net.minecraft.world.level.block.DoublePlantBlock}.
     */
    @Override
    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
        if (!isUpperState(state)) {
            return super.canSurvive(state, reader, pos);
        } else {
            BlockState blockstate = reader.getBlockState(pos.below());
            if (state.getBlock() != this) {
                return super.canSurvive(state, reader, pos); //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
            }
            return blockstate.is(this) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER;
        }
    }

    /**
     * Copy from {@link net.minecraft.world.level.block.DoublePlantBlock}.
     */
    @Override
    public BlockState updateShape(BlockState selfState, Direction direction, BlockState blockState, LevelAccessor levelAccessor, BlockPos selfPos, BlockPos blockPos) {
        DoubleBlockHalf blockHalf = selfState.getValue(HALF);
        if (direction.getAxis() != Direction.Axis.Y || blockHalf == DoubleBlockHalf.LOWER != (direction == Direction.UP) || blockState.is(this) && blockState.getValue(HALF) != blockHalf) {
            return blockHalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !selfState.canSurvive(levelAccessor, selfPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(selfState, direction, blockState, levelAccessor, selfPos, blockPos);
        } else {
            return Blocks.AIR.defaultBlockState();
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        Level level = context.getLevel();
        return blockpos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockpos.above()).canBeReplaced(context) ? super.getStateForPlacement(context) : null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState state, LivingEntity entity, ItemStack itemStack) {
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos blockPos, BlockState state, Player player) {
        if (!level.isClientSide) {
            if (player.isCreative()) {
                preventCreativeDropFromBottomPart(level, blockPos, state, player);
            } else {
                dropResources(state, level, blockPos, null, player, player.getMainHandItem());
            }
        }

        return super.playerWillDestroy(level, blockPos, state, player);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        super.playerDestroy(level, player, blockPos, Blocks.AIR.defaultBlockState(), blockEntity, stack);
    }

    protected static void preventCreativeDropFromBottomPart(Level level, BlockPos blockPos, BlockState state, Player player) {
        DoubleBlockHalf doubleblockhalf = state.getValue(HALF);
        if (doubleblockhalf == DoubleBlockHalf.UPPER) {
            BlockPos blockpos = blockPos.below();
            BlockState blockstate = level.getBlockState(blockpos);
            if (blockstate.is(state.getBlock()) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER) {
                BlockState blockstate1 = blockstate.hasProperty(BlockStateProperties.WATERLOGGED) && blockstate.getValue(BlockStateProperties.WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
                level.setBlock(blockpos, blockstate1, 35);
                level.levelEvent(player, 2001, blockpos, Block.getId(blockstate));
            }
        }

    }

    public static void placeAt(LevelAccessor levelAccessor, BlockState blockState, BlockPos blockPos, int flag) {
        BlockPos blockpos = blockPos.above();
        levelAccessor.setBlock(blockPos, copyWaterloggedFrom(levelAccessor, blockPos, blockState.setValue(HALF, DoubleBlockHalf.LOWER)), flag);
        levelAccessor.setBlock(blockpos, copyWaterloggedFrom(levelAccessor, blockpos, blockState.setValue(HALF, DoubleBlockHalf.UPPER)), flag);
    }

    public static BlockState copyWaterloggedFrom(LevelReader reader, BlockPos blockPos, BlockState blockState) {
        return blockState.hasProperty(BlockStateProperties.WATERLOGGED) ? blockState.setValue(BlockStateProperties.WATERLOGGED, reader.isWaterAt(blockPos)) : blockState;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader reader, BlockPos blockPos, BlockState blockState) {
        return !isUpperState(blockState) && super.isValidBonemealTarget(reader, blockPos, blockState);
    }

    @Override
    protected void onGrow(Level level, BlockState state, BlockPos pos, int age) {
        if (!isUpperState(state)) {
            level.setBlock(pos, this.getStateForAge(age, false), 2);
            if (canGrowUpper(level.getBlockState(pos))) {
                level.setBlock(pos.above(), this.getStateForAge(age, true), 2);
            }
        }
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return MAX_AGE;
    }

    @Override
    public VoxelShape[] getShapes() {
        return LOWER_SHAPE;
    }

    public static boolean isUpperState(BlockState state) {
        return state.getValue(HALF) == DoubleBlockHalf.UPPER;
    }

    protected boolean canGrowUpper(BlockState state) {
        return getStateIndex(state) >= 2;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, HALF);
    }

    /**
     * Not every age will change the state and texture.
     */
    @Override
    public int getStateIndex(BlockState state){
        return getAge(state) / 2;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        final int age = this.getStateIndex(state);
        return isUpperState(state) ? UPPER_SHAPE[age] : LOWER_SHAPE[age];
    }

    public BlockState getStateForAge(int age, boolean upper) {
        return this.defaultBlockState()
                .setValue(this.getAgeProperty(), age)
                .setValue(HALF, upper ? DoubleBlockHalf.UPPER : DoubleBlockHalf.LOWER);
    }

    @Override
    public ItemLike getSeedItem(BlockState state) {
        return IMMItems.JUTE_SEEDS.get();
    }

}
