package hungteen.imm.data.recipe;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import hungteen.htlib.data.recipe.HTFinishedRecipe;
import hungteen.htlib.data.recipe.HTRecipeBuilder;
import hungteen.htlib.util.helper.StringHelper;
import hungteen.htlib.util.helper.registry.RecipeHelper;
import hungteen.imm.common.recipe.IMMRecipeSerializers;
import hungteen.imm.util.RecipeUtil;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-29 12:34
 **/
public class ElixirRecipeBuilder extends HTRecipeBuilder {

    private final List<Ingredient> ingredients = Lists.newArrayList();
    private final int count;
    private final int smeltingCD;
    private final int requireLevel;

    public ElixirRecipeBuilder(ItemLike itemLike, int count, int smeltingCD, int requireLevel) {
        super(itemLike.asItem());
        this.count = count;
        this.smeltingCD = smeltingCD;
        this.requireLevel = requireLevel;
    }

    public ElixirRecipeBuilder requires(TagKey<Item> tagKey) {
        return this.requires(Ingredient.of(tagKey));
    }

    public ElixirRecipeBuilder requires(ItemLike itemLike) {
        return this.requires(itemLike, 1);
    }

    public ElixirRecipeBuilder requires(ItemLike itemLike, int count) {
        for (int i = 0; i < count; ++i) {
            this.requires(Ingredient.of(itemLike));
        }
        return this;
    }

    public ElixirRecipeBuilder requires(Ingredient ingredient) {
        return this.requires(ingredient, 1);
    }

    public ElixirRecipeBuilder requires(Ingredient ingredient, int count) {
        for (int i = 0; i < count; ++i) {
            this.ingredients.add(ingredient);
        }
        return this;
    }

    @Override
    public FinishedRecipe createFinishedRecipe(ResourceLocation location) {
        return new Result(
                location,
                this.getResult(),
                this.count,
                this.smeltingCD,
                this.requireLevel,
                this.getGroup(),
                this.ingredients,
                this.getAdvancement(),
                StringHelper.prefix(location, "recipes/elixirs/" + location.getPath())
        );
    }

    public static class Result extends HTFinishedRecipe {
        private final Item result;
        private final int count;
        private final String group;
        private final List<Ingredient> ingredients;
        private final int smeltingCD;
        private final int requireLevel;

        public Result(ResourceLocation id, Item result, int count, int smeltingCD, int requireLevel, String group, List<Ingredient> ingredients, Advancement.Builder builder, ResourceLocation advancement) {
            super(id, builder, advancement);
            this.result = result;
            this.count = count;
            this.smeltingCD = smeltingCD;
            this.requireLevel = requireLevel;
            this.group = group;
            this.ingredients = ingredients;
        }

        @Override
        public void serializeRecipeData(JsonObject jsonObject) {
            RecipeUtil.writeGroup(jsonObject, this.group);
            RecipeUtil.writeIngredients(jsonObject, this.ingredients);
            RecipeHelper.writeResultItem(jsonObject, this.result, this.count);
            jsonObject.addProperty("smelting_cd", this.smeltingCD);
            jsonObject.addProperty("require_level", this.requireLevel);
        }

        @Override
        public RecipeSerializer<?> getType() {
            return IMMRecipeSerializers.ELIXIR.get();
        }

    }
}
