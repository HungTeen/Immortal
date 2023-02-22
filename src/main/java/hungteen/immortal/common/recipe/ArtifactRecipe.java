package hungteen.immortal.common.recipe;

import hungteen.htlib.common.menu.container.SimpleCraftingContainer;
import hungteen.immortal.common.item.ImmortalItems;
import hungteen.immortal.common.item.artifacts.RawArtifactBox;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-28 22:21
 **/
public abstract class ArtifactRecipe implements Recipe<SimpleCraftingContainer> {

    protected final ResourceLocation id;
    protected final String group;
    protected final NonNullList<Ingredient> ingredients;
    protected final ItemStack result;
    protected final int width;
    protected final int height;
    protected final int requireLevel;
    protected final boolean needRecovery;

    public ArtifactRecipe(ResourceLocation id, String group, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result, int requireLevel, boolean needRecovery) {
        this.id = id;
        this.group = group;
        this.width = width;
        this.height = height;
        this.result = result;
        this.ingredients = ingredients;
        this.requireLevel = requireLevel;
        this.needRecovery = needRecovery;
    }

    @Override
    public boolean matches(SimpleCraftingContainer container, Level level) {
        for (int i = 0; i <= container.getHeight() - this.height; ++i) {
            for (int j = 0; j <= container.getWidth() - this.width; ++j) {
                if (this.matches(container, i, j)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean matches(SimpleCraftingContainer container, int posX, int posY) {
        for(int i = 0; i < container.getHeight(); ++i) {
            for(int j = 0; j < container.getWidth(); ++j) {
                final int currentX = i - posX;
                final int currentY = j - posY;
                Ingredient ingredient = Ingredient.EMPTY;
                if(currentX >= 0 && currentX < this.height && currentY >= 0 && currentY < this.width) {
                    ingredient = this.ingredients.get(currentY + currentX * this.width);
                }
                if(! ingredient.test(container.getItem(j + i * container.getWidth()))){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(SimpleCraftingContainer container) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int p_44252_, int p_44253_) {
        return p_44252_ * p_44253_ >= this.ingredients.size();
    }

    @Override
    public ItemStack getResultItem() {
        if(this.needRecovery){
            ItemStack stack = new ItemStack(ImmortalItems.RAW_ARTIFACT_BOX.get());
            RawArtifactBox.setArtifactItem(stack, this.result.copy());
            return stack;
        }
        return this.result.copy();
    }

    public int getRequireLevel() {
        return requireLevel;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public String getGroup() {
        return group;
    }

    /**
     * Copy from {@link ShapedRecipe.Serializer}.
     */
    public static abstract class BaseSerializer<T extends ArtifactRecipe> implements RecipeSerializer<ArtifactRecipe> {

//         @Override
//         public ArtifactRecipe fromJson(ResourceLocation location, JsonObject jsonObject) {
//            final String group = GsonHelper.getAsString(jsonObject, "group", "");
//            final Map<String, Ingredient> map = ShapedRecipe.keyFromJson(GsonHelper.getAsJsonObject(jsonObject, "key"));
//            final String[] patterns = ShapedRecipe.shrink(ShapedRecipe.patternFromJson(GsonHelper.getAsJsonArray(jsonObject, "pattern")));
//            final int width = patterns[0].length();
//            final int height = patterns.length;
//            final NonNullList<Ingredient> ingredients = ShapedRecipe.dissolvePattern(patterns, map, width, height);
//            final ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
//            final int requireLevel = GsonHelper.getAsInt(jsonObject, "require_level", 0);
//            final boolean needRecovery = GsonHelper.getAsBoolean(jsonObject, "need_recovery", true);
//            return new ArtifactRecipe(location, group, width, height, ingredients, result, requireLevel, needRecovery);
//        }
//
//        @Override
//        public ArtifactRecipe fromNetwork(ResourceLocation location, FriendlyByteBuf byteBuf) {
//            String group = byteBuf.readUtf();
//            final int width = byteBuf.readVarInt();
//            final int height = byteBuf.readVarInt();
//            final int requireLevel = byteBuf.readVarInt();
//            final boolean needRecovery = byteBuf.readBoolean();
//            NonNullList<Ingredient> ingredients = NonNullList.withSize(width * height, Ingredient.EMPTY);
//            for(int k = 0; k < ingredients.size(); ++k) {
//                ingredients.set(k, Ingredient.fromNetwork(byteBuf));
//            }
//            ItemStack result = byteBuf.readItem();
//            return new ArtifactRecipe(location, group, width, height, ingredients, result, requireLevel, needRecovery);
//        }
//
//        @Override
//        public void toNetwork(FriendlyByteBuf byteBuf, ArtifactRecipe recipe) {
//            byteBuf.writeUtf(recipe.group);
//            byteBuf.writeVarInt(recipe.width);
//            byteBuf.writeVarInt(recipe.height);
//            byteBuf.writeVarInt(recipe.requireLevel);
//            byteBuf.writeBoolean(recipe.needRecovery);
//
//            for(Ingredient ingredient : recipe.ingredients) {
//                ingredient.toNetwork(byteBuf);
//            }
//            byteBuf.writeItem(recipe.result);
//        }
    }

}
