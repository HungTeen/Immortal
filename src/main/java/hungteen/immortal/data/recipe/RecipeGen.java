package hungteen.immortal.data.recipe;

import hungteen.htlib.util.helper.ItemHelper;
import hungteen.immortal.api.registry.ISpiritualType;
import hungteen.immortal.common.block.ImmortalBlocks;
import hungteen.immortal.common.block.plants.GourdGrownBlock;
import hungteen.immortal.common.item.ImmortalItems;
import hungteen.immortal.impl.SpiritualTypes;
import hungteen.immortal.utils.Util;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-29 12:26
 **/
public class RecipeGen extends RecipeProvider {

    public RecipeGen(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        this.buildNormalRecipes(consumer);
        this.buildElixirRecipes(consumer);
        this.buildSmithingRecipes(consumer);
    }

    /**
     * Recipes for Crafting Table.
     */
    protected void buildNormalRecipes(Consumer<FinishedRecipe> consumer) {
//        ShapedRecipeBuilder.shaped(ImmortalItems.FLAME_GOURD.get(), 1)
//                .define('A', GourdGrownBlock.GourdTypes.GREEN.getGourdGrownBlock())
//                .define('B', Items.BLAZE_ROD)
//                .save(consumer);
    }

    /**
     * Recipes for Smithing Artifact.
     */
    protected void buildSmithingRecipes(Consumer<FinishedRecipe> consumer) {
        genSmithing(consumer, ImmortalItems.BRONZE_SWORD.get(), 1, true, 100, 1F, RecipePatterns.SWORD, builder -> {
            builder.define('X', Items.STICK).define('Y', Items.COPPER_INGOT);
        });
        genSmithing(consumer, ImmortalItems.BRONZE_SHORT_SWORD.get(), 1, true, 100, 1F, RecipePatterns.SHORT_SWORD, builder -> {
            builder.define('X', Items.STICK).define('Y', Items.COPPER_INGOT);
        });
        genSmithing(consumer, ImmortalItems.BRONZE_AXE.get(), 1, true, 100, 1F, RecipePatterns.AXE, builder -> {
            builder.define('X', Items.STICK).define('Y', Items.COPPER_INGOT);
        });
        genSmithing(consumer, ImmortalItems.BRONZE_PICKAXE.get(), 1, true, 100, 1F, RecipePatterns.PICKAXE, builder -> {
            builder.define('X', Items.STICK).define('Y', Items.COPPER_INGOT);
        });
        genSmithing(consumer, ImmortalItems.BRONZE_SHOVEL.get(), 1, true, 100, 1F, RecipePatterns.SHOVEL, builder -> {
            builder.define('X', Items.STICK).define('Y', Items.COPPER_INGOT);
        });
        genSmithing(consumer, ImmortalItems.BRONZE_HOE.get(), 1, true, 100, 1F, RecipePatterns.HOE, builder -> {
            builder.define('X', Items.STICK).define('Y', Items.COPPER_INGOT);
        });
    }

