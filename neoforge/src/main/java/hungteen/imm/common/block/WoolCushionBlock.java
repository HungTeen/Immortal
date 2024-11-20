package hungteen.imm.common.block;

import hungteen.imm.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.Map;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-03-11 20:55
 **/
public class WoolCushionBlock extends CushionBlock{

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private static final Map<DyeColor, WoolCushionBlock> COLOR_TO_CUSHION = new EnumMap<>(DyeColor.class);
    private static final VoxelShape AABB = Block.box(0, 0, 0, 16, 7, 16);
    private final DyeColor dyeColor;

    public static ResourceLocation getWoolCushionLocation(DyeColor color){
        return Util.prefix(color.getName() + "_wool_cushion");
    }

    public static Block getWoolCushion(DyeColor color){
        return COLOR_TO_CUSHION.get(color);
    }

    public WoolCushionBlock(DyeColor dyeColor) {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.BLACK_BED).mapColor(dyeColor.getMapColor()));
        this.dyeColor = dyeColor;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
        COLOR_TO_CUSHION.put(dyeColor, this);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos blockPos, CollisionContext context) {
        return AABB;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }

    public DyeColor getDyeColor() {
        return dyeColor;
    }
}
