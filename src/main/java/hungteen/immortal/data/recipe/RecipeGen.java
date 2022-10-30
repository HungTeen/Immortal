package hungteen.immortal.data.recipe;

import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.registry.ISpiritualRoot;
import hungteen.immortal.common.block.ImmortalBlocks;
import hungteen.immortal.common.block.plants.GourdGrownBlock;
import hungteen.immortal.common.item.ImmortalItems;
import hungteen.immortal.common.item.eixirs.ElixirItem;
import hungteen.immortal.impl.SpiritualRoots;
import hungteen.immortal.utils.Util;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ForgeRecipeProvider;

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
        genElixir(consumer, ImmortalItems.FIVE_FLOWERS_ELIXIR.get(), 600, 1200, 10, Arrays.asList(
                Items.SUNFLOWER, Items.LILAC, Items.ROSE_BUSH, Items.PEONY, Items.SPORE_BLOSSOM
        ), Map.of(
                SpiritualRoots.METAL, 5,
                SpiritualRoots.WOOD, 5,
                SpiritualRoots.WATER, 5,
                SpiritualRoots.FIRE, 5,
                SpiritualRoots.EARTH, 5
        ));
        genElixir(consumer, ImmortalItems.ANTIDOTE_ELIXIR.get(), 600, 1200, 10, Arrays.asList(
                Items.MILK_BUCKET, Items.GOLDEN_CARROT, Items.GLISTERING_MELON_SLICE
        ), Map.of(
                SpiritualRoots.WOOD, 5,
                SpiritualRoots.WATER, 5,
                SpiritualRoots.FIRE, 5,
                SpiritualRoots.EARTH, 5,
                SpiritualRoots.DRUG, 10
        ));
    }

    private void genElixir(Consumer<FinishedRecipe> consumer, ItemLike result, int preCD, int smeltCD, int ingredientLimit, List<ItemLike> ingredients, Map<ISpiritualRoot, Integer> map){
        ElixirRecipeBuilder builder = ElixirRecipeBuilder.elixir(result, preCD, smeltCD, ingredientLimit).unlockedBy("has_elixir_furnace", has(ImmortalBlocks.ELIXIR_FURNACE.get()));
        ingredients.forEach(builder::requires);
        map.forEach(builder::put);
        builder.save(consumer, Util.prefix("elixir/" + result.asItem().getRegistryName().getPath()));
    }

}
