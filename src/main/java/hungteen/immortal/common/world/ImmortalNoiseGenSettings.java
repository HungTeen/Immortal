package hungteen.immortal.common.world;

import hungteen.immortal.common.world.dimension.SpiritualLandDimension;
import hungteen.immortal.utils.Util;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouterData;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-12 19:38
 **/
public class ImmortalNoiseGenSettings {

    public static final DeferredRegister<NoiseGeneratorSettings> NOISE_GENERATOR_SETTINGS = DeferredRegister.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, Util.id());

    public static final RegistryObject<NoiseGeneratorSettings> SPIRITUAL_LAND_SETTINGS = NOISE_GENERATOR_SETTINGS.register("spiritual_land_settings", ImmortalNoiseGenSettings::spiritualLandSettings);

    public static NoiseGeneratorSettings spiritualLandSettings() {
        return new NoiseGeneratorSettings(
                NoiseSettings.create(-64, 384, 1, 2),
                Blocks.STONE.defaultBlockState(),
                Blocks.WATER.defaultBlockState(),
                NoiseRouterData.overworld(BuiltinRegistries.DENSITY_FUNCTION, false, false),
                ImmortalSurfaceRules.overworldLike(false, true),
                SpiritualLandDimension.spawnTarget(),
                63,
                false,
                true,
                true,
                false
        );
    }

    public static void register(IEventBus event){
        NOISE_GENERATOR_SETTINGS.register(event);
    }
}
