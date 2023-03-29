package hungteen.imm.common.world.levelgen;

import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-12 19:38
 **/
public class IMMNoiseSettings {

//    public static final RegistryObject<NoiseGeneratorSettings> SPIRITUAL_LAND_SETTINGS = NOISE_GENERATOR_SETTINGS.register("spiritual_land_settings", ImmortalNoiseGenSettings::spiritualLandSettings);

//    public static NoiseGeneratorSettings spiritualLandSettings() {
//        return new NoiseGeneratorSettings(
//                NoiseSettings.create(-64, 384, 1, 2),
//                Blocks.STONE.defaultBlockState(),
//                Blocks.WATER.defaultBlockState(),
//                NoiseRouterData.overworld(BuiltInRegistries.DENSITY_FUNCTION_TYPE, false, false),
//                ImmortalSurfaceRules.overworldLike(false, true),
//                SpiritualLandDimension.spawnTarget(),
//                63,
//                false,
//                true,
//                true,
//                false
//        );
//    }

    public static void register(BootstapContext<NoiseGeneratorSettings> context){
//        NOISE_GENERATOR_SETTINGS.register(event);
    }
}
