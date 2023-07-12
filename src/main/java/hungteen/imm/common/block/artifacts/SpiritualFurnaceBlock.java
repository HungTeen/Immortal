package hungteen.imm.common.block.artifacts;

import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.api.registry.IArtifactType;
import hungteen.imm.common.block.IMMBlockPatterns;
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

    public static void use(Level level, Player player, BlockState blockState, BlockPos blockPos){
        if (player instanceof ServerPlayer serverPlayer) {
            BlockPattern.BlockPatternMatch match = BlockUtil.match(level, IMMBlockPatterns.getFurnacePattern(), blockPos);
            if (match != null) {
                final BlockInWorld blockInWorld = match.getBlock(4, 2, 4);
                if(blockInWorld.getState().getBlock() instanceof SpiritualFurnaceBlock furnaceBlock){
                    NetworkHooks.openScreen(serverPlayer, furnaceBlock.getMenuProvider(blockState, level, blockPos), buf -> {
                        buf.writeBlockPos(blockInWorld.getPos());
                    });
                } else if(blockInWorld.getState().getBlock() instanceof ElixirRoomBlock elixirRoom){
                    NetworkHooks.openScreen((ServerPlayer)player, elixirRoom.getMenuProvider(blockState, level, blockPos), buf -> {
                        buf.writeBlockPos(blockPos);
                    });
                }
            } else {
                if(blockState.getBlock() instanceof SpiritualFurnaceBlock){
                    PlayerHelper.sendTipTo(player, NO_FURNACE_AROUND);
                }
            }
        }
    }

//    @Override
//    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult) {
//        if (level.isClientSide) {
//            return InteractionResult.SUCCESS;
//        } else {
//            if (player instanceof ServerPlayer serverPlayer) {
////                BlockPattern.BlockPatternMatch match = BlockUtil.match(level, IMMBlockPatterns.getFurnacePattern(), blockPos);
////                if(match != null){
////                    NetworkHooks.openScreen(serverPlayer, getMenuProvider(blockState, level, blockPos), buf -> {
////                        buf.writeBlockPos(blockPos);
////                    });
////                    return InteractionResult.CONSUME;
////                } else {
////                    PlayerHelper.sendTipTo(player, );
////                }
////                BlockPattern blockPattern = IMMBlockPatterns.getFurnacePattern();
////                int cnt = 0;
////                for (Direction direction : Direction.values()) {
////                    for (Direction direction1 : Direction.values()) {
////                        if (direction1 != direction && direction1 != direction.getOpposite()) {
////                            gen(level, blockPattern, blockPos.offset(16 * (cnt / 8), 0, 16 * (cnt % 8)), direction, direction1);
////                            ++cnt;
////                        }
////                    }
////                }
//            }
//            return InteractionResult.PASS;
//        }
//    }

//    protected static void gen(Level level, BlockPattern blockPattern, BlockPos blockPos, Direction direction, Direction direction1) {
//        final List<List<String>> pattern = new ArrayList<>();
//        pattern.add(List.of("       ", "       ", "       ", "       ", "       "));
//        pattern.add(List.of("       ", "       ", "       ", "       ", " #   # "));
//        pattern.add(List.of("   ^   ", "  ###  ", "  #o#  ", "  ###  ", "       "));
//        pattern.add(List.of("  ^x^  ", "# # # #", " ## ## ", "  #o#  ", "       "));
//        pattern.add(List.of("   ^   ", "  ###  ", "  #f#  ", "  ###  ", "       "));
//        pattern.add(List.of("       ", "       ", "       ", "       ", " #   # "));
//        pattern.add(List.of("       ", "       ", "       ", "       ", "       "));
//        for (int i = 0; i < blockPattern.getWidth(); ++i) {
//            for (int j = 0; j < blockPattern.getHeight(); ++j) {
//                for (int k = 0; k < blockPattern.getDepth(); ++k) {
//                    BlockPos translate = translateAndRotate(blockPos, direction, direction1, i, j, k);
//                    char c = pattern.get(k).get(j).charAt(i);
//                    if(i == 0 && j == 0 && k == 0){
//                        level.setBlock(translate, Blocks.GLOWSTONE.defaultBlockState(), 3);
//                    } else if(c == '^'){
//                        level.setBlock(translate, Blocks.CUT_COPPER_SLAB.defaultBlockState(), 3);
//                    } else if(c == '#'){
//                        level.setBlock(translate, Blocks.COPPER_BLOCK.defaultBlockState(), 3);
//                    } else if(c == 'f'){
//                        level.setBlock(translate, Blocks.BEDROCK.defaultBlockState(), 3);
//                    } else if(c == 'o'){
//                        level.setBlock(translate, Blocks.OBSIDIAN.defaultBlockState(), 3);
//                    } else if(i == 0 ^ j == 0 ^ k == 0){
//                        level.setBlock(translate, Blocks.GLASS.defaultBlockState(), 3);
//                    }
//                }
//            }
//        }
//    }
//
//    protected static BlockPos translateAndRotate(BlockPos p_61191_, Direction p_61192_, Direction p_61193_, int p_61194_, int p_61195_, int p_61196_) {
//        if (p_61192_ != p_61193_ && p_61192_ != p_61193_.getOpposite()) {
//            Vec3i vec3i = new Vec3i(p_61192_.getStepX(), p_61192_.getStepY(), p_61192_.getStepZ());
//            Vec3i vec3i1 = new Vec3i(p_61193_.getStepX(), p_61193_.getStepY(), p_61193_.getStepZ());
//            Vec3i vec3i2 = vec3i.cross(vec3i1);
//            return p_61191_.offset(vec3i1.getX() * -p_61195_ + vec3i2.getX() * p_61194_ + vec3i.getX() * p_61196_, vec3i1.getY() * -p_61195_ + vec3i2.getY() * p_61194_ + vec3i.getY() * p_61196_, vec3i1.getZ() * -p_61195_ + vec3i2.getZ() * p_61194_ + vec3i.getZ() * p_61196_);
//        } else {
//            throw new IllegalArgumentException("Invalid forwards & up combination");
//        }
//    }

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
