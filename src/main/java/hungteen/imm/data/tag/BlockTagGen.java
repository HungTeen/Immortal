package hungteen.imm.data.tag;

import hungteen.htlib.data.tag.HTBlockTagGen;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.tag.IMMBlockTags;
import hungteen.imm.util.BlockUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-07 12:12
 **/
public class BlockTagGen extends HTBlockTagGen {

    public BlockTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.addMCTags();

        /* Forge */
        this.tag(IMMBlockTags.METAL_ELEMENT_ATTACHED_BLOCKS)
                .addTags(Tags.Blocks.ORES_GOLD, Tags.Blocks.STORAGE_BLOCKS_GOLD, Tags.Blocks.STORAGE_BLOCKS_RAW_GOLD);
        this.tag(IMMBlockTags.WOOD_ELEMENT_ATTACHED_BLOCKS)
                .add(Blocks.MOSS_BLOCK, Blocks.MOSS_CARPET, Blocks.MYCELIUM);
        this.tag(IMMBlockTags.WATER_ELEMENT_ATTACHED_BLOCKS)
                .addTags(BlockTags.SNOW, BlockTags.ICE);
        this.tag(IMMBlockTags.FIRE_ELEMENT_ATTACHED_BLOCKS)
                .add(Blocks.MAGMA_BLOCK);
        this.tag(IMMBlockTags.EARTH_ELEMENT_ATTACHED_BLOCKS)
                .add(Blocks.MUD);
        this.tag(IMMBlockTags.SPIRIT_ELEMENT_ATTACHED_BLOCKS)
                .add(Blocks.CRYING_OBSIDIAN, Blocks.SOUL_SAND);

        this.tag(IMMBlockTags.SPIRITUAL_ORES).addTag(BlockTags.EMERALD_ORES);
        this.tag(IMMBlockTags.CINNABAR_ORES).add(IMMBlocks.CINNABAR_ORE.get());

        this.tag(IMMBlockTags.COMMON_ARTIFACTS).add(Blocks.ENDER_CHEST, Blocks.RESPAWN_ANCHOR);
        this.tag(IMMBlockTags.MODERATE_ARTIFACTS).add(Blocks.CONDUIT);
        this.tag(IMMBlockTags.ADVANCED_ARTIFACTS).add(Blocks.ENCHANTING_TABLE, Blocks.BEACON);

        /* IMM */
        BlockUtil.getGourds().forEach(p -> this.tag(IMMBlockTags.GOURDS).add(p.getSecond()));
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
        /* 挖掘方式 & 等级 */

        this.tag(BlockTags.MINEABLE_WITH_AXE).add(IMMBlocks.GOURD_SCAFFOLD.get())
                .addTag(IMMBlockTags.GOURDS);
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(IMMBlocks.CINNABAR_ORE.get(), IMMBlocks.COPPER_ELIXIR_ROOM.get(), IMMBlocks.COPPER_FURNACE.get());
        this.tag(BlockTags.NEEDS_STONE_TOOL)
                .add(IMMBlocks.GOURD_SCAFFOLD.get())
                .addTag(IMMBlockTags.GOURDS);
        this.tag(BlockTags.NEEDS_IRON_TOOL).add(IMMBlocks.CINNABAR_ORE.get(), IMMBlocks.COPPER_ELIXIR_ROOM.get(), IMMBlocks.COPPER_FURNACE.get());

        /* Woods */
//        this.woodIntegration(ImmortalWoods.MULBERRY);

        /* sapling */
        this.tag(BlockTags.SAPLINGS).add(IMMBlocks.MULBERRY_SAPLING.get());


        /* leaves */
        this.tag(BlockTags.LEAVES).add(IMMBlocks.MULBERRY_LEAVES.get(), IMMBlocks.MULBERRY_LEAVES_WITH_MULBERRIES.get());

        /* crops */
//        this.tag(BlockTags.CROPS).add(PVZBlocks.PEA.get(), PVZBlocks.CABBAGE.get(), PVZBlocks.CORN.get());

        this.tag(BlockTags.CLIMBABLE).add(IMMBlocks.GOURD_SCAFFOLD.get());
    }

}
