package hungteen.immortal.data.tag;

import hungteen.htlib.data.tag.HTBlockTagGen;
import hungteen.immortal.common.block.ImmortalBlocks;
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
        /* planks */
        this.tag(BlockTags.PLANKS).add(ImmortalBlocks.MULBERRY_PLANKS.get());

        /* button */
        this.tag(BlockTags.BUTTONS).add(ImmortalBlocks.MULBERRY_BUTTON.get());

        /* stairs */
        this.tag(BlockTags.WOODEN_STAIRS).add(ImmortalBlocks.MULBERRY_STAIRS.get());

        /* slab */
        this.tag(BlockTags.WOODEN_SLABS).add(ImmortalBlocks.MULBERRY_SLAB.get());

        /* fence */
        this.tag(BlockTags.FENCES).add(ImmortalBlocks.MULBERRY_FENCE.get());
        this.tag(BlockTags.FENCE_GATES).add(ImmortalBlocks.MULBERRY_FENCE_GATE.get());

        /* door  */
        this.tag(BlockTags.WOODEN_DOORS).add(ImmortalBlocks.MULBERRY_DOOR.get());

        /* sapling */
        this.tag(BlockTags.SAPLINGS).add(ImmortalBlocks.MULBERRY_SAPLING.get());

        /* log & wood */
        this.tag(BlockTags.LOGS_THAT_BURN).add(ImmortalBlocks.MULBERRY_LOG.get(), ImmortalBlocks.MULBERRY_WOOD.get(), ImmortalBlocks.STRIPPED_MULBERRY_LOG.get(), ImmortalBlocks.STRIPPED_MULBERRY_WOOD.get());


        /* pressure plate */
        this.tag(BlockTags.WOODEN_PRESSURE_PLATES).add(ImmortalBlocks.MULBERRY_PRESSURE_PLATE.get());

        /* leaves */
        this.tag(BlockTags.LEAVES).add(ImmortalBlocks.MULBERRY_LEAVES.get(), ImmortalBlocks.MULBERRY_LEAVES_WITH_MULBERRIES.get());

        /* trap door */
        this.tag(BlockTags.WOODEN_TRAPDOORS).add(ImmortalBlocks.MULBERRY_TRAPDOOR.get());

        /* sign */
        this.tag(BlockTags.STANDING_SIGNS).add(ImmortalBlocks.MULBERRY_SIGN.get());
        this.tag(BlockTags.WALL_SIGNS).add(ImmortalBlocks.MULBERRY_WALL_SIGN.get());

        /* crops */
//        this.tag(BlockTags.CROPS).add(PVZBlocks.PEA.get(), PVZBlocks.CABBAGE.get(), PVZBlocks.CORN.get());

    }

}
