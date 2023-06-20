package hungteen.imm.data.tag;

import hungteen.htlib.data.tag.HTBlockTagGen;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.tag.IMMBlockTags;
import hungteen.imm.util.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-07 12:12
 **/
public class BlockTagGen extends HTBlockTagGen {

    public BlockTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, Util.id(), existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.addMCTags();
    }

    private void addMCTags(){
        /* Ores */
        this.tag(IMMBlockTags.SPIRITUAL_ORES).addTag(BlockTags.EMERALD_ORES);
        this.tag(IMMBlockTags.CINNABAR_ORES).add(IMMBlocks.CINNABAR_ORE.get());

        /* Woods */
//        this.woodIntegration(ImmortalWoods.MULBERRY);

        /* sapling */
        this.tag(BlockTags.SAPLINGS).add(IMMBlocks.MULBERRY_SAPLING.get());


        /* leaves */
        this.tag(BlockTags.LEAVES).add(IMMBlocks.MULBERRY_LEAVES.get(), IMMBlocks.MULBERRY_LEAVES_WITH_MULBERRIES.get());

        /* crops */
//        this.tag(BlockTags.CROPS).add(PVZBlocks.PEA.get(), PVZBlocks.CABBAGE.get(), PVZBlocks.CORN.get());

    }

}
