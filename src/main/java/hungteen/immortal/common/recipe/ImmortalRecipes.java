package hungteen.immortal.common.recipe;

import hungteen.immortal.utils.Util;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
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
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Util.id());

    /* Recipe Serializers */

    public static final RegistryObject<ElixirRecipe.Serializer> ELIXIR_SERIALIZER = RECIPE_SERIALIZERS.register("elixir_serializer", ElixirRecipe.Serializer::new);
    public static final RegistryObject<SmithingArtifactRecipe.Serializer> SMITHING_ARTIFACT_SERIALIZER = RECIPE_SERIALIZERS.register("smithing_artifact_serializer", SmithingArtifactRecipe.Serializer::new);

    /* Recipe Types */

    public static final RegistryObject<RecipeType<ElixirRecipe>> ELIXIR_RECIPE_TYPE = RECIPE_TYPES.register("elixir", () -> new RecipeType<>() {});
    public static final RegistryObject<RecipeType<SmithingArtifactRecipe>> SMITHING_ARTIFACT_RECIPE_TYPE = RECIPE_TYPES.register("smithing_artifact", () -> new RecipeType<>() {});

}
