package hungteen.imm.common.recipe;

import hungteen.imm.util.Util;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-28 22:19
 **/
public class IMMRecipes {

    static {
        ShapedRecipe.setCraftingSize(7, 7);
    }

    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Util.id());

    public static final RegistryObject<RecipeType<ElixirRecipe>> ELIXIR = register("elixir");
    public static final RegistryObject<RecipeType<SmithingArtifactRecipe>> SMITHING_ARTIFACT = register("smithing_artifact");

    /**
     * {@link hungteen.imm.ImmortalMod#defferRegister(IEventBus)}
     */
    public static void register(IEventBus event){
        RECIPE_TYPES.register(event);
    }

    private static <T extends Recipe<?>> RegistryObject<RecipeType<T>> register(String name){
        return RECIPE_TYPES.register(name, () -> new RecipeType<>() {
            @Override
            public String toString() {
                return Util.prefixName(name);
            }
        });
    }

}
