package hungteen.immortal.data.recipe;

import hungteen.immortal.api.registry.ISpiritualRoot;
import hungteen.immortal.common.block.ImmortalBlocks;
import hungteen.immortal.common.block.plants.GourdGrownBlock;
import hungteen.immortal.common.item.ImmortalItems;
import hungteen.immortal.impl.SpiritualRoots;
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
     * Recipes for Elixir Furnace.
     */
    protected void buildElixirRecipes(Consumer<FinishedRecipe> consumer) {
        genElixir(consumer, ImmortalItems.FIVE_FLOWERS_ELIXIR.get(), 600, 1200, 10, 1,
                Arrays.asList(
                        Items.SUNFLOWER, Items.LILAC, Items.ROSE_BUSH, Items.PEONY, Items.SPORE_BLOSSOM
                ), Map.of(
                        SpiritualRoots.METAL, 5,
                        SpiritualRoots.WOOD, 5,
                        SpiritualRoots.WATER, 5,
                        SpiritualRoots.FIRE, 5,
                        SpiritualRoots.EARTH, 5
                )
        );
        genElixir(consumer, ImmortalItems.ANTIDOTE_ELIXIR.get(), 600, 1200, 10, 1,
                Arrays.asList(
                        Items.MILK_BUCKET, Items.GOLDEN_CARROT, Items.GLISTERING_MELON_SLICE
                ), Map.of(
                        SpiritualRoots.WOOD, 5, 
                        SpiritualRoots.WATER, 5,
                        SpiritualRoots.FIRE, 5,
                        SpiritualRoots.EARTH, 5,
                        SpiritualRoots.DRUG, 10
                )
        );
        genElixir(consumer, ImmortalItems.SPIRIT_RECOVERY_ELIXIR.get(), 600, 300, 10, 1,
                Arrays.asList(
                        Items.BROWN_MUSHROOM, Items.RED_MUSHROOM, Items.SWEET_BERRIES, Items.BEETROOT
                ), Map.of(
                        SpiritualRoots.METAL, 5,
                        SpiritualRoots.WOOD, 10,
                        SpiritualRoots.WATER, 5,
                        SpiritualRoots.FIRE, 5,
                        SpiritualRoots.EARTH, 5
                )
        );
        genElixir(consumer, ImmortalItems.ABSTINENCE_ELIXIR.get(), 600, 2400, 12, 1,
                Arrays.asList(
                        Items.WHEAT, Items.EGG, Items.SUGAR, Items.MILK_BUCKET, Items.GOLDEN_APPLE
                ), Map.of(
                        SpiritualRoots.METAL, 5,
                        SpiritualRoots.WOOD, 10,
                        SpiritualRoots.WATER, 15,
                        SpiritualRoots.FIRE, 15,
                        SpiritualRoots.EARTH, 10
                )
        );
        genElixir(consumer, ImmortalItems.GATHER_BREATH_ELIXIR.get(), 800, 2400, 15, 1,
                Arrays.asList(
                        GourdGrownBlock.GourdTypes.RED.getGourdItem(),
                        GourdGrownBlock.GourdTypes.ORANGE.getGourdItem(),
                        GourdGrownBlock.GourdTypes.YELLOW.getGourdItem()
                ), Map.of(
                        SpiritualRoots.METAL, 15,
                        SpiritualRoots.WOOD, 15,
                        SpiritualRoots.WATER, 15,
                        SpiritualRoots.FIRE, 15,
                        SpiritualRoots.EARTH, 15,
                        SpiritualRoots.WIND, 15
                )
        );
//        genElixir(consumer, ImmortalItems.ANTIDOTE_ELIXIR.get(), 800, 2400, 15, 1,
//                Arrays.asList(
//                        GourdGrownBlock.GourdTypes.RED.getGourdItem(),
//                        GourdGrownBlock.GourdTypes.ORANGE.getGourdItem(),
//                        GourdGrownBlock.GourdTypes.YELLOW.getGourdItem()
//                ), Map.of(
//                        SpiritualRoots.METAL, 15,
//                        SpiritualRoots.WOOD, 15,
//                        SpiritualRoots.WATER, 15,
//                        SpiritualRoots.FIRE, 15,
//                        SpiritualRoots.EARTH, 15,
//                        SpiritualRoots.WIND, 15
//                )
//        );
    }

    private void genElixir(Consumer<FinishedRecipe> consumer, ItemLike result, int preCD, int smeltCD, int ingredientLimit, int requireFlameLevel, List<ItemLike> ingredients, Map<ISpiritualRoot, Integer> map){
        ElixirRecipeBuilder builder = new ElixirRecipeBuilder(result, 1, preCD, smeltCD, ingredientLimit, requireFlameLevel).unlockedBy("has_elixir_furnace", has(ImmortalBlocks.ELIXIR_ROOM.get()));
        ingredients.forEach(builder::requires);
        map.forEach(builder::put);
        builder.save(consumer, Util.prefix("elixir/" + result.asItem().getRegistryName().getPath()));
    }

}
