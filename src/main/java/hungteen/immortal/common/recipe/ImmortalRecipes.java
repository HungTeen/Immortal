package hungteen.immortal.common.recipe;

import hungteen.immortal.utils.Util;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-28 22:19
 **/
public class ImmortalRecipes {

    static {
        ShapedRecipe.setCraftingSize(7, 7);
    }

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Util.id());

    public static final RecipeType<ElixirRecipe> ELIXIR_RECIPE_TYPE = RecipeType.register(Util.prefix("elixir").toString());

    public static final RegistryObject<ElixirRecipe.Serializer> ELIXIR_SERIALIZER = RECIPE_SERIALIZERS.register("elixir_serializer", ElixirRecipe.Serializer::new);
}
