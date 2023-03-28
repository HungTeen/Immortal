package hungteen.imm.compat.jei.category;

import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.htlib.HTLib;
import hungteen.imm.common.block.ImmortalBlocks;
import hungteen.imm.common.blockentity.SmithingArtifactBlockEntity;
import hungteen.imm.common.recipe.SmithingArtifactRecipe;
import hungteen.imm.utils.Util;
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
 * @create: 2022-11-16 15:19
 **/
public class SmithingCategory implements IRecipeCategory<SmithingArtifactRecipe> {

    public static final RecipeType<SmithingArtifactRecipe> SMITHING_ARTIFACT_RECIPE_TYPE = RecipeType.create(Util.id(), "smithing_artifact", SmithingArtifactRecipe.class);

    private final IDrawable slotDraw;
    private final IDrawable background;
//    private final IDrawable iconDraw;
//    private final IDrawable arrowDraw;

    public SmithingCategory(IGuiHelper helper) {
        this.slotDraw = helper.getSlotDrawable();
        this.background = helper.createBlankDrawable(150, 100);
//        this.arrowDraw = helper.drawableBuilder(HTLib.WIDGETS, 44, 64, 22, 15).build();
//        this.iconDraw = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ImmortalBlocks.COPPER_SMITHING_ARTIFACT.get()));
    }

    @Override
    public void draw(SmithingArtifactRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        stack.pushPose();
//        this.arrowDraw.draw(stack, 50, 23);
        stack.popPose();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SmithingArtifactRecipe recipe, IFocusGroup focuses) {
        List<Ingredient> ingredients = recipe.getIngredients();
        for(int i = 0; i < 5; ++ i){
            for(int j = 0; j < 5; ++ j){
                final int id = i * 5 + j;
                if(id < ingredients.size()){
                    builder.addSlot(RecipeIngredientRole.INPUT, i * 18 + 5, j * 18 + 5)
                            .addIngredients(ingredients.get(id));
                }
            }
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 120, 53)
                .addItemStack(recipe.getResultItem());
    }

    @Override
    public Component getTitle() {
        return Component.empty();
//        return SmithingArtifactBlockEntity.TITLE;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return null;
//        return iconDraw;
    }

    @Override
    public RecipeType<SmithingArtifactRecipe> getRecipeType() {
        return SMITHING_ARTIFACT_RECIPE_TYPE;
    }
}
