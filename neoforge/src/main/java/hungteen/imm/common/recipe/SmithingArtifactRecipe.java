package hungteen.imm.common.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-11-13 23:23
 **/
public class SmithingArtifactRecipe extends ArtifactRecipe{

    private final int needSmithingValue;
    private final float speedMultiple;

    public SmithingArtifactRecipe(ResourceLocation id, String group, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result, int requireLevel, boolean needRecovery, int needSmithingValue, float speedMultiple) {
        super(id, group, width, height, ingredients, result, requireLevel, needRecovery);
        this.needSmithingValue = needSmithingValue;
        this.speedMultiple = speedMultiple;
    }

    public int getSmithingValue() {
        return needSmithingValue;
    }

    public float getSpeedMultiple() {
        return speedMultiple;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return IMMRecipeSerializers.SMITHING_ARTIFACT.get();
    }

    @Override
    public RecipeType<?> getType() {
        return IMMRecipes.SMITHING_ARTIFACT.get();
    }

    /**
     * Copy from {@link ArtifactRecipe.BaseSerializer}.
     */
    public static class Serializer implements RecipeSerializer<SmithingArtifactRecipe> {

//        public SmithingArtifactRecipe fromJson(ResourceLocation location, JsonObject jsonObject) {
//            final String group = GsonHelper.getAsString(jsonObject, "group", "");
//            final Map<String, Ingredient> map = ShapedRecipe.keyFromJson(GsonHelper.getAsJsonObject(jsonObject, "key"));
//            final String[] patterns = ShapedRecipe.shrink(ShapedRecipe.patternFromJson(GsonHelper.getAsJsonArray(jsonObject, "pattern")));
//            final int width = patterns[0].length();
//            final int height = patterns.length;
//            final NonNullList<Ingredient> ingredients = ShapedRecipe.dissolvePattern(patterns, map, width, height);
//            final ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
//            final int requireLevel = GsonHelper.getAsInt(jsonObject, "require_level", 0);
//            final boolean needRecovery = GsonHelper.getAsBoolean(jsonObject, "need_recovery", true);
//            final int smithingValue = GsonHelper.getAsInt(jsonObject, "require_smithing_value", 100);
//            final float speedMultiple = GsonHelper.getAsFloat(jsonObject, "speed_multiple", 1F);
//            return new SmithingArtifactRecipe(location, group, width, height, ingredients, result, requireLevel, needRecovery, smithingValue, speedMultiple);
//        }
//
//        public SmithingArtifactRecipe fromNetwork(ResourceLocation location, FriendlyByteBuf byteBuf) {
//            String group = byteBuf.readUtf();
//            final int width = byteBuf.readVarInt();
//            final int height = byteBuf.readVarInt();
//            final int requireLevel = byteBuf.readVarInt();
//            final boolean needRecovery = byteBuf.readBoolean();
//            final int smithingValue = byteBuf.readVarInt();
//            final float speedMultiple = byteBuf.readFloat();
//            NonNullList<Ingredient> ingredients = NonNullList.withSize(width * height, Ingredient.EMPTY);
//            for(int k = 0; k < ingredients.size(); ++k) {
//                ingredients.set(k, Ingredient.fromNetwork(byteBuf));
//            }
//            ItemStack result = byteBuf.readItem();
//            return new SmithingArtifactRecipe(location, group, width, height, ingredients, result, requireLevel, needRecovery, smithingValue, speedMultiple);
//        }
//
//        public void toNetwork(FriendlyByteBuf byteBuf, SmithingArtifactRecipe recipe) {
//            byteBuf.writeUtf(recipe.group);
//            byteBuf.writeVarInt(recipe.width);
//            byteBuf.writeVarInt(recipe.height);
//            byteBuf.writeVarInt(recipe.requireLevel);
//            byteBuf.writeBoolean(recipe.needRecovery);
//            byteBuf.writeVarInt(recipe.needSmithingValue);
//            byteBuf.writeFloat(recipe.speedMultiple);
//
//            for(Ingredient ingredient : recipe.ingredients) {
//                ingredient.toNetwork(byteBuf);
//            }
//            byteBuf.writeItem(recipe.result);
//        }

        @Override
        public MapCodec<SmithingArtifactRecipe> codec() {
            return null;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SmithingArtifactRecipe> streamCodec() {
            return null;
        }
    }
}
