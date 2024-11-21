package hungteen.imm.common.block.artifacts;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-11-13 19:22
 **/
public class SmithingArtifact {
//        extends ArtifactEntityBlock {
//
//    private static final VoxelShape AABB;
//
//    static{
//        VoxelShape voxelShape = Block.box(0, 6, 0, 16, 14, 16);
//        VoxelShape voxelShape2 = Block.box(2, 6, 2, 14, 6, 14);
//        AABB = Shapes.or(voxelShape, voxelShape2);
//    }
//
//    public SmithingArtifact(IArtifactType artifactType) {
//        super(BlockBehaviour.Properties.copy(Blocks.SMITHING_TABLE), artifactType);
//    }
//
//    @Override
//    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult) {
//        if (level.isClientSide) {
//            return InteractionResult.SUCCESS;
//        } else {
//            if(player instanceof ServerPlayer){
//                if(player.getItemInHand(hand).canPerformAction(ImmortalToolActions.ARTIFACT_SMITHING)){
////                    final BlockEntity blockEntity = level.getBlockEntity(blockPos);
////                    if(blockEntity instanceof SmithingArtifactBlockEntity){
////                        player.startUsingItem(hand);
////                        ((SmithingArtifactBlockEntity) blockEntity).onSmithing();
////                    }
//                    return InteractionResult.PASS;
//                } else{
//                    NetworkHooks.openScreen((ServerPlayer)player, getMenuProvider(blockState, level, blockPos), buf -> {
//                        buf.writeBlockPos(blockPos);
//                    });
//                }
//                return InteractionResult.CONSUME;
//            }
//            return InteractionResult.PASS;
//        }
//    }
//
//    @Nullable
//    @Override
//    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
//        return new SmithingArtifactBlockEntity(blockPos, blockState);
//    }
//
//    @Nullable
//    @Override
//    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
//        return null;
//    }
//
//    @Override
//    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos blockPos, CollisionContext context) {
//        return AABB;
//    }

}
