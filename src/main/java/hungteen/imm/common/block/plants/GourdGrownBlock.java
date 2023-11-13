package hungteen.imm.common.block.plants;

import hungteen.htlib.common.block.plants.HTAttachedStemBlock;
import hungteen.htlib.common.block.plants.HTStemGrownBlock;
import hungteen.htlib.util.helper.registry.EffectHelper;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Locale;
import java.util.function.Supplier;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-06 22:31
 **/
public class GourdGrownBlock extends HTStemGrownBlock {

    private static final VoxelShape AABB;
    private final GourdTypes type;

    static{
        VoxelShape voxelshape1 = Block.box(5, 0, 5, 11, 5, 11);
        VoxelShape voxelShape2 = Block.box(6, 5, 6, 10, 9, 10);
        AABB = Shapes.or(voxelshape1, voxelShape2);
    }

    public GourdGrownBlock(GourdTypes type) {
        super(BlockBehaviour.Properties.copy(Blocks.MELON));
        this.type = type;
    }

    @Override
    public HTAttachedStemBlock getAttachedStem() {
        return IMMBlocks.GOURD_ATTACHED_STEM.get();
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return AABB;
    }

    public static ResourceLocation getGourdLocation(GourdTypes type){
        return Util.prefix(type.toString().toLowerCase(Locale.ROOT) + "_gourd");
    }

    public enum GourdTypes implements WeightedEntry {

        /**
         * 土。
         */
        RED(() -> EffectHelper.viewEffect(MobEffects.DAMAGE_BOOST, 200, 1),10),

        /**
         * 木。
         */
        ORANGE(() -> EffectHelper.viewEffect(MobEffects.NIGHT_VISION, 400, 1), 10),

        /**
         * 金。
         */
        YELLOW(() -> EffectHelper.viewEffect(MobEffects.DAMAGE_RESISTANCE, 200, 1), 10),

        /**
         * 火。
         */
        GREEN(() -> EffectHelper.viewEffect(MobEffects.FIRE_RESISTANCE, 300, 1), 10),

        /**
         * 水。
         */
        AQUA(() -> EffectHelper.viewEffect(MobEffects.WATER_BREATHING, 400, 1), 10),

        /**
         * 阴。
         */
        BLUE(() -> EffectHelper.viewEffect(MobEffects.INVISIBILITY, 300, 1), 1),

        /**
         * 阳。
         */
        PURPLE(() -> EffectHelper.viewEffect(MobEffects.WEAKNESS, 200, 1), 1);

        private GourdGrownBlock gourdGrownBlock;
        private final Supplier<MobEffectInstance> effectSupplier;
        private final int weight;

        private GourdTypes(Supplier<MobEffectInstance> effectSupplier, int weight) {
            this.effectSupplier = effectSupplier;
            this.weight = weight;
        }

        public void setGourdGrownBlock(GourdGrownBlock gourdGrownBlock) {
            this.gourdGrownBlock = gourdGrownBlock;
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
