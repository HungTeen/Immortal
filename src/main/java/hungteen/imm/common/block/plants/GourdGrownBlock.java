package hungteen.imm.common.block.plants;

import hungteen.htlib.common.block.plants.HTAttachedStemBlock;
import hungteen.htlib.common.block.plants.HTStemGrownBlock;
import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.registry.EffectHelper;
import hungteen.htlib.util.records.HTColor;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.function.Supplier;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-06 22:31
 **/
public class GourdGrownBlock extends HTStemGrownBlock {

    public static final BooleanProperty HANGING = BlockStateProperties.HANGING;

    private static final VoxelShape HANGING_AABB;
    private static final VoxelShape AABB;
    private final GourdTypes type;

    static{
        VoxelShape voxelshape1 = Block.box(5, 0, 5, 11, 6, 11);
        VoxelShape voxelShape2 = Block.box(6, 6, 6, 10, 9, 10);
        VoxelShape voxelshape3 = Block.box(5, 1, 5, 11, 7, 11);
        VoxelShape voxelShape4 = Block.box(6, 7, 6, 10, 10, 10);
        AABB = Shapes.or(voxelshape1, voxelShape2);
        HANGING_AABB = Shapes.or(voxelshape3, voxelShape4);
    }

    public GourdGrownBlock(GourdTypes type) {
        super(BlockBehaviour.Properties.copy(Blocks.MELON));
        this.type = type;
        this.registerDefaultState(this.stateDefinition.any().setValue(HANGING, Boolean.FALSE));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        for(Direction direction : context.getNearestLookingDirections()) {
            if (direction.getAxis() == Direction.Axis.Y) {
                return this.defaultBlockState().setValue(HANGING, direction == Direction.UP);
            }
        }
        return null;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return state.getValue(HANGING) ? HANGING_AABB : AABB;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HANGING, FACING);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
        Direction direction = getConnectedDirection(state).getOpposite();
        if(direction == Direction.UP){
            BlockState upperState = reader.getBlockState(pos.relative(direction));
            if(upperState.is(IMMBlocks.GOURD_SCAFFOLD.get()) || upperState.is(Blocks.SCAFFOLDING)){
                return true;
            }
        }
        return Block.canSupportCenter(reader, pos.relative(direction), direction.getOpposite());
    }

    protected static Direction getConnectedDirection(BlockState state) {
        return state.getValue(HANGING) ? Direction.DOWN : Direction.UP;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState nearby, LevelAccessor accessor, BlockPos p_153487_, BlockPos p_153488_) {
        return getConnectedDirection(state).getOpposite() == direction && !state.canSurvive(accessor, p_153487_) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, nearby, accessor, p_153487_, p_153488_);
    }

    @Override
    public HTAttachedStemBlock getAttachedStem() {
        return IMMBlocks.GOURD_ATTACHED_STEM.get();
    }

    public static ResourceLocation getGourdLocation(GourdTypes type){
        return Util.prefix(type.toString().toLowerCase(Locale.ROOT) + "_gourd");
    }

    public GourdTypes getType() {
        return type;
    }

    public enum GourdTypes implements WeightedEntry {

        /**
         * 土。
         */
        RED(() -> EffectHelper.viewEffect(MobEffects.DAMAGE_BOOST, 200, 1), ColorHelper.DARK_RED, 10),

        /**
         * 木。
         */
        ORANGE(() -> EffectHelper.viewEffect(MobEffects.NIGHT_VISION, 400, 1), ColorHelper.ORANGE, 10),

        /**
         * 金。
         */
        YELLOW(() -> EffectHelper.viewEffect(MobEffects.DAMAGE_RESISTANCE, 200, 1), ColorHelper.YELLOW, 10),

        /**
         * 火。
         */
        GREEN(() -> EffectHelper.viewEffect(MobEffects.FIRE_RESISTANCE, 300, 1), ColorHelper.GREEN, 10),

        /**
         * 水。
         */
        AQUA(() -> EffectHelper.viewEffect(MobEffects.WATER_BREATHING, 400, 1), ColorHelper.AQUA, 10),

        /**
         * 阴。
         */
        BLUE(() -> EffectHelper.viewEffect(MobEffects.INVISIBILITY, 300, 1), ColorHelper.DARK_BLUE, 1),

        /**
         * 阳。
         */
        PURPLE(() -> EffectHelper.viewEffect(MobEffects.WEAKNESS, 200, 1), ColorHelper.DARK_PURPLE, 1);

        private GourdGrownBlock gourdGrownBlock;
        private final Supplier<MobEffectInstance> effectSupplier;
        private final int weight;
        private final int color;

        GourdTypes(Supplier<MobEffectInstance> effectSupplier, HTColor color, int weight) {
            this.effectSupplier = effectSupplier;
            this.weight = weight;
            this.color = color.rgb();
        }

        public void setGourdGrownBlock(GourdGrownBlock gourdGrownBlock) {
            this.gourdGrownBlock = gourdGrownBlock;
        }

        public int getColor() {
            return color;
        }

        public Weight getWeight() {
            return Weight.of(weight);
        }

        public GourdGrownBlock getGourdGrownBlock() {
            return gourdGrownBlock;
        }

        public Supplier<MobEffectInstance> getEffectSupplier() {
            return effectSupplier;
        }
    }
}
