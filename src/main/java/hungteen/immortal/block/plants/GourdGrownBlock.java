package hungteen.immortal.block.plants;

import hungteen.htlib.block.plants.HTAttachedStemBlock;
import hungteen.htlib.block.plants.HTStemGrownBlock;
import hungteen.immortal.block.ImmortalBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

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
        return ImmortalBlocks.GOURD_ATTACHED_STEM.get();
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return AABB;
    }

    public enum GourdTypes {
        RED(10),
        ORANGE(5),
        YELLOW(10),
        GREEN(8),
        AQUA(15),
        BLUE(5),
        PURPLE(3);

        private GourdGrownBlock gourdGrownBlock;
        private Item gourdItem;
        private final int weight;

        private GourdTypes(int weight) {
            this.weight = weight;
        }

        public void setGourdGrownBlock(GourdGrownBlock gourdGrownBlock) {
            this.gourdGrownBlock = gourdGrownBlock;
        }

        public void setGourdItem(Item gourdItem) {
            this.gourdItem = gourdItem;
        }

        public int getWeight() {
            return weight;
        }

        public GourdGrownBlock getGourdGrownBlock() {
            return gourdGrownBlock;
        }

        public Item getGourdItem() {
            return gourdItem;
        }

    }
}
