package hungteen.imm.common.block.artifacts;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-11-13 20:59
 **/
public class SpiritualRoom {
//        extends ArtifactEntityBlock {
//
//    private static final VoxelShape AABB = Block.box(1, 0, 1, 15, 6, 15);
//
//    public SpiritualRoom(IArtifactType artifactType) {
//        super(BlockBehaviour.Properties.copy(Blocks.ANVIL), artifactType);
//    }
//
//    @Override
//    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand
//    hand, BlockHitResult hitResult) {
//        if (level.isClientSide) {
//            return InteractionResult.SUCCESS;
//        } else {
//            if(player instanceof ServerPlayer){
//                if(level.getBlockEntity(blockPos.below()) instanceof SpiritualFurnaceBlockEntity){
//                    NetworkHooks.openScreen((ServerPlayer)player, getMenuProvider(blockState, level, blockPos), buf -> {
//                        buf.writeBlockPos(blockPos);
//                    });
//                } else{
//                    PlayerHelper.sendTipTo(player, TipUtil.ELIXIR_ROOM_TIP.withStyle(ChatFormatting.RED));
//                    return InteractionResult.PASS;
//                }
//            }
//            return InteractionResult.CONSUME;
//        }
//    }
//
//    @org.jetbrains.annotations.Nullable
//    @Override
//    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
//        return new SpiritualRoomBlockEntity(blockPos, blockState);
//    }
//
//    @Nullable
//    @Override
//    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
//        return level.isClientSide ? null : createTickerHelper(blockEntityType, ImmortalBlockEntities.SPIRITUAL_ROOM.get(), SpiritualRoomBlockEntity::serverTick);
//    }
//
//    @Override
//    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos blockPos, CollisionContext context) {
//        return AABB;
//    }

}
