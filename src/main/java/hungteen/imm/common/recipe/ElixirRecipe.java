package hungteen.imm.common.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import hungteen.htlib.util.helper.registry.RecipeHelper;
import hungteen.imm.util.RecipeUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;

import java.util.ArrayList;
import java.util.List;

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
    private final int smeltingCD;
    private final int requireLevel;
    private final boolean isSimple;

    public ElixirRecipe(ResourceLocation id, String group, NonNullList<Ingredient> ingredients, ItemStack result, int smeltingCD, int requireLevel) {
        this.id = id;
        this.group = group;
        this.result = result;
        this.ingredients = ingredients;
        this.smeltingCD = smeltingCD;
        this.requireLevel = requireLevel;
        this.isSimple = ingredients.stream().allMatch(Ingredient::isSimple);
    }

    /**
     * Copy from {@link ShapelessRecipe#matches(CraftingContainer, Level)}.
     */
    @Override
    public boolean matches(SimpleContainer container, Level level) {
        final StackedContents contents = new StackedContents();
        final List<ItemStack> inputs = new ArrayList<>();
        int cnt = 0;
        for(int j = 0; j < container.getContainerSize(); ++j) {
            ItemStack itemstack = container.getItem(j);
            if (!itemstack.isEmpty()) {
                ++ cnt;
                if(isSimple){
                    contents.accountStack(itemstack, 1);
                } else {
                    inputs.add(itemstack);
                }

            }
        }
        return cnt == this.ingredients.size() && (isSimple ? contents.canCraft(this, null) : RecipeMatcher.findMatches(inputs, this.ingredients) != null);
    }

    @Override
    public ItemStack assemble(SimpleContainer container, RegistryAccess registryAccess) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int p_44252_, int p_44253_) {
        return p_44252_ * p_44253_ >= this.ingredients.size();
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.result;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return IMMRecipeSerializers.ELIXIR.get();
    }

    @Override
    public RecipeType<?> getType() {
        return IMMRecipes.ELIXIR.get();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public String getGroup() {
        return group;
    }


    public int getSmeltingCD() {
        return smeltingCD;
    }

    public int getRequireLevel() {
        return requireLevel;
    }

    /**
     * Copy from {@link ShapelessRecipe.Serializer}.
     */
    public static class Serializer implements RecipeSerializer<ElixirRecipe> {

        @Override
        public ElixirRecipe fromJson(ResourceLocation location, JsonObject jsonObject) {
            final String group = RecipeUtil.readGroup(jsonObject);
            final NonNullList<Ingredient> ingredients = RecipeUtil.readIngredients(jsonObject);
            if (ingredients.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            } else if (ingredients.size() > 9) {
                throw new JsonParseException("Too many ingredients for shapeless recipe. The maximum is 9");
            } else {
                final ItemStack itemstack = RecipeHelper.readResultItem(jsonObject);
                final int smeltingCD = GsonHelper.getAsInt(jsonObject, "smelting_cd", 1200);
                final int requireLevel = GsonHelper.getAsInt(jsonObject, "require_level");
                return new ElixirRecipe(location, group, ingredients, itemstack, smeltingCD, requireLevel);
            }
        }

        @Override
        public ElixirRecipe fromNetwork(ResourceLocation location, FriendlyByteBuf byteBuf) {
            final String group = byteBuf.readUtf();
            final NonNullList<Ingredient> ingredients = RecipeHelper.readIngredients(byteBuf);
            final ItemStack itemstack = byteBuf.readItem();
            final int smeltingCD = byteBuf.readInt();
            final int requireLevel = byteBuf.readInt();
            return new ElixirRecipe(location, group, ingredients, itemstack, smeltingCD, requireLevel);
        }

        @Override
        public void toNetwork(FriendlyByteBuf byteBuf, ElixirRecipe recipe) {
            byteBuf.writeUtf(recipe.group);
            RecipeHelper.writeIngredients(byteBuf, recipe.getIngredients());
            byteBuf.writeItem(recipe.result);
            byteBuf.writeInt(recipe.smeltingCD);
            byteBuf.writeInt(recipe.requireLevel);
        }
    }

}
