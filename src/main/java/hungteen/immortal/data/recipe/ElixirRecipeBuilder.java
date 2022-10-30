package hungteen.immortal.data.recipe;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import hungteen.immortal.api.registry.ISpiritualRoot;
import hungteen.immortal.common.recipe.ImmortalRecipes;
import hungteen.immortal.utils.Util;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-29 12:34
 **/
public class ElixirRecipeBuilder implements RecipeBuilder {

    private final Item result;
    private final int count;
    private final List<Ingredient> ingredients = Lists.newArrayList();
    private final Map<ISpiritualRoot, Integer> spiritualMap = new HashMap<>();
    private final Advancement.Builder advancement = Advancement.Builder.advancement();
    @javax.annotation.Nullable
    private String group;
    private final int prepareCD;
    private final int smeltingCD;
    private final int ingredientLimit;

    public ElixirRecipeBuilder(ItemLike itemLike, int count, int prepareCD, int smeltingCD, int ingredientLimit) {
        this.result = itemLike.asItem();
        this.count = count;
        this.prepareCD = prepareCD;
        this.smeltingCD = smeltingCD;
        this.ingredientLimit = ingredientLimit;
    }

    public static ElixirRecipeBuilder elixir(ItemLike itemLike, int prepareCD, int smeltingCD, int ingredientLimit) {
        return new ElixirRecipeBuilder(itemLike, 1, prepareCD, smeltingCD, ingredientLimit);
    }

    public static ElixirRecipeBuilder elixir(ItemLike itemLike, int count, int prepareCD, int smeltingCD, int ingredientLimit) {
        return new ElixirRecipeBuilder(itemLike, count, prepareCD, smeltingCD, ingredientLimit);
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

    public ElixirRecipeBuilder requires(Ingredient p_126187_, int count) {
        for (int i = 0; i < count; ++i) {
            this.ingredients.add(p_126187_);
        }
        return this;
    }

    @Override
    public ElixirRecipeBuilder unlockedBy(String name, CriterionTriggerInstance triggerInstance) {
        this.advancement.addCriterion(name, triggerInstance);
        return this;
    }

    @Override
    public ElixirRecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    public ElixirRecipeBuilder put(ISpiritualRoot root, int value) {
        this.spiritualMap.put(root, value);
        return this;
    }

    @Override
    public Item getResult() {
        return this.result;
    }

    @Override
    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation location) {
        this.ensureValid(location);
        this.advancement.parent(Util.mcPrefix("recipes/root"))
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(location))
                .rewards(AdvancementRewards.Builder.recipe(location))
                .requirements(RequirementsStrategy.OR);
        consumer.accept(new Result(
                        location,
                        this.result,
                        this.count,
                        this.prepareCD,
                        this.smeltingCD,
                        this.ingredientLimit,
                        this.group == null ? "" : this.group,
                        this.ingredients,
                        this.spiritualMap,
                        this.advancement,
                        new ResourceLocation(location.getNamespace(), "recipes/" + this.result.getItemCategory().getRecipeFolderName() + "/" + location.getPath())
                )
        );
    }

    private void ensureValid(ResourceLocation location) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + location);
        }
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final Item result;
        private final int count;
        private final String group;
        private final List<Ingredient> ingredients;
        private final Map<ISpiritualRoot, Integer> spiritualMap;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;
        private final int prepareCD;
        private final int smeltingCD;
        private final int ingredientLimit;

        public Result(ResourceLocation location, Item result, int count, int prepareCD, int smeltingCD, int ingredientLimit, String group, List<Ingredient> ingredients, Map<ISpiritualRoot, Integer> spiritualMap, Advancement.Builder builder, ResourceLocation advancement) {
            this.id = location;
            this.result = result;
            this.count = count;
            this.prepareCD = prepareCD;
            this.smeltingCD = smeltingCD;
            this.ingredientLimit = ingredientLimit;
            this.group = group;
            this.ingredients = ingredients;
            this.spiritualMap = spiritualMap;
            this.advancement = builder;
            this.advancementId = advancement;
        }

        @Override
        public void serializeRecipeData(JsonObject jsonObject) {
            if (!this.group.isEmpty()) {
                jsonObject.addProperty("group", this.group);
            }
            JsonArray jsonarray = new JsonArray();
            for (Ingredient ingredient : this.ingredients) {
                jsonarray.add(ingredient.toJson());
            }
            jsonObject.add("ingredients", jsonarray);
            JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("item", Registry.ITEM.getKey(this.result).toString());
            if (this.count > 1) {
                jsonobject.addProperty("count", this.count);
            }
            jsonObject.add("result", jsonobject);
            jsonObject.addProperty("prepare_cd", this.prepareCD);
            jsonObject.addProperty("smelting_cd", this.smeltingCD);
            jsonObject.addProperty("ingredient_limit", this.ingredientLimit);

            JsonArray map = new JsonArray();
            this.spiritualMap.forEach((root, value) -> {
                final JsonObject obj = new JsonObject();
                obj.addProperty("spiritual_root", root.getRegistryName());
                obj.addProperty("spiritual_value", value);
                map.add(obj);
            });
            jsonObject.add("spiritual_map", map);
        }

        @Override
        public RecipeSerializer<?> getType() {
            return ImmortalRecipes.ELIXIR_SERIALIZER.get();
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        @javax.annotation.Nullable
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Override
        @javax.annotation.Nullable
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}
