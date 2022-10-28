package hungteen.immortal.common.block;

import hungteen.immortal.common.blockentity.ElixirFurnaceBlockEntity;
import hungteen.immortal.common.blockentity.ImmortalBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-27 12:41
 **/
public class ElixirFurnace extends FallingBlock implements EntityBlock {

    public ElixirFurnace() {
        super(BlockBehaviour.Properties.copy(Blocks.ANVIL));
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            if(player instanceof ServerPlayer){
                NetworkHooks.openGui((ServerPlayer)player, getMenuProvider(blockState, level, blockPos));
            }
            return InteractionResult.CONSUME;
        }
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ElixirFurnaceBlockEntity(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public boolean triggerEvent(BlockState state, Level level, BlockPos blockPos, int p_49229_, int p_49230_) {
        super.triggerEvent(state, level, blockPos, p_49229_, p_49230_);
        BlockEntity blockentity = level.getBlockEntity(blockPos);
        return blockentity != null && blockentity.triggerEvent(p_49229_, p_49230_);
    }

    @Nullable
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos blockPos) {
        BlockEntity blockentity = level.getBlockEntity(blockPos);
        return blockentity instanceof MenuProvider ? (MenuProvider)blockentity : null;
    }

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> entityType, BlockEntityType<E> entityType1, BlockEntityTicker<? super E> ticker) {
        return entityType1 == entityType ? (BlockEntityTicker<A>)ticker : null;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, ImmortalBlockEntities.ELIXIR_FURNACE.get(), ElixirFurnaceBlockEntity::serverTick);
    }

}
