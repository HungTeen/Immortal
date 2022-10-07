package hungteen.immortal.block.plants;

import hungteen.htlib.block.plants.HTAttachedStemBlock;
import hungteen.htlib.block.plants.HTStemGrownBlock;
import hungteen.immortal.block.ImmortalBlocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-06 22:31
 **/
public class GourdGrownBlock extends HTStemGrownBlock {

    private final GourdTypes type;

    public GourdGrownBlock(GourdTypes type) {
        super(BlockBehaviour.Properties.copy(Blocks.MELON));
        this.type = type;
    }


    @Override
    public HTAttachedStemBlock getAttachedStem() {
        return ImmortalBlocks.GOURD_ATTACHED_STEM.get();
    }

//    public static GourdStemBlock byGourdType(GourdTypes type){
//        return BlockUtil.getFilterBlocks(b -> b instanceof GourdStemBlock).stream().map(b -> (GourdStemBlock) b).
//    }

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
