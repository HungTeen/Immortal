package hungteen.imm.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.api.registry.ISpiritualType;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
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
    private final int requireFlameLevel;
    private final Map<ISpiritualType, Integer> spiritualMap;

    public ElixirRecipe(ResourceLocation id, String group, NonNullList<Ingredient> ingredients, ItemStack result, int prepareCD, int smeltingCD, int ingredientLimit, int requireFlameLevel, Map<ISpiritualType, Integer> map) {
        this.id = id;
        this.group = group;
        this.result = result;
        this.ingredients = ingredients;
        this.prepareCD = prepareCD;
        this.smeltingCD = smeltingCD;
        this.ingredientLimit = ingredientLimit;
        this.requireFlameLevel = requireFlameLevel;
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
        return IMMRecipes.ELIXIR_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return IMMRecipes.ELIXIR_RECIPE_TYPE.get();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
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

    public int getRequireFlameLevel() {
        return requireFlameLevel;
    }

    public void put(ISpiritualType root, int value) {
        spiritualMap.put(root, value);
    }

    public Map<ISpiritualType, Integer> getSpiritualMap() {
        return Collections.unmodifiableMap(spiritualMap);
    }

    /**
     * Copy from {@link ShapelessRecipe.Serializer}.
     */
    public static class Serializer implements RecipeSerializer<ElixirRecipe> {

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
                int requireFlameLevel = GsonHelper.getAsInt(jsonObject, "require_flame_level");

                JsonArray array = GsonHelper.getAsJsonArray(jsonObject, "spiritual_map", new JsonArray());
                Map<ISpiritualType, Integer> map = new HashMap<>();
                for(int i = 0; i < array.size(); ++ i){
                    if(array.get(i).isJsonObject() && IMMAPI.get().spiritualRegistry().isPresent()){
                        final Optional<ISpiritualType> opt = IMMAPI.get().spiritualRegistry().get().getValue(GsonHelper.getAsString(array.get(i).getAsJsonObject(), "spiritual_root"));
                        if(opt.isPresent()){
                            final int value = GsonHelper.getAsInt(array.get(i).getAsJsonObject(), "spiritual_value");
                            map.put(opt.get(), value);
                        } else{
                            throw new JsonSyntaxException("Wrong Spiritual Root Type " + GsonHelper.getAsString(array.get(i).getAsJsonObject(), "spiritual_root"));
                        }
                    }
                }
                return new ElixirRecipe(location, s, ingredients, itemstack, prepareCD, smeltingCD, ingredientLimit, requireFlameLevel, map);
            }
        }

        private static NonNullList<Ingredient> itemsFromJson(JsonArray jsonArray) {
            NonNullList<Ingredient> ingredients = NonNullList.create();
            for(int i = 0; i < jsonArray.size(); ++i) {
                ingredients.add(Ingredient.fromJson(jsonArray.get(i)));
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
            int requireFlameLevel = byteBuf.readInt();
            Map<ISpiritualType, Integer> map = new HashMap<>();
            final int len = byteBuf.readInt();
            for(int j = 0; j < len; ++ j) {
                final String type = byteBuf.readUtf();
                final int value = byteBuf.readInt();
                IMMAPI.get().spiritualRegistry().ifPresent(l -> {
                    l.getValue(type).ifPresent(t -> map.put(t, value));
                });
            }
            return new ElixirRecipe(location, s, ingredients, itemstack, prepareCD, smeltingCD, ingredientLimit, requireFlameLevel, map);
        }

        @Override
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
            byteBuf.writeInt(recipe.requireFlameLevel);

            byteBuf.writeInt(recipe.spiritualMap.size());
            recipe.spiritualMap.forEach((root, value) -> {
                byteBuf.writeUtf(root.getRegistryName());
                byteBuf.writeInt(value);
            });
        }
    }

}
