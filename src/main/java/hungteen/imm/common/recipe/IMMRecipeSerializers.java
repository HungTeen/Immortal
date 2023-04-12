package hungteen.imm.common.recipe;

import hungteen.imm.util.Util;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-11 22:19
 **/
public class IMMRecipeSerializers {

    private static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Util.id());

    public static final RegistryObject<ElixirRecipe.Serializer> ELIXIR = SERIALIZERS.register("elixir_serializer", ElixirRecipe.Serializer::new);
    public static final RegistryObject<SmithingArtifactRecipe.Serializer> SMITHING_ARTIFACT = SERIALIZERS.register("smithing_artifact_serializer", SmithingArtifactRecipe.Serializer::new);

    /**
     * {@link hungteen.imm.ImmortalMod#defferRegister(IEventBus)}
     */
    public static void register(IEventBus event){
        SERIALIZERS.register(event);
    }

}
