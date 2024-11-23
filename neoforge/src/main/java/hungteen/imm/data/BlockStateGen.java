package hungteen.imm.data;

import hungteen.htlib.data.HTBlockStateGen;
import hungteen.htlib.util.helper.StringHelper;
import hungteen.htlib.util.helper.impl.BlockHelper;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.block.WoolCushionBlock;
import hungteen.imm.common.block.artifacts.SpiritualFurnaceBlock;
import hungteen.imm.common.block.artifacts.TeleportAnchorBlock;
import hungteen.imm.common.block.plants.GourdGrownBlock;
import hungteen.imm.common.block.plants.GourdStemBlock;
import hungteen.imm.common.block.plants.IMMCropBlock;
import hungteen.imm.common.block.plants.JuteBlock;
import hungteen.imm.common.impl.registry.IMMWoods;
import hungteen.imm.util.Util;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Arrays;
import java.util.List;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-07 12:13
 **/
public class BlockStateGen extends HTBlockStateGen {

    public BlockStateGen(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Util.id(), exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        /*
        Special model generated by blockbench & Ignored blocks.
         */
        addedBlocks.addAll(List.of(
                IMMBlocks.GOURD_SCAFFOLD.get(),
                IMMBlocks.GANODERMA.get()
        ));

        Arrays.asList(
                IMMBlocks.COPPER_FURNACE.get()
        ).forEach(this::spiritualFurnace);

        Arrays.asList(
                IMMBlocks.COPPER_ELIXIR_ROOM.get()
        ).forEach(this::elixirRoom);

//        Arrays.asList(
//                ImmortalBlocks.COPPER_SMITHING_ARTIFACT.get()
//        ).forEach(this::smithingArtifact);
//
//        Arrays.asList(
//                ImmortalBlocks.COPPER_SPIRITUAL_ROOM.get()
//        ).forEach(this::spiritualRoom);

        Arrays.asList(
                IMMBlocks.RICE.get()
        ).forEach(this::rice);

        this.gen(IMMBlocks.JUTE.get(), this::jute);

        this.gen(IMMBlocks.TELEPORT_ANCHOR.get(), block -> {
            getVariantBuilder(block).forAllStates(state -> {
                final int charge = state.getValue(TeleportAnchorBlock.CHARGE);
                return ConfiguredModel.builder()
                        .modelFile(models().cubeBottomTop(name(block) + "_" + charge,
                                        StringHelper.suffix(BlockHelper.blockTexture(block), "side" + charge),
                                        BlockHelper.blockTexture(Blocks.POLISHED_DEEPSLATE),
                                        StringHelper.suffix(BlockHelper.blockTexture(block), "top")
                                ).renderType(solid())
                        ).build();
            });
        });

        /* Crops with age property. */
//        Arrays.asList(
//                Pair.of(ImmortalBlocks.GOURD_STEM.get(), GourdStemBlock.AGE)
////                Pair.of(BlockRegister.TOXIC_SHROOM.get(), ToxicShroomBlock.AGE),
//        ).forEach(pair -> {
//            crop(pair.getFirst(), pair.getSecond(), cutout());
//        });
        this.gen(IMMBlocks.GOURD_STEM.get(), b -> crop(b, GourdStemBlock.AGE, cutout()));

        /* Woods */
        IMMWoods.woods().forEach(this::woodSuitGen);

        /* Blocks with cross style. */
        Arrays.asList(
                IMMBlocks.MULBERRY_SAPLING.get()
        ).forEach(block -> this.gen(block, this::cross));

        List.of(
                IMMBlocks.SPIRIT_BEDROCK.get()
        ).forEach(block -> {
            this.gen(block, b -> simpleBlock(b, translucent()));
        });

        /* Horizontal Blocks. */
        Arrays.asList(
                IMMBlocks.GOURD_ATTACHED_STEM.get()
        ).forEach(block -> {
            horizontalBlock(block, models()
                    .withExistingParent(name(block), Util.prefix("block/stem_base"))
                    .texture("stem_attached", Util.prefix("block/gourd_stem_attached"))
                    .texture("stem", Util.prefix("block/gourd_stem"))
                    .renderType(cutout()));
            this.addedBlocks.add(block);
        });

        BlockHelper.get().filterValues(WoolCushionBlock.class::isInstance).stream()
                .map(WoolCushionBlock.class::cast).forEach(block -> {
                    getVariantBuilder(block).forAllStates(state -> {
                        final Direction dir = state.getValue(WoolCushionBlock.FACING);
                        return ConfiguredModel.builder()
                                .modelFile(models().withExistingParent(name(block), Util.prefix("block/wool_cushion"))
                                        .texture("color", Util.mc().prefix("entity/bed/" + block.getDyeColor().getName()))
                                        .renderType(cutout())
                                )
                                .rotationY(((int) dir.toYRot() + 180) % 360).build();
                    });
                    this.addedBlocks.add(block);
                });

        /* Common Blocks. */
        for (Block block : BlockHelper.get().values()) {
            if (!Util.in(key(block)) || addedBlocks.contains(block)) {
                continue;
            }
            if (block instanceof GourdGrownBlock) {
                // normal block items.
                gourd(block);
            }
        }

        /* Last step for all normal block models. */
        for (Block block : BlockHelper.get().values()) {
            if (Util.in(key(block)) && !addedBlocks.contains(block)) {
                simpleBlock(block);
            }
        }
    }

