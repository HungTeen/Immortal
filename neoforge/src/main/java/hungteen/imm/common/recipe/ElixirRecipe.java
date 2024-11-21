package hungteen.imm.common.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.htlib.util.helper.impl.RecipeHelper;
import hungteen.imm.util.CodecUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-28 22:21
 **/
public class ElixirRecipe implements Recipe<CraftingInput> {

    private final String group;
    private final NonNullList<Ingredient> ingredients;
    private final ItemStack result;
    private final int smeltingCD;
    private final int requireLevel;
    private final boolean isSimple;

    public ElixirRecipe(String group, NonNullList<Ingredient> ingredients, ItemStack result, int smeltingCD, int requireLevel) {
        this.group = group;
        this.result = result;
        this.ingredients = ingredients;
        this.smeltingCD = smeltingCD;
        this.requireLevel = requireLevel;
        this.isSimple = ingredients.stream().allMatch(Ingredient::isSimple);
    }

    /**
     */
    @Override
    public boolean matches(CraftingInput container, Level level) {
        final StackedContents contents = new StackedContents();
        final List<ItemStack> inputs = new ArrayList<>();
        int cnt = 0;
        for(int j = 0; j < container.items().size(); ++j) {
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
    public ItemStack assemble(CraftingInput container, HolderLookup.Provider provider) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int p_44252_, int p_44253_) {
        return p_44252_ * p_44253_ >= this.ingredients.size();
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registryAccess) {
        return this.result;
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

        private static final MapCodec<ElixirRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(ElixirRecipe::getGroup),
                CodecUtil.readIngredientsCodec().forGetter((p_300975_) -> p_300975_.ingredients),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter((p_301142_) -> p_301142_.result),
                Codec.INT.fieldOf("smelting_cd").forGetter((p_301142_) -> p_301142_.smeltingCD),
                Codec.INT.fieldOf("require_level").forGetter((p_301142_) -> p_301142_.requireLevel)
        ).apply(instance, ElixirRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, ElixirRecipe> STREAM_CODEC = StreamCodec.of(ElixirRecipe.Serializer::toNetwork, ElixirRecipe.Serializer::fromNetwork);

        public static ElixirRecipe fromNetwork(RegistryFriendlyByteBuf byteBuf) {
            final String group = byteBuf.readUtf();
            final NonNullList<Ingredient> ingredients = RecipeHelper.readIngredients(byteBuf);
            final ItemStack itemstack = ItemStack.STREAM_CODEC.decode(byteBuf);
            final int smeltingCD = byteBuf.readInt();
            final int requireLevel = byteBuf.readInt();
            return new ElixirRecipe(group, ingredients, itemstack, smeltingCD, requireLevel);
        }

        public static void toNetwork(RegistryFriendlyByteBuf byteBuf, ElixirRecipe recipe) {
            byteBuf.writeUtf(recipe.group);
            RecipeHelper.writeIngredients(byteBuf, recipe.getIngredients());
            ItemStack.STREAM_CODEC.encode(byteBuf, recipe.result);
            byteBuf.writeInt(recipe.smeltingCD);
            byteBuf.writeInt(recipe.requireLevel);
        }

        @Override
        public MapCodec<ElixirRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ElixirRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

}
