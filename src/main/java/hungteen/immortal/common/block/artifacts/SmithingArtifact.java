package hungteen.immortal.common.block.artifacts;

import hungteen.htlib.common.block.entityblock.HTEntityBlock;
import hungteen.immortal.api.interfaces.IArtifact;
import hungteen.immortal.common.blockentity.SmithingArtifactBlockEntity;
import hungteen.immortal.common.item.ImmortalToolActions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-13 19:22
 **/
public class SmithingArtifact extends HTEntityBlock implements IArtifact {

    private static final VoxelShape AABB;
    private final int level;

    static{
        VoxelShape voxelShape = Block.box(0, 6, 0, 16, 14, 16);
        VoxelShape voxelShape2 = Block.box(2, 6, 2, 14, 6, 14);
        AABB = Shapes.or(voxelShape, voxelShape2);
    }

    public SmithingArtifact(int level) {
        super(BlockBehaviour.Properties.copy(Blocks.SMITHING_TABLE));
        this.level = level;
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            if(player instanceof ServerPlayer){
                if(player.getItemInHand(hand).canPerformAction(ImmortalToolActions.ARTIFACT_SMITHING)){
//                    final BlockEntity blockEntity = level.getBlockEntity(blockPos);
//                    if(blockEntity instanceof SmithingArtifactBlockEntity){
//                        player.startUsingItem(hand);
//                        ((SmithingArtifactBlockEntity) blockEntity).onSmithing();
//                    }
                    return InteractionResult.PASS;
                } else{
                    NetworkHooks.openScreen((ServerPlayer)player, getMenuProvider(blockState, level, blockPos), buf -> {
                        buf.writeBlockPos(blockPos);
                    });
                }
                return InteractionResult.CONSUME;
            }
            return InteractionResult.PASS;
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SmithingArtifactBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return null;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos blockPos, CollisionContext context) {
        return AABB;
    }

    @Override
    public int getArtifactLevel() {
        return this.level;
    }
}
