package hungteen.immortal.common.block.artifacts;

import hungteen.htlib.common.block.entityblock.HTEntityBlock;
import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.immortal.api.interfaces.IArtifactBlock;
import hungteen.immortal.api.interfaces.IArtifactItem;
import hungteen.immortal.api.registry.IArtifactType;
import hungteen.immortal.common.blockentity.ElixirRoomBlockEntity;
import hungteen.immortal.common.blockentity.ImmortalBlockEntities;
import hungteen.immortal.common.blockentity.SpiritualFurnaceBlockEntity;
import hungteen.immortal.utils.TipUtil;
import net.minecraft.ChatFormatting;
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
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-27 12:41
 **/
public class ElixirRoom extends ArtifactEntityBlock {

    private static final VoxelShape AABB = Block.box(1, 0, 1, 15, 6, 15);

    public ElixirRoom(IArtifactType artifactType) {
        super(BlockBehaviour.Properties.copy(Blocks.ANVIL), artifactType);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            if(player instanceof ServerPlayer){
                if(level.getBlockEntity(blockPos.below()) instanceof SpiritualFurnaceBlockEntity){
                    NetworkHooks.openScreen((ServerPlayer)player, getMenuProvider(blockState, level, blockPos), buf -> {
                        buf.writeBlockPos(blockPos);
                    });
                } else{
                    PlayerHelper.sendTipTo(player, TipUtil.ELIXIR_ROOM_TIP.withStyle(ChatFormatting.RED));
                    return InteractionResult.PASS;
                }
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
        return level.isClientSide ? null : createTickerHelper(blockEntityType, ImmortalBlockEntities.ELIXIR_ROOM.get(), ElixirRoomBlockEntity::serverTick);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos blockPos, CollisionContext context) {
        return AABB;
    }


}
