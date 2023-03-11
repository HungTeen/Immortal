package hungteen.immortal.data;

import hungteen.htlib.data.HTBlockStateGen;
import hungteen.htlib.util.helper.StringHelper;
import hungteen.htlib.util.helper.registry.BlockHelper;
import hungteen.immortal.common.block.ImmortalBlocks;
import hungteen.immortal.common.block.WoolCushionBlock;
import hungteen.immortal.common.block.plants.GourdGrownBlock;
import hungteen.immortal.common.block.plants.GourdStemBlock;
import hungteen.immortal.common.impl.registry.ImmortalWoods;
import hungteen.immortal.utils.Util;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-07 12:13
 **/
public class BlockStateGen extends HTBlockStateGen {

    public BlockStateGen(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Util.id(), exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        /*
        Special model generated by blockbench & Ignored blocks.
         */
//        addedBlocks.addAll();

//        Arrays.asList(
//                ImmortalBlocks.COPPER_SPIRITUAL_FURNACE.get()
//        ).forEach(this::spiritualFurnace);
//
//        Arrays.asList(
//                ImmortalBlocks.COPPER_ELIXIR_ROOM.get()
//        ).forEach(this::elixirRoom);
//
//        Arrays.asList(
//                ImmortalBlocks.COPPER_SMITHING_ARTIFACT.get()
//        ).forEach(this::smithingArtifact);
//
//        Arrays.asList(
//                ImmortalBlocks.COPPER_SPIRITUAL_ROOM.get()
//        ).forEach(this::spiritualRoom);

        /* Crops with age property. */
//        Arrays.asList(
//                Pair.of(ImmortalBlocks.GOURD_STEM.get(), GourdStemBlock.AGE)
////                Pair.of(BlockRegister.TOXIC_SHROOM.get(), ToxicShroomBlock.AGE),
//        ).forEach(pair -> {
//            crop(pair.getFirst(), pair.getSecond(), cutout());
//        });
        crop(ImmortalBlocks.GOURD_STEM.get(), GourdStemBlock.AGE, cutout());

        /* Woods */
        ImmortalWoods.woods().forEach(this::woodIntegration);

        /* Blocks with cross style. */
        Arrays.asList(
                ImmortalBlocks.MULBERRY_SAPLING.get()
        ).forEach(block -> {
            cross(block);
            this.addedBlocks.add(block);
        });

        /* Horizontal Blocks. */
        Arrays.asList(
                ImmortalBlocks.GOURD_ATTACHED_STEM.get()
        ).forEach(block -> {
            horizontalBlock(block, models().cubeAll(name(block), BlockHelper.blockTexture(block)).renderType(cutout()));
            this.addedBlocks.add(block);
        });

        BlockHelper.get().getFilterEntries(WoolCushionBlock.class::isInstance).stream()
                .map(WoolCushionBlock.class::cast).forEach(block -> {
                    getVariantBuilder(block).forAllStates(state -> {
                        final Direction dir = state.getValue(WoolCushionBlock.FACING);
                        return ConfiguredModel.builder()
                                .modelFile(models().withExistingParent(name(block), Util.prefix("block/wool_cushion"))
                                        .texture("color", Util.mcPrefix("entity/bed/" + block.getDyeColor().getName()))
                                        .renderType(cutout())
                                )
                                .rotationY(((int) dir.toYRot() + 180) % 360).build();
                    });
                    this.addedBlocks.add(block);
                });

        /* Common Blocks. */
        for (Block block : ForgeRegistries.BLOCKS) {
            if (!Util.in(key(block)) || addedBlocks.contains(block)) {
                continue;
            }
            if (block instanceof GourdGrownBlock) {
                // normal block items.
                gourd(block);
            }
        }

        /* Last step for all normal block models. */
        for (Block block : ForgeRegistries.BLOCKS) {
            if (Util.in(key(block)) && !addedBlocks.contains(block)) {
                simpleBlock(block);
            }
        }
    }

    /**
     * Cutout gourd blocks.
     */
    public void gourd(Block block) {
        horizontalBlock(block, models().singleTexture(name(block), Util.prefix("gourd_block"), "gourd", BlockHelper.blockTexture(block)).renderType(cutout()));
        this.addedBlocks.add(block);
    }

    public void spiritualFurnace(Block block) {
//        getVariantBuilder(block).forAllStates(state -> {
//            final Direction dir = state.getValue(SpiritualFurnace.FACING);
//            final boolean lit = state.getValue(SpiritualFurnace.LIT);
//            final ResourceLocation texture = BlockHelper.blockTexture(block);
//            return ConfiguredModel.builder()
//                    .modelFile(models().withExistingParent(name(block) + (lit ? "_lit" : ""), Util.prefix("block/spiritual_furnace_base"))
//                            .texture("furnace", lit ? StringHelper.suffix(texture, "lit") : texture)
//                            .renderType(cutout())
//                    )
//                    .rotationY(((int) dir.toYRot() + 180) % 360).build();
//        });
        this.addedBlocks.add(block);
    }

    public void elixirRoom(Block block) {
        simpleBlock(block, models().withExistingParent(name(block), Util.prefix("block/elixir_room_base"))
                .texture("top", StringHelper.suffix(BlockHelper.blockTexture(block), "top"))
                .texture("bottom", StringHelper.suffix(BlockHelper.blockTexture(block), "bottom"))
                .texture("sides", StringHelper.suffix(BlockHelper.blockTexture(block), "sides"))
                .renderType(cutout())
        );
        this.addedBlocks.add(block);
    }

    public void smithingArtifact(Block block) {
        simpleBlock(block, models().withExistingParent(name(block), Util.prefix("block/smithing_artifact_base"))
                .texture("top", StringHelper.suffix(BlockHelper.blockTexture(block), "top"))
                .texture("bottom", StringHelper.suffix(BlockHelper.blockTexture(block), "bottom"))
                .texture("top_sides", StringHelper.suffix(BlockHelper.blockTexture(block), "top_sides"))
                .texture("bottom_sides", StringHelper.suffix(BlockHelper.blockTexture(block), "bottom_sides"))
                .renderType(cutout())
        );
        this.addedBlocks.add(block);
    }

    public void spiritualRoom(Block block) {
        simpleBlock(block, models().withExistingParent(name(block), Util.prefix("block/spiritual_room_base"))
                .texture("top", StringHelper.suffix(BlockHelper.blockTexture(block), "top"))
                .texture("bottom", StringHelper.suffix(BlockHelper.blockTexture(block), "bottom"))
                .texture("sides", StringHelper.suffix(BlockHelper.blockTexture(block), "sides"))
                .texture("light", StringHelper.suffix(BlockHelper.blockTexture(block), "light"))
                .texture("insides", StringHelper.suffix(BlockHelper.blockTexture(block), "insides"))
                .renderType(translucent())
        );
        this.addedBlocks.add(block);
    }

}
