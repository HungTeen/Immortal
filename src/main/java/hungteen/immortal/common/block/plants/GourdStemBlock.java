package hungteen.immortal.common.block.plants;

import hungteen.htlib.common.block.plants.HTStemBlock;
import hungteen.htlib.common.block.plants.HTStemGrownBlock;
import hungteen.htlib.util.WeightedList;
import hungteen.immortal.common.block.ImmortalBlocks;
import hungteen.immortal.common.item.ImmortalItems;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

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
    protected HTStemGrownBlock getResultFruit(RandomSource random) {
        return WeightedList.create(GourdGrownBlock.GourdTypes.values()).getRandomItem(random).map(GourdGrownBlock.GourdTypes::getGourdGrownBlock).orElseThrow();
    }

    public static BlockState getFinalAge(){
        return ImmortalBlocks.GOURD_STEM.get().defaultBlockState().setValue(AGE, MAX_AGE);
    }
}