    /**
     * Recipes for Elixir Furnace.
     */
    protected void buildElixirRecipes(Consumer<FinishedRecipe> consumer) {
        genElixir(consumer, ImmortalItems.FIVE_FLOWERS_ELIXIR.get(), 600, 1200, 10, 1,
                Arrays.asList(
                        Items.SUNFLOWER, Items.LILAC, Items.ROSE_BUSH, Items.PEONY, Items.SPORE_BLOSSOM
                ), Map.of(
                        SpiritualTypes.METAL, 5,
                        SpiritualTypes.WOOD, 5,
                        SpiritualTypes.WATER, 5,
                        SpiritualTypes.FIRE, 5,
                        SpiritualTypes.EARTH, 5
                )
        );
        genElixir(consumer, ImmortalItems.ANTIDOTE_ELIXIR.get(), 600, 1200, 10, 1,
                Arrays.asList(
                        Items.MILK_BUCKET, Items.GOLDEN_CARROT, Items.GLISTERING_MELON_SLICE
                ), Map.of(
                        SpiritualTypes.WOOD, 5, 
                        SpiritualTypes.WATER, 5,
                        SpiritualTypes.FIRE, 5,
                        SpiritualTypes.EARTH, 5,
                        SpiritualTypes.DRUG, 10
                )
        );
        genElixir(consumer, ImmortalItems.SPIRIT_RECOVERY_ELIXIR.get(), 600, 300, 10, 1,
                Arrays.asList(
                        Items.BROWN_MUSHROOM, Items.RED_MUSHROOM, Items.SWEET_BERRIES, Items.BEETROOT
                ), Map.of(
                        SpiritualTypes.METAL, 5,
                        SpiritualTypes.WOOD, 10,
                        SpiritualTypes.WATER, 5,
                        SpiritualTypes.FIRE, 5,
                        SpiritualTypes.EARTH, 5
                )
        );
        genElixir(consumer, ImmortalItems.ABSTINENCE_ELIXIR.get(), 600, 2400, 12, 1,
                Arrays.asList(
                        Items.WHEAT, Items.EGG, Items.SUGAR, Items.MILK_BUCKET, Items.GOLDEN_APPLE
                ), Map.of(
                        SpiritualTypes.METAL, 5,
                        SpiritualTypes.WOOD, 10,
                        SpiritualTypes.WATER, 15,
                        SpiritualTypes.FIRE, 15,
                        SpiritualTypes.EARTH, 10
                )
        );
        genElixir(consumer, ImmortalItems.GATHER_BREATH_ELIXIR.get(), 800, 2400, 15, 1,
                Arrays.asList(
                        GourdGrownBlock.GourdTypes.RED.getGourdItem(),
                        GourdGrownBlock.GourdTypes.ORANGE.getGourdItem(),
                        GourdGrownBlock.GourdTypes.YELLOW.getGourdItem()
                ), Map.of(
                        SpiritualTypes.METAL, 15,
                        SpiritualTypes.WOOD, 15,
                        SpiritualTypes.WATER, 15,
                        SpiritualTypes.FIRE, 15,
                        SpiritualTypes.EARTH, 15,
                        SpiritualTypes.WIND, 15
                )
        );
//        genElixir(consumer, ImmortalItems.ANTIDOTE_ELIXIR.get(), 800, 2400, 15, 1,
//                Arrays.asList(
//                        GourdGrownBlock.GourdTypes.RED.getGourdItem(),
//                        GourdGrownBlock.GourdTypes.ORANGE.getGourdItem(),
//                        GourdGrownBlock.GourdTypes.YELLOW.getGourdItem()
//                ), Map.of(
//                        SpiritualTypes.METAL, 15,
//                        SpiritualTypes.WOOD, 15,
//                        SpiritualTypes.WATER, 15,
//                        SpiritualTypes.FIRE, 15,
//                        SpiritualTypes.EARTH, 15,
//                        SpiritualTypes.WIND, 15
//                )
//        );
    }

    private void genSmithing(Consumer<FinishedRecipe> consumer, ItemLike result, int requireLevel, boolean needRecovery, int needSmithingValue, float speedMultiple, List<String> pattern, Consumer<SmithingRecipeBuilder> patternConsumer){
        SmithingRecipeBuilder builder = new SmithingRecipeBuilder(result, 1, requireLevel, needRecovery, needSmithingValue, speedMultiple)
                .unlockedBy("has_smithing_artifact", has(ImmortalBlocks.COPPER_SMITHING_ARTIFACT.get()));
        pattern.forEach(builder::pattern);
        patternConsumer.accept(builder);
        builder.save(consumer, Util.prefix("smithing/" + ItemHelper.getKey(result.asItem()).getPath()));
    }

    private void genElixir(Consumer<FinishedRecipe> consumer, ItemLike result, int preCD, int smeltCD, int ingredientLimit, int requireFlameLevel, List<ItemLike> ingredients, Map<ISpiritualType, Integer> map){
        ElixirRecipeBuilder builder = new ElixirRecipeBuilder(result, 1, preCD, smeltCD, ingredientLimit, requireFlameLevel);
        builder.unlockedBy("has_elixir_furnace", has(ImmortalBlocks.COPPER_ELIXIR_ROOM.get()));
        ingredients.forEach(builder::requires);
        map.forEach(builder::put);
        builder.save(consumer, Util.prefix("elixir/" + ItemHelper.getKey(result.asItem()).getPath()));
    }

}
