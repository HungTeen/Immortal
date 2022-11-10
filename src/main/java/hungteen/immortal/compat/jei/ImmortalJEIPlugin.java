package hungteen.immortal.compat.jei;

import hungteen.immortal.common.block.ImmortalBlocks;
import hungteen.immortal.common.menu.ElixirRoomMenu;
import hungteen.immortal.common.menu.ImmortalMenus;
import hungteen.immortal.common.recipe.ImmortalRecipes;
import hungteen.immortal.compat.jei.category.ElixirCategory;
import hungteen.immortal.utils.Util;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-04 12:35
 **/
@JeiPlugin
public class ImmortalJEIPlugin implements IModPlugin {

    private static final ResourceLocation ID = Util.prefix("jei");

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Util.getProxy().getRecipeManager().ifPresent(manager -> {
            registration.addRecipes(ElixirCategory.ELIXIR_RECIPE_TYPE, manager.getAllRecipesFor(ImmortalRecipes.ELIXIR_RECIPE_TYPE.get()));
        });
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new ElixirCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ImmortalBlocks.ELIXIR_ROOM.get()), ElixirCategory.ELIXIR_RECIPE_TYPE);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(ElixirRoomMenu.class, ImmortalMenus.ELIXIR_ROOM.get(), ElixirCategory.ELIXIR_RECIPE_TYPE, 0, 9, 9, 36);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }
}
