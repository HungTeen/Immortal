package hungteen.imm.common.block.artifacts;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-09 09:15
 **/
public class SpiritualFurnace {
//        extends ArtifactEntityBlock {
//
//    private static final VoxelShape AABB = Block.box(1, 0, 1, 15, 16, 15);
//    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
//    public static final BooleanProperty LIT = BlockStateProperties.LIT;
//
//    public SpiritualFurnace(IArtifactType artifactType) {
//        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).requiresCorrectToolForDrops().lightLevel(state -> {
//            return state.getValue(LIT) ? 15 : 0;
//        }), artifactType);
//        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.FALSE).setValue(FACING, Direction.NORTH));
//    }
//
//    @Override
//    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult) {
//        if (level.isClientSide) {
//            return InteractionResult.SUCCESS;
//        } else {
//            if(player instanceof ServerPlayer){
//                NetworkHooks.openScreen((ServerPlayer)player, getMenuProvider(blockState, level, blockPos), buf -> {
//                    buf.writeBlockPos(blockPos);
//                });
//            }
//            return InteractionResult.CONSUME;
//        }
//    }
//
//    @Nullable
//    @Override
//    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState state) {
//        return new SpiritualFurnaceBlockEntity(blockPos, state);
//    }
//
//    @Nullable
//    @Override
//    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
//        return level.isClientSide ? null : createTickerHelper(blockEntityType, ImmortalBlockEntities.SPIRITUAL_FURNACE.get(), SpiritualFurnaceBlockEntity::serverTick);
//    }
//
//    public BlockState getStateForPlacement(BlockPlaceContext context) {
//        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
//    }
//
//    @Override
//    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
//        stateBuilder.add(FACING, LIT);
//    }
//
//    public BlockState rotate(BlockState blockState, Rotation rotation) {
//        return blockState.setValue(FACING, rotation.rotate((Direction)blockState.getValue(FACING)));
//    }
//
//    public BlockState mirror(BlockState blockState, Mirror mirror) {
//        return blockState.rotate(mirror.getRotation((Direction)blockState.getValue(FACING)));
//    }
//
//    @Override
//    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos blockPos, CollisionContext context) {
//        return AABB;
//    }
}
