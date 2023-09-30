package hungteen.imm.common.block.artifacts;

import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.api.registry.IArtifactType;
import hungteen.imm.common.block.IMMBlockPatterns;
import hungteen.imm.common.blockentity.FunctionalFurnaceBlockEntity;
import hungteen.imm.common.blockentity.IMMBlockEntities;
import hungteen.imm.common.blockentity.SpiritualFurnaceBlockEntity;
import hungteen.imm.util.BlockUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-09 09:15
 **/
public class SpiritualFurnaceBlock extends ArtifactEntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    private static final Component NO_FURNACE_AROUND = TipUtil.info("no_furnace_around").withStyle(ChatFormatting.RED);

    public SpiritualFurnaceBlock(BlockBehaviour.Properties properties, IArtifactType artifactType) {
        super(properties.requiresCorrectToolForDrops().lightLevel(state -> {
            return state.getValue(LIT) ? 15 : 0;
        }), artifactType);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.FALSE).setValue(FACING, Direction.NORTH));
    }

    public static BlockPattern.BlockPatternMatch getMatch(Level level, BlockPos blockPos){
        final BlockPattern.BlockPatternMatch match = BlockUtil.match(level, IMMBlockPatterns.getFurnacePattern().blockPattern(), blockPos);
        if (match != null) {
            final BlockInWorld blockInWorld = getFurnace(match);
            // Furnace must face to outside.
            if(match.getForwards().equals(blockInWorld.getState().getValue(FACING))){
                return match;
            }
        }
        return null;
    }

    public static boolean makeNewFurnace(Level level, BlockPos blockPos){
        return false;
    }

    public static void use(Level level, Player player, BlockState blockState, BlockPos blockPos){
        if (player instanceof ServerPlayer serverPlayer && ! serverPlayer.hasContainerOpen()) {
            final BlockPattern.BlockPatternMatch match = getMatch(level, blockPos);
            if (match != null) {
                final BlockInWorld blockInWorld = getFurnace(match);
                final BlockPos keypoint = blockInWorld.getPos();
                if(blockInWorld.getState().getBlock() instanceof SpiritualFurnaceBlock furnaceBlock){
                    NetworkHooks.openScreen(serverPlayer, furnaceBlock.getMenuProvider(blockState, level, keypoint), buf -> {
                        buf.writeBlockPos(keypoint);
                    });
                }
            } else {
                if(blockState.getBlock() instanceof SpiritualFurnaceBlock){
                    PlayerHelper.sendTipTo(player, NO_FURNACE_AROUND);
                }
            }
        }
    }

    public static BlockInWorld getFurnace(@NotNull BlockPattern.BlockPatternMatch match){
        return match.getBlock(3, 2, 3);
    }

    @Nullable
    public static SpiritualFurnaceBlockEntity getFurnaceBlockEntity(@NotNull BlockPattern.BlockPatternMatch match){
       if(getFurnace(match).getEntity() instanceof SpiritualFurnaceBlockEntity blockEntity) return blockEntity;
       return null;
    }

    public static BlockInWorld getFunctional(@NotNull BlockPattern.BlockPatternMatch match){
        return match.getBlock(3, 0, 2);
    }

    @Nullable
    public static FunctionalFurnaceBlockEntity getFunctionalBlockEntity(@NotNull BlockPattern.BlockPatternMatch match){
        final BlockInWorld blockInWorld = getFunctional(match);
        if(getFunctional(match).getEntity() instanceof FunctionalFurnaceBlockEntity blockEntity) return blockEntity;
        return null;
    }

    public static boolean persist(Level level, BlockPattern.BlockPatternMatch matched){
        return IMMBlockPatterns.getFurnacePattern().blockPattern().matches(level, matched.getFrontTopLeft(), matched.getForwards(), matched.getUp()) != null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState state) {
        return new SpiritualFurnaceBlockEntity(blockPos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, IMMBlockEntities.SPIRITUAL_FURNACE.get(), SpiritualFurnaceBlockEntity::serverTick);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(FACING, LIT);
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }

}
