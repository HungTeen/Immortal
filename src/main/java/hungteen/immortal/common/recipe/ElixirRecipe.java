package hungteen.immortal.common.recipe;

import com.google.gson.*;
import hungteen.htlib.util.Pair;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.registry.ISpiritualRoot;
import hungteen.immortal.utils.Util;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;

import java.util.*;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-28 22:21
 **/
public class ElixirRecipe implements Recipe<SimpleContainer> {

    private final ResourceLocation id;
    private final String group;
    private final NonNullList<Ingredient> ingredients;
    private final ItemStack result;
    private final int prepareCD;
    private final int smeltingCD;
    private final int ingredientLimit;
    private final Map<ISpiritualRoot, Integer> spiritualMap;

    public ElixirRecipe(ResourceLocation id, String group, NonNullList<Ingredient> ingredients, ItemStack result, int prepareCD, int smeltingCD, int ingredientLimit, Map<ISpiritualRoot, Integer> map) {
        this.id = id;
        this.group = group;
        this.result = result;
        this.ingredients = ingredients;
        this.prepareCD = prepareCD;
        this.smeltingCD = smeltingCD;
        this.ingredientLimit = ingredientLimit;
        this.spiritualMap = map;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        List<ItemStack> inputs = new ArrayList<>();
        for(int j = 0; j < container.getContainerSize(); ++j) {
            ItemStack itemstack = container.getItem(j);
            if (!itemstack.isEmpty()) {
                    inputs.add(itemstack);
            }
        }
        return inputs.size() == this.ingredients.size() && RecipeMatcher.findMatches(inputs,  this.ingredients) != null;
    }

    @Override
    public ItemStack assemble(SimpleContainer container) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int p_44252_, int p_44253_) {
        return p_44252_ * p_44253_ >= this.ingredients.size();
    }

    @Override
    public ItemStack getResultItem() {
        return this.result;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ImmortalRecipes.ELIXIR_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ImmortalRecipes.ELIXIR_RECIPE_TYPE;
    }

    @Override
    public String getGroup() {
        return group;
    }

    public int getPrepareCD() {
        return prepareCD;
    }

    public int getSmeltingCD() {
        return smeltingCD;
    }

    public int getIngredientLimit() {
        return ingredientLimit;
    }

    public void put(ISpiritualRoot root, int value) {
        spiritualMap.put(root, value);
    }

    public Map<ISpiritualRoot, Integer> getSpiritualMap() {
        return Collections.unmodifiableMap(spiritualMap);
    }

    /**
     * Copy from {@link ShapelessRecipe.Serializer}.
     */
    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<ElixirRecipe> {

        @Override
        public ElixirRecipe fromJson(ResourceLocation location, JsonObject jsonObject) {
            String s = GsonHelper.getAsString(jsonObject, "group", "");
            NonNullList<Ingredient> ingredients = itemsFromJson(GsonHelper.getAsJsonArray(jsonObject, "ingredients"));
            if (ingredients.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            } else if (ingredients.size() > 9) {
                throw new JsonParseException("Too many ingredients for shapeless recipe. The maximum is 9");
            } else {
                ItemStack itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
                int prepareCD = GsonHelper.getAsInt(jsonObject, "prepare_cd", 600);
                int smeltingCD = GsonHelper.getAsInt(jsonObject, "smelting_cd", 1200);
                int ingredientLimit = GsonHelper.getAsInt(jsonObject, "ingredient_limit", 10);

                JsonArray array = GsonHelper.getAsJsonArray(jsonObject, "spiritual_map", new JsonArray());
                Map<ISpiritualRoot, Integer> map = new HashMap<>();
                for(int i = 0; i < array.size(); ++ i){
                    if(array.get(i).isJsonObject()){
                        final Optional<ISpiritualRoot> opt = ImmortalAPI.get().getSpiritualRoot(GsonHelper.getAsString(array.get(i).getAsJsonObject(), "spiritual_root"));
                        if(opt.isPresent()){
                            final int value = GsonHelper.getAsInt(array.get(i).getAsJsonObject(), "spiritual_value");
                            map.put(opt.get(), value);
                        } else{
                            throw new JsonSyntaxException("Wrong Spiritual Root Type " + GsonHelper.getAsString(array.get(i).getAsJsonObject(), "spiritual_root"));
                        }
                    }
                }
                return new ElixirRecipe(location, s, ingredients, itemstack, prepareCD, smeltingCD, ingredientLimit, map);
            }
        }

        private static NonNullList<Ingredient> itemsFromJson(JsonArray jsonArray) {
            NonNullList<Ingredient> ingredients = NonNullList.create();
            for(int i = 0; i < jsonArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(jsonArray.get(i));
                if (net.minecraftforge.common.ForgeConfig.SERVER.skipEmptyShapelessCheck.get() || !ingredient.isEmpty()) {
                    ingredients.add(ingredient);
                }
            }
            return ingredients;
        }

        @Override
        public ElixirRecipe fromNetwork(ResourceLocation location, FriendlyByteBuf byteBuf) {
            String s = byteBuf.readUtf();
            int i = byteBuf.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(i, Ingredient.EMPTY);
            for(int j = 0; j < ingredients.size(); ++j) {
                ingredients.set(j, Ingredient.fromNetwork(byteBuf));
            }
            ItemStack itemstack = byteBuf.readItem();
            int prepareCD = byteBuf.readInt();
            int smeltingCD = byteBuf.readInt();
            int ingredientLimit = byteBuf.readInt();
            Map<ISpiritualRoot, Integer> map = new HashMap<>();
            final int len = byteBuf.readInt();
            for(int j = 0; j < len; ++ j) {
                final String type = byteBuf.readUtf();
                final int value = byteBuf.readInt();
                ImmortalAPI.get().getSpiritualRoot(type).ifPresent(l -> {
                    map.put(l, value);
                });
            }
            return new ElixirRecipe(location, s, ingredients, itemstack, prepareCD, smeltingCD, ingredientLimit, map);
        }

        public void toNetwork(FriendlyByteBuf byteBuf, ElixirRecipe recipe) {
            byteBuf.writeUtf(recipe.group);
            byteBuf.writeVarInt(recipe.ingredients.size());

            for(Ingredient ingredient : recipe.ingredients) {
                ingredient.toNetwork(byteBuf);
            }

            byteBuf.writeItem(recipe.result);
            byteBuf.writeInt(recipe.prepareCD);
            byteBuf.writeInt(recipe.smeltingCD);
            byteBuf.writeInt(recipe.ingredientLimit);

            byteBuf.writeInt(recipe.spiritualMap.size());
            recipe.spiritualMap.forEach((root, value) -> {
                byteBuf.writeUtf(root.getRegistryName());
                byteBuf.writeInt(value);
            });
        }
    }

}
