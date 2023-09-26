package hungteen.imm.data.tag;

import hungteen.htlib.data.tag.HTBlockTagGen;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.tag.IMMBlockTags;
import hungteen.imm.util.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
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

        /* Forge */
        this.tag(IMMBlockTags.SPIRITUAL_ORES).addTag(BlockTags.EMERALD_ORES);
        this.tag(IMMBlockTags.CINNABAR_ORES).add(IMMBlocks.CINNABAR_ORE.get());

        this.tag(IMMBlockTags.COMMON_ARTIFACTS).add(Blocks.CRAFTING_TABLE, Blocks.ENDER_CHEST, Blocks.RESPAWN_ANCHOR);
        this.tag(IMMBlockTags.MODERATE_ARTIFACTS).add(Blocks.ANVIL, Blocks.BREWING_STAND, Blocks.CONDUIT);
        this.tag(IMMBlockTags.ADVANCED_ARTIFACTS).add(Blocks.ENCHANTING_TABLE, Blocks.BEACON);

        /* IMM */
        this.tag(IMMBlockTags.COPPER_BLOCKS)
                .addTags(Tags.Blocks.STORAGE_BLOCKS_COPPER)
                .add(Blocks.EXPOSED_COPPER, Blocks.WEATHERED_COPPER, Blocks.OXIDIZED_COPPER);
        this.tag(IMMBlockTags.COPPER_SLABS)
                .add(Blocks.CUT_COPPER_SLAB, Blocks.EXPOSED_CUT_COPPER_SLAB, Blocks.WEATHERED_CUT_COPPER_SLAB, Blocks.OXIDIZED_CUT_COPPER_SLAB);
        this.tag(IMMBlockTags.FUNCTIONAL_COPPERS)
                .add(IMMBlocks.COPPER_ELIXIR_ROOM.get());
        this.tag(IMMBlockTags.FURNACE_BLOCKS)
                .add(IMMBlocks.COPPER_FURNACE.get())
                .addTags(IMMBlockTags.COPPER_BLOCKS, IMMBlockTags.COPPER_SLABS);
    }

    private void addMCTags(){
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
