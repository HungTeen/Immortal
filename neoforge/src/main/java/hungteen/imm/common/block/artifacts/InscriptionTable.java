package hungteen.imm.common.block.artifacts;

import hungteen.imm.api.artifact.ArtifactRank;
import hungteen.imm.common.menu.InscriptionTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-04-09 22:55
 **/
public class InscriptionTable extends SimpleArtifactBlock {

    public InscriptionTable() {
        super(Properties.ofFullCopy(Blocks.CRAFTING_TABLE), ArtifactRank.MODERATE);
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
            serverPlayer.openMenu(blockState.getMenuProvider(level, blockPos));
        }
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider((id, inventory, player) -> {
            return new InscriptionTableMenu(id, inventory, ContainerLevelAccess.create(level, pos));
        }, Component.empty());
    }

}
