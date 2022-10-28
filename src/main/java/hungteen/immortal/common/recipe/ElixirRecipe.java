package hungteen.immortal.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import hungteen.immortal.common.blockentity.ElixirFurnaceBlockEntity;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-28 22:21
 **/
public class ElixirRecipe implements Recipe<ElixirFurnaceBlockEntity> {

    private final ResourceLocation id;
    final String group;
    final ItemStack result;
    final NonNullList<Ingredient> ingredients;
    private final boolean isSimple;

    public ElixirRecipe(ResourceLocation location, String group, NonNullList<Ingredient> ingredients, ItemStack result) {
        this.id = location;
        this.group = group;
        this.result = result;
        this.ingredients = ingredients;
        this.isSimple = ingredients.stream().allMatch(Ingredient::isSimple);
    }

    @Override
    public boolean matches(ElixirFurnaceBlockEntity container, Level level) {
        StackedContents stackedcontents = new StackedContents();
        java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
        int i = 0;
        for(int j = 0; j < container.getContainerSize(); ++j) {
            ItemStack itemstack = container.getItem(j);
            if (!itemstack.isEmpty()) {
                ++ i;
                if (isSimple){
                    stackedcontents.accountStack(itemstack, 1);
                } else {
                    inputs.add(itemstack);
                }
            }
        }

        return i == this.ingredients.size() && (isSimple ? stackedcontents.canCraft(this, (IntList)null) : net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs,  this.ingredients) != null);

    }

    @Override
    public ItemStack assemble(ElixirFurnaceBlockEntity container) {
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
                return new ElixirRecipe(location, s, ingredients, itemstack);
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
            return new ElixirRecipe(location, s, ingredients, itemstack);
        }

        public void toNetwork(FriendlyByteBuf byteBuf, ElixirRecipe recipe) {
            byteBuf.writeUtf(recipe.group);
            byteBuf.writeVarInt(recipe.ingredients.size());

            for(Ingredient ingredient : recipe.ingredients) {
                ingredient.toNetwork(byteBuf);
            }

            byteBuf.writeItem(recipe.result);
        }
    }

}
