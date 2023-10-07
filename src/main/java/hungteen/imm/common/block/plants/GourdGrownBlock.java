package hungteen.imm.common.block.plants;

import hungteen.htlib.common.block.plants.HTAttachedStemBlock;
import hungteen.htlib.common.block.plants.HTStemGrownBlock;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Locale;

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
        RED(10),

        /**
         * 木。
         */
        ORANGE(10),

        /**
         * 金。
         */
        YELLOW(10),

        /**
         * 火。
         */
        GREEN(10),

        /**
         * 水。
         */
        AQUA(10),

        /**
         * 阴。
         */
        BLUE(1),

        /**
         * 阳。
         */
        PURPLE(1);

        private GourdGrownBlock gourdGrownBlock;
        private final int weight;

        private GourdTypes(int weight) {
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

    }
}
