package hungteen.imm.common.recipe;

import hungteen.htlib.api.registry.HTHolder;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.common.impl.registry.HTVanillaRegistry;
import hungteen.htlib.util.NeoHelper;
import hungteen.imm.IMMInitializer;
import hungteen.imm.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-04-11 22:19
 **/
public class IMMRecipeSerializers {

    private static final HTVanillaRegistry<RecipeSerializer<?>> SERIALIZERS = HTRegistryManager.vanilla(Registries.RECIPE_SERIALIZER, Util.id());

    public static final HTHolder<ElixirRecipe.Serializer> ELIXIR = SERIALIZERS.register("elixir_serializer", ElixirRecipe.Serializer::new);
    public static final HTHolder<SmithingArtifactRecipe.Serializer> SMITHING_ARTIFACT = SERIALIZERS.register("smithing_artifact_serializer", SmithingArtifactRecipe.Serializer::new);

    /**
     * {@link IMMInitializer#defferRegister(IEventBus)}
     */
    public static void initialize(IEventBus event){
        NeoHelper.initRegistry(SERIALIZERS, event);
    }

}
