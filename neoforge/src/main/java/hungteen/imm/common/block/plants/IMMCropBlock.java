package hungteen.imm.common.block.plants;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.CommonHooks;

/**
 * TODO Port to HTLib.
 * @author PangTeen
 * @program Immortal
 * @create 2023/6/1 11:26
 */
public abstract class IMMCropBlock extends CropBlock implements BonemealableBlock {

    public IMMCropBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return !this.isMaxAge(state);
    }

    public boolean canNaturalGrow(ServerLevel level, BlockState state, BlockPos pos) {
        return level.getRawBrightness(pos, 0) >= 9 && ! this.isMaxAge(state);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        if (!level.isAreaLoaded(pos, 1)) {
            return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        }
        if (this.canNaturalGrow(level, state, pos)) {
            final float f = CropBlock.getGrowthSpeed(state, level, pos);
            if (CommonHooks.canCropGrow(level, pos, state, randomSource.nextInt((int)(25.0F / f) + 1) == 0)) {
                this.growCrops(level, pos, state, true);
                CommonHooks.fireCropGrowPost(level, pos, state);
            }
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
        return (reader.getRawBrightness(pos, 0) >= 8 || reader.canSeeSky(pos)) && super.canSurvive(state, reader, pos);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof Ravager ravager && CommonHooks.canEntityDestroy(level, pos, ravager)) {
            level.destroyBlock(pos, true, entity);
        }

        super.entityInside(state, level, pos, entity);
    }

    public void growCrops(Level level, BlockPos pos, BlockState state, boolean natural) {
        final int age = Mth.clamp(this.getAge(state) + this.getAgeIncrease(level, natural), 0, this.getMaxAge());
        this.onGrow(level, state, pos, age);
    }

    protected int getAgeIncrease(Level level, boolean natural) {
        return natural ? 1 : Mth.nextInt(level.random, 2, 5);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter getter, BlockPos pos) {
        return state.is(Blocks.FARMLAND);
    }

    protected void onGrow(Level level, BlockState state, BlockPos pos, int age) {
        level.setBlock(pos, this.getStateForAge(age), 2);
    }

    @Override
    public abstract IntegerProperty getAgeProperty();

    @Override
    public abstract int getMaxAge();

    public abstract VoxelShape[] getShapes();

    public abstract ItemLike getSeedItem(BlockState state);

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return !this.isMaxAge(state);
    }

    @Override
    public boolean isBonemealSuccess(Level p_221045_, RandomSource p_221046_, BlockPos p_221047_, BlockState p_221048_) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource source, BlockPos pos, BlockState state) {
        this.growCrops(level, pos, state, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(getAgeProperty());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return getShapes()[Mth.clamp(getStateIndex(state), 0, getShapes().length - 1)];
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader getter, BlockPos pos, BlockState state) {
        return new ItemStack(getSeedItem(state));
    }

    @Override
    public int getAge(BlockState state){
        return state.getValue(getAgeProperty());
    }

    /**
     * Not every age will change the state and texture.
     */
    public int getStateIndex(BlockState state){
        return getAge(state);
    }

    @Override
    public BlockState getStateForAge(int age) {
        return this.defaultBlockState().setValue(this.getAgeProperty(), age);
    }

}
