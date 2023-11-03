package hungteen.imm.common.block.artifacts;

import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.common.blockentity.ElixirRoomBlockEntity;
import hungteen.imm.common.blockentity.IMMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-27 12:41
 **/
public class ElixirRoomBlock extends ArtifactEntityBlock {

    private static final VoxelShape AABB = Block.box(1, 0, 1, 15, 6, 15);

    public ElixirRoomBlock(BlockBehaviour.Properties properties, IRealmType realmType) {
        super(properties, realmType);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            if(player instanceof ServerPlayer){
                    NetworkHooks.openScreen((ServerPlayer)player, getMenuProvider(blockState, level, blockPos), buf -> {
                        buf.writeBlockPos(blockPos);
                    });
            }
            return InteractionResult.CONSUME;
        }
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ElixirRoomBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, IMMBlockEntities.ELIXIR_ROOM.get(), ElixirRoomBlockEntity::serverTick);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos blockPos, CollisionContext context) {
        return AABB;
    }


}
