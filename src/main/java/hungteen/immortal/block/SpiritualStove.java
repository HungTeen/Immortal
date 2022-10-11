package hungteen.immortal.block;

import hungteen.htlib.block.entityblock.HTEntityBlock;
import hungteen.immortal.api.interfaces.IArtifact;
import hungteen.immortal.blockentity.SpiritualStoveBlockEntity;
import hungteen.immortal.menu.SpiritualStoveMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-09 09:15
 **/
public class SpiritualStove extends HTEntityBlock implements IArtifact {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    private final int level;

    public SpiritualStove(int level) {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).requiresCorrectToolForDrops().lightLevel(state -> {
            return state.getValue(LIT) ? 15 : 0;
        }));
        this.level = level;
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.valueOf(false)));
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            player.openMenu(blockState.getMenuProvider(level, blockPos));
            return InteractionResult.CONSUME;
        }
    }

    @javax.annotation.Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState blockState, Level level, BlockPos blockPos) {
        final BlockEntity blockentity = level.getBlockEntity(blockPos);
        if (blockentity instanceof Nameable) {
            Component component = ((Nameable) blockentity).getDisplayName();
            return new SimpleMenuProvider((id, inventory, player) -> {
                return new SpiritualStoveMenu(id, inventory, ContainerLevelAccess.create(level, blockPos));
            }, component);
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState state) {
        return new SpiritualStoveBlockEntity(blockPos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(LIT);
    }

    @Override
    public int getArtifactLevel() {
        return this.level;
    }
}