    private void rice(Block block) {
        if(block instanceof IMMCropBlock b){
            getVariantBuilder(block).forAllStates(state -> {
                final int age = b.getStateIndex(state);
                final String name = name(block) + "_" + age;
                return ConfiguredModel.builder()
                        .modelFile(models().cross(name, StringHelper.blockTexture(Util.prefix(name))).renderType(cutout()))
                        .build();

            });
            this.add(block);
        } else {
            Util.error("Could not parse {} to IMMCropBlock in BlockStateGen !", name(block));
        }
    }

    private void jute(JuteBlock block) {
            getVariantBuilder(block).forAllStates(state -> {
                final int age = block.getStateIndex(state);
                final boolean isUpper = JuteBlock.isUpperState(state);
                final String name = name(block) + (isUpper ? "_upper" : "_lower") + "_" + age;
                return ConfiguredModel.builder()
                        .modelFile(models().cross(name, StringHelper.blockTexture(Util.prefix(name))).renderType(cutout()))
                        .build();
            });
    }

    /**
     * Cutout gourd blocks.
     */
    public void gourd(Block block) {
        getVariantBuilder(block).forAllStatesExcept(state -> {
            final boolean hanging = state.getValue(GourdGrownBlock.HANGING);
            return ConfiguredModel.builder().modelFile(
                    models().withExistingParent(name(block) + (hanging ? "_hanging" : ""), Util.prefix("block/" + (hanging ? "hanging_gourd" : "gourd")))
                            .renderType(cutout())
            ).build();
        }, GourdGrownBlock.FACING);

//        horizontalBlock(block, models().singleTexture(name(block), Util.prefix("gourd_block"), "gourd", BlockHelper.blockTexture(block)).renderType(cutout()));
        this.addedBlocks.add(block);
    }

    public void spiritualFurnace(Block block) {
        getVariantBuilder(block).forAllStates(state -> {
            final Direction dir = state.getValue(SpiritualFurnaceBlock.FACING);
            final boolean lit = state.getValue(SpiritualFurnaceBlock.LIT);
            final ResourceLocation texture = BlockHelper.blockTexture(block);
            return ConfiguredModel.builder()
                    .modelFile(models()
                            .orientable(name(block) + (lit ? "_lit" : ""), BlockHelper.blockTexture(Blocks.COPPER_BLOCK), lit ? StringHelper.suffix(texture, "lit") : texture, BlockHelper.blockTexture(Blocks.COPPER_BLOCK))
                            .renderType(solid())
                    )
                    .rotationY(((int) dir.toYRot() + 180) % 360).build();
        });
        this.add(block);
    }

    public void elixirRoom(Block block) {
        simpleBlock(block, models().withExistingParent(name(block), Util.prefix("block/elixir_room_base"))
                .texture("top", StringHelper.suffix(BlockHelper.blockTexture(block), "top"))
                .texture("bottom", StringHelper.suffix(BlockHelper.blockTexture(block), "bottom"))
                .texture("sides", StringHelper.suffix(BlockHelper.blockTexture(block), "sides"))
                .renderType(cutout())
        );
        this.add(block);
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
