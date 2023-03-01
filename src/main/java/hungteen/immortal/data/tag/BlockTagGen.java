package hungteen.immortal.data.tag;

import hungteen.htlib.data.tag.HTBlockTagGen;
import hungteen.immortal.common.block.ImmortalBlocks;
import hungteen.immortal.common.impl.registry.ImmortalWoods;
import hungteen.immortal.utils.Util;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-07 12:12
 **/
public class BlockTagGen extends HTBlockTagGen {
    public BlockTagGen(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, Util.id(), existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.addMCTags();
    }

    private void addMCTags(){
        /* Woods */
//        this.woodIntegration(ImmortalWoods.MULBERRY);

        /* sapling */
        this.tag(BlockTags.SAPLINGS).add(ImmortalBlocks.MULBERRY_SAPLING.get());


        /* leaves */
        this.tag(BlockTags.LEAVES).add(ImmortalBlocks.MULBERRY_LEAVES.get(), ImmortalBlocks.MULBERRY_LEAVES_WITH_MULBERRIES.get());

        /* crops */
//        this.tag(BlockTags.CROPS).add(PVZBlocks.PEA.get(), PVZBlocks.CABBAGE.get(), PVZBlocks.CORN.get());

    }

}
