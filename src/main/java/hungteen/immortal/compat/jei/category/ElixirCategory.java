package hungteen.immortal.compat.jei.category;

import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.htlib.HTLib;
import hungteen.immortal.common.block.ImmortalBlocks;
import hungteen.immortal.common.blockentity.ElixirRoomBlockEntity;
import hungteen.immortal.common.recipe.ElixirRecipe;
import hungteen.immortal.utils.Util;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-04 12:56
 **/
public class ElixirCategory implements IRecipeCategory<ElixirRecipe> {

    public static final RecipeType<ElixirRecipe> ELIXIR_RECIPE_TYPE = RecipeType.create(Util.id(), "elixir", ElixirRecipe.class);

    private final IDrawable slotDraw;
    private final IDrawable background;
    private final IDrawable iconDraw;
    private final IDrawable arrowDraw;

    public ElixirCategory(IGuiHelper helper) {
        this.slotDraw = helper.getSlotDrawable();
        this.background = helper.createBlankDrawable(120, 60);
        this.arrowDraw = helper.drawableBuilder(HTLib.WIDGETS, 44, 64, 22, 15).build();
        this.iconDraw = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ImmortalBlocks.ELIXIR_ROOM.get()));
    }

    @Override
    public void draw(ElixirRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        stack.pushPose();
        this.arrowDraw.draw(stack, 50, 23);
        stack.popPose();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ElixirRecipe recipe, IFocusGroup focuses) {
        List<Ingredient> ingredients = recipe.getIngredients();
        for(int i = 0; i < 3; ++ i){
            for(int j = 0; j < 3; ++ j){
                final int id = i * 3 + j;
                if(id < ingredients.size()){
                    builder.addSlot(RecipeIngredientRole.INPUT, i * 18 + 5, j * 18 + 5)
                            .addIngredients(ingredients.get(id));
                }
            }
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 90, 23)
                        .addItemStack(recipe.getResultItem());
    }

    @Override
    public Component getTitle() {
        return ElixirRoomBlockEntity.TITLE;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return iconDraw;
    }

    @Override
    public RecipeType<ElixirRecipe> getRecipeType() {
        return ELIXIR_RECIPE_TYPE;
    }
}
