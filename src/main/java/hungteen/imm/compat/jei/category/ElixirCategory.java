package hungteen.imm.compat.jei.category;

import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.blockentity.ElixirRoomBlockEntity;
import hungteen.imm.common.recipe.ElixirRecipe;
import hungteen.imm.compat.jei.IMMJEIUtil;
import hungteen.imm.util.Util;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-04 12:56
 **/
public class ElixirCategory implements IRecipeCategory<ElixirRecipe> {

    public static final RecipeType<ElixirRecipe> ELIXIR_RECIPE_TYPE = RecipeType.create(Util.id(), "elixir", ElixirRecipe.class);

    private final Component title;
    private final ICraftingGridHelper craftingGridHelper;
    private final IDrawable background;
    private final IDrawable icon;

    public ElixirCategory(IGuiHelper helper) {
        this.title = ElixirRoomBlockEntity.TITLE;
        this.craftingGridHelper = helper.createCraftingGridHelper();
        this.background = helper.createDrawable(IMMJEIUtil.CRAFTING_TABLE, 29, 16, 116, 54);
        this.icon = helper.createDrawableItemStack(new ItemStack(IMMBlocks.COPPER_ELIXIR_ROOM.get()));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ElixirRecipe recipe, IFocusGroup focuses) {
        this.craftingGridHelper.createAndSetInputs(builder,  recipe.getIngredients().stream().map(ingredient -> Arrays.stream(ingredient.getItems()).toList()).toList(), 3, 3);
        Util.getProxy().registryAccess().ifPresent(access -> {
            this.craftingGridHelper.createAndSetOutputs(builder, List.of(recipe.getResultItem(access)));
        });
    }

    @Override
    public Component getTitle() {
        return title;
    }

//    @Override
//    public IDrawable getBackground() {
//        return background;
//    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public RecipeType<ElixirRecipe> getRecipeType() {
        return ELIXIR_RECIPE_TYPE;
    }
}
