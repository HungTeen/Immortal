package hungteen.imm.common.recipe;

import hungteen.htlib.api.registry.HTHolder;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.common.impl.registry.HTVanillaRegistry;
import hungteen.htlib.common.recipe.HTRecipeType;
import hungteen.htlib.util.NeoHelper;
import hungteen.imm.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.neoforged.bus.api.IEventBus;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-28 22:19
 **/
public class IMMRecipes {

    static {
        ShapedRecipePattern.setCraftingSize(7, 7);
    }

    private static final HTVanillaRegistry<RecipeType<?>> RECIPE_TYPES = HTRegistryManager.vanilla(Registries.RECIPE_TYPE, Util.id());

    public static final HTHolder<RecipeType<ElixirRecipe>> ELIXIR = register("elixir");
    public static final HTHolder<RecipeType<SmithingArtifactRecipe>> SMITHING_ARTIFACT = register("smithing_artifact");

    public static void initialize(IEventBus event){
        NeoHelper.initRegistry(RECIPE_TYPES, event);
    }

    private static <T extends Recipe<?>> HTHolder<RecipeType<T>> register(String name){
        return RECIPE_TYPES.register(name, () -> new HTRecipeType<>(Util.prefix(name)));
    }

}
