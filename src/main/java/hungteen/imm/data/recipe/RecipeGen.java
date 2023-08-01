package hungteen.imm.data.recipe;

import hungteen.htlib.util.helper.registry.ItemHelper;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.block.WoolCushionBlock;
import hungteen.imm.common.item.IMMItems;
import hungteen.imm.util.Util;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-29 12:26
 **/
public class RecipeGen extends RecipeProvider {

    public RecipeGen(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        this.buildShapedRecipes(consumer);
        this.buildShapelessRecipes(consumer);
        this.buildElixirRecipes(consumer);
        this.buildSmithingRecipes(consumer);
    }

    protected void buildShapedRecipes(Consumer<FinishedRecipe> consumer) {
        for (DyeColor color : DyeColor.values()) {
            ItemHelper.get().get(WoolCushionBlock.getWoolCushionLocation(color)).ifPresent(cushion -> {
                ItemHelper.get().get(Util.mcPrefix(color.getName() + "_wool")).ifPresent(wool -> {
                    woolCushion(consumer, cushion, wool);
                });
                ItemHelper.get().get(Util.mcPrefix(color.getName() + "_dye")).ifPresent(dye -> {
                    woolCushionFromDye(consumer, cushion, dye);
                });
            });
        }
//        ShapedRecipeBuilder.shaped(ImmortalItems.FLAME_GOURD.get(), 1)
//                .define('A', GourdGrownBlock.GourdTypes.GREEN.getGourdGrownBlock())
//                .define('B', Items.BLAZE_ROD)
//                .save(consumer);
    }

    protected void buildShapelessRecipes(Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, IMMItems.FIVE_FLOWERS_ELIXIR.get())
                .requires(Items.SUNFLOWER)
                .requires(Items.LILAC)
                .requires(Items.ROSE_BUSH)
                .requires(Items.PEONY)
                .requires(Items.SPORE_BLOSSOM)
                .unlockedBy("has_flower", has(ItemTags.FLOWERS))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IMMItems.JOSS_PAPER.get())
                .requires(Items.BAMBOO)
                .requires(IMMItems.RICE_STRAW.get())
                .requires(IMMItems.JUTE.get())
                .unlockedBy("has_bamboo", has(Items.BAMBOO))
                .save(consumer);
    }

    /**
     * Recipes for Smithing Artifact.
     */
    protected void buildSmithingRecipes(Consumer<FinishedRecipe> consumer) {
//        genSmithing(consumer, ImmortalItems.BRONZE_SWORD.get(), 1, true, 100, 1F, RecipePatterns.SWORD, builder -> {
//            builder.define('X', Items.STICK).define('Y', Items.COPPER_INGOT);
//        });
//        genSmithing(consumer, ImmortalItems.BRONZE_SHORT_SWORD.get(), 1, true, 100, 1F, RecipePatterns.SHORT_SWORD, builder -> {
//            builder.define('X', Items.STICK).define('Y', Items.COPPER_INGOT);
//        });
//        genSmithing(consumer, ImmortalItems.BRONZE_AXE.get(), 1, true, 100, 1F, RecipePatterns.AXE, builder -> {
//            builder.define('X', Items.STICK).define('Y', Items.COPPER_INGOT);
//        });
//        genSmithing(consumer, ImmortalItems.BRONZE_PICKAXE.get(), 1, true, 100, 1F, RecipePatterns.PICKAXE, builder -> {
//            builder.define('X', Items.STICK).define('Y', Items.COPPER_INGOT);
//        });
//        genSmithing(consumer, ImmortalItems.BRONZE_SHOVEL.get(), 1, true, 100, 1F, RecipePatterns.SHOVEL, builder -> {
//            builder.define('X', Items.STICK).define('Y', Items.COPPER_INGOT);
//        });
//        genSmithing(consumer, ImmortalItems.BRONZE_HOE.get(), 1, true, 100, 1F, RecipePatterns.HOE, builder -> {
//            builder.define('X', Items.STICK).define('Y', Items.COPPER_INGOT);
//        });
    }

    /**
     * Recipes for Elixir Furnace.
     */
    protected void buildElixirRecipes(Consumer<FinishedRecipe> consumer) {
        genElixir(consumer, IMMItems.FIVE_FLOWERS_ELIXIR.get(), 600, 1, Arrays.asList(
                Items.SUNFLOWER, Items.LILAC, Items.ROSE_BUSH, Items.PEONY, Items.SPORE_BLOSSOM
        ));
    }

//    private void genSmithing(Consumer<FinishedRecipe> consumer, ItemLike result, int requireLevel, boolean needRecovery, int needSmithingValue, float speedMultiple, List<String> pattern, Consumer<SmithingRecipeBuilder> patternConsumer){
//        SmithingRecipeBuilder builder = new SmithingRecipeBuilder(result, 1, requireLevel, needRecovery, needSmithingValue, speedMultiple)
//                .unlockedBy("has_smithing_artifact", has(ImmortalBlocks.COPPER_SMITHING_ARTIFACT.get()));
//        pattern.forEach(builder::pattern);
//        patternConsumer.accept(builder);
//        builder.save(consumer, Util.prefix("smithing/" + ItemHelper.getKey(result.asItem()).getPath()));
//    }

    private void genElixir(Consumer<FinishedRecipe> consumer, ItemLike result, int smeltCD, int requireFlameLevel, List<ItemLike> ingredients) {
        final ElixirRecipeBuilder builder = new ElixirRecipeBuilder(result, 1, smeltCD, requireFlameLevel);
        builder.unlockedBy("has_elixir_furnace", has(IMMBlocks.COPPER_ELIXIR_ROOM.get()));
        ingredients.forEach(builder::requires);
        builder.save(consumer, Util.prefix("elixirs/" + ItemHelper.get().getKey(result.asItem()).getPath()));
    }

    protected static void woolCushion(Consumer<FinishedRecipe> consumer, ItemLike cushion, ItemLike wool) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, cushion).define('#', wool).define('X', ItemTags.PLANKS).pattern("##").pattern("XX").group("wool_cushion").unlockedBy(getHasName(wool), has(wool)).save(consumer);
    }

    protected static void woolCushionFromDye(Consumer<FinishedRecipe> consumer, ItemLike cushion, ItemLike dye) {
        ItemHelper.get().get(WoolCushionBlock.getWoolCushionLocation(DyeColor.WHITE)).ifPresent(whiteCushion -> {
            if (cushion != whiteCushion) {
                ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, cushion).requires(whiteCushion).requires(dye).group("dyed_wool_cushion").unlockedBy("has_white_cushion", has(whiteCushion)).save(consumer, conversionName(cushion, whiteCushion));
            }
        });
    }

    protected static String conversionName(ItemLike old, ItemLike item) {
        return Util.prefixName(getItemName(old) + "_from_" + getItemName(item));
    }

}
