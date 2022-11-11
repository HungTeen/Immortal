package hungteen.immortal.data;

import hungteen.htlib.data.HTBlockModelGen;
import hungteen.immortal.common.block.ImmortalBlocks;
import hungteen.immortal.utils.Util;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Arrays;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-07 12:10
 **/
public class BlockModelGen extends HTBlockModelGen {

    public BlockModelGen(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Util.id(), existingFileHelper);
    }

    @Override
    protected void registerModels() {
        /*
        Fence Blocks.
         */
        Arrays.asList(
                ImmortalBlocks.MULBERRY_FENCE.get()
        ).forEach(this::fence);

        /*
        Button Blocks.
         */
        Arrays.asList(
                ImmortalBlocks.MULBERRY_BUTTON.get()
        ).forEach(this::button);
    }
}
