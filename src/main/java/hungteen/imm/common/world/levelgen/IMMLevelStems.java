package hungteen.imm.common.world.levelgen;

import hungteen.htlib.util.helper.registry.LevelHelper;
import hungteen.imm.util.Util;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-17 10:55
 **/
public class IMMLevelStems {

    public static final ResourceKey<Level> SPIRITUAL_LAND_DIMENSION = createLevel("spiritual_land");
    public static final ResourceKey<LevelStem> SPIRITUAL_LAND = createStem("spiritual_land");

//    public static final MultiNoiseBiomeSource.Preset SPIRITUAL_LAND_PRESET = new MultiNoiseBiomeSource.Preset(
//            Util.prefix("spiritual_land"),
//            (biomes) -> {
//                ImmutableList.Builder<Pair<Climate.ParameterPoint, Holder<Biome>>> builder = ImmutableList.builder();
//                SpiritualLandDimension.addBiomes((resourceKeyPair) -> {
//                    builder.add(resourceKeyPair.mapSecond(biomes::getOrThrow));
//                });
//                return new Climate.ParameterList<>(builder.build());
//            }
//    );

    public static void register(BootstapContext<LevelStem> context){


    }

    private static ResourceKey<Level> createLevel(String name) {
        return LevelHelper.get().createKey(Util.prefix(name));
    }

    private static ResourceKey<LevelStem> createStem(String name) {
        return LevelHelper.stem().createKey(Util.prefix(name));
    }

}
