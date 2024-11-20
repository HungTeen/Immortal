package hungteen.imm.common.block.plants;

import hungteen.htlib.util.WeightedList;
import hungteen.htlib.util.helper.impl.BlockHelper;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.block.IMMStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ScaffoldingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.Nullable;

/**
 * @author PangTeen
 */
public class GourdScaffoldBlock extends ScaffoldingBlock {

    public static final DirectionProperty ORIGIN_FACING = IMMStateProperties.ORIGIN_FACING;
    public static final DirectionProperty TARGET_FACING = IMMStateProperties.TARGET_FACING;
    public static final IntegerProperty REACH_DISTANCE = IMMStateProperties.REACH_DISTANCE;
    private static final int MAX_REACH_DISTANCE = 12;

    public GourdScaffoldBlock() {
        super(Properties.ofFullCopy(Blocks.SCAFFOLDING).randomTicks());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(DISTANCE, 7)
                .setValue(WATERLOGGED, Boolean.FALSE)
                .setValue(BOTTOM, Boolean.FALSE)
                .setValue(ORIGIN_FACING, Direction.DOWN)
                .setValue(TARGET_FACING, Direction.DOWN)
                .setValue(REACH_DISTANCE, 0)
        );
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource source) {
        super.randomTick(state, level, pos, source);
        if (level.isAreaLoaded(pos, 1)) {
            if (level.getRawBrightness(pos, 0) >= 9) {
                final int reach = state.getValue(REACH_DISTANCE);
                if (source.nextFloat() < 0.5) {
                    // 没有延伸过。
                    if (!isValidReach(level, state, pos) && reach < MAX_REACH_DISTANCE) {
                        for (Direction direction : Direction.Plane.HORIZONTAL) {
                            if (tryReach(level, state, pos, direction)) {
                                return;
                            }
                        }
                        for (Direction direction : Direction.Plane.VERTICAL) {
                            if (tryReach(level, state, pos, direction)) {
                                return;
                            }
                        }
                    }
                } else {
                    // 结果。
                    final BlockState belowState = level.getBlockState(pos.below());
                    if (belowState.isAir()) {
                        GourdGrownBlock resultFruit = this.getResultFruit(source);
                        level.setBlockAndUpdate(pos.below(), resultFruit.defaultBlockState().setValue(GourdGrownBlock.HANGING, true));
                    }
                }
            }
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource source) {
        super.tick(state, level, pos, source);
        final int reach = getSourceReach(level, state, pos);
        if (reach >= 0) {
            level.setBlockAndUpdate(pos, BlockHelper.setProperty(state, REACH_DISTANCE, reach + 1));
        } else {
            // 带藤脚手架 -> 脚手架。
            level.setBlockAndUpdate(pos, copyScaffoldState(state, Blocks.SCAFFOLDING.defaultBlockState()));
        }
    }

    public boolean tryReach(ServerLevel level, BlockState state, BlockPos pos, Direction direction) {
        if (direction != state.getValue(ORIGIN_FACING)) {
            final BlockPos targetPos = pos.relative(direction);
            final BlockState targetState = level.getBlockState(targetPos);
            // 脚手架 -> 带藤脚手架。
            if (targetState.is(Blocks.SCAFFOLDING)) {
                BlockState newState = copyScaffoldState(targetState, IMMBlocks.GOURD_SCAFFOLD.get().defaultBlockState());
                level.setBlockAndUpdate(pos, BlockHelper.setProperty(state, TARGET_FACING, direction));
                level.setBlockAndUpdate(targetPos, BlockHelper.setProperty(newState, ORIGIN_FACING, direction.getOpposite()));
            }
        }
        return false;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
        return super.canSurvive(state, reader, pos);
    }

    @Override
    public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility ability, boolean simulate) {
        if(ability == ItemAbilities.AXE_STRIP){
            return copyScaffoldState(state, Blocks.SCAFFOLDING.defaultBlockState());
        }
        return super.getToolModifiedState(state, context, ability, simulate);
    }

    public int getSourceReach(LevelReader reader, BlockState state, BlockPos pos) {
        Direction originFacing = getOriginFacing(state);
        BlockPos originPos = pos.relative(originFacing);
        BlockState sourceState = reader.getBlockState(originPos);
        // 葫芦藤和它正好相对，则是正确的源。
        if (sourceState.getBlock() instanceof AttachedGourdStemBlock attachedStem && attachedStem.getFacing(sourceState).getOpposite() == originFacing) {
            return 0;
        } else if (sourceState.getBlock() instanceof GourdScaffoldBlock gourdScaffold && gourdScaffold.getTargetFacing(sourceState).getOpposite() == originFacing) {
            return sourceState.getValue(REACH_DISTANCE);
        }
        return -1;
    }

    public static BlockState copyScaffoldState(BlockState originState, BlockState targetState) {
        return targetState.setValue(DISTANCE, originState.getValue(DISTANCE))
                .setValue(WATERLOGGED, originState.getValue(WATERLOGGED))
                .setValue(BOTTOM, originState.getValue(BOTTOM));
    }

    public boolean isValidReach(LevelReader reader, BlockState state, BlockPos pos) {
        Direction targetFacing = getTargetFacing(state);
        BlockPos targetPos = pos.relative(targetFacing);
        BlockState targetState = reader.getBlockState(targetPos);
        return targetState.getBlock() instanceof GourdScaffoldBlock gourdScaffold && gourdScaffold.getOriginFacing(targetState).getOpposite() == targetFacing;
    }

    public Direction getOriginFacing(BlockState blockState) {
        return blockState.getValue(ORIGIN_FACING);
    }

    public Direction getTargetFacing(BlockState blockState) {
        return blockState.getValue(TARGET_FACING);
    }

    protected GourdGrownBlock getResultFruit(RandomSource random) {
        return WeightedList.create(GourdGrownBlock.GourdTypes.values()).getRandomItem(random).map(GourdGrownBlock.GourdTypes::getGourdGrownBlock).orElseThrow();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DISTANCE, WATERLOGGED, BOTTOM, ORIGIN_FACING, TARGET_FACING, REACH_DISTANCE);
    }

    @Override
    public boolean isScaffolding(BlockState state, LevelReader level, BlockPos pos, LivingEntity entity) {
        return state.is(this);
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 60;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 60;
    }
}
