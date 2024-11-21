package hungteen.imm.common.block.artifacts;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.common.blockentity.ElixirRoomBlockEntity;
import hungteen.imm.common.blockentity.IMMBlockEntities;
import hungteen.imm.common.impl.registry.RealmTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-27 12:41
 **/
public class ElixirRoomBlock extends ArtifactEntityBlock {

    public static final MapCodec<ElixirRoomBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            propertiesCodec(),
            RealmTypes.registry().byNameCodec().fieldOf("realm").forGetter(ElixirRoomBlock::getRealmType)
    ).apply(instance, ElixirRoomBlock::new));

    private static final VoxelShape AABB = Block.box(1, 0, 1, 15, 6, 15);

    public ElixirRoomBlock(BlockBehaviour.Properties properties, IRealmType realmType) {
        super(properties, realmType);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        } else {
            openMenu(blockState, level, pos, player);
            return ItemInteractionResult.SUCCESS;
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            openMenu(blockState, level, pos, player);
            return InteractionResult.CONSUME;
        }
    }

    public void openMenu(BlockState blockState, Level level, BlockPos blockPos, Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(getMenuProvider(blockState, level, blockPos), buf -> {
                buf.writeBlockPos(blockPos);
            });
        }
    }

    @Nullable
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

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

}
