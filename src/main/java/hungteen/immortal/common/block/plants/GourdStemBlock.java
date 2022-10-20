package hungteen.immortal.common.block.plants;

import hungteen.htlib.block.plants.HTStemBlock;
import hungteen.htlib.block.plants.HTStemGrownBlock;
import hungteen.htlib.util.WeightList;
import hungteen.immortal.common.block.ImmortalBlocks;
import hungteen.immortal.common.item.ImmortalItems;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-06 22:31
 **/
public class GourdStemBlock extends HTStemBlock {

    public GourdStemBlock() {
        super(() -> ImmortalItems.GOURD_SEEDS.get(), BlockBehaviour.Properties.copy(Blocks.MELON_STEM));
    }

    @Override
    protected HTStemGrownBlock getResultFruit(Random random) {
        WeightList<HTStemGrownBlock> list = new WeightList<>();
        for(GourdGrownBlock.GourdTypes type : GourdGrownBlock.GourdTypes.values()){
            list.addItem(type.getGourdGrownBlock(), type.getWeight());
        }
        return list.getRandomItem(random).get();
    }

    public static BlockState getFinalAge(){
        return ImmortalBlocks.GOURD_STEM.get().defaultBlockState().setValue(AGE, MAX_AGE);
    }
}
