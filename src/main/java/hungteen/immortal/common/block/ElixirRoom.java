package hungteen.immortal.common.block;

import hungteen.htlib.block.entityblock.HTEntityBlock;
import hungteen.htlib.util.PlayerUtil;
import hungteen.immortal.api.interfaces.IArtifact;
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
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
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
public class ElixirRoom extends HTEntityBlock implements IArtifact {

    private static final VoxelShape AABB = Block.box(0, 0, 0, 16, 9, 16);
    private final int level;

    public ElixirRoom(int level) {
        super(BlockBehaviour.Properties.copy(Blocks.ANVIL));
        this.level = level;
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            if(player instanceof ServerPlayer){
                if(level.getBlockEntity(blockPos.below()) instanceof SpiritualFurnaceBlockEntity){
                    NetworkHooks.openGui((ServerPlayer)player, getMenuProvider(blockState, level, blockPos), buf -> {
                        buf.writeBlockPos(blockPos);
                    });
                } else{
                    PlayerUtil.sendTipTo(player, TipUtil.ELIXIR_ROOM_TIP.withStyle(ChatFormatting.RED));
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

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean triggerEvent(BlockState state, Level level, BlockPos blockPos, int p_49229_, int p_49230_) {
        super.triggerEvent(state, level, blockPos, p_49229_, p_49230_);
        BlockEntity blockentity = level.getBlockEntity(blockPos);
        return blockentity != null && blockentity.triggerEvent(p_49229_, p_49230_);
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

    @Override
    public int getArtifactLevel() {
        return level;
    }
}
