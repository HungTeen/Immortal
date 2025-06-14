package hungteen.imm.common.world.levelgen;

import hungteen.htlib.util.helper.impl.LevelHelper;
import hungteen.imm.util.Util;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;

import java.util.List;
import java.util.Optional;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-17 10:55
 **/
public interface IMMLevelStems {

    ResourceKey<LevelStem> EAST_WORLD = create("east_world");
    ResourceKey<LevelStem> SPIRIT_WORLD = create("spirit_world");

    static void register(BootstrapContext<LevelStem> context){
        final HolderGetter<MultiNoiseBiomeSourceParameterList> holderGetter = context.lookup(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST);
        HolderGetter<DimensionType> dimensionTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
//        {
//            final ImmutableList.Builder<Pair<Climate.ParameterPoint, Holder<Biome>>> builder = ImmutableList.builder();
//            EastWorldDimension.addBiomes(resourceKeyPair -> builder.add(resourceKeyPair.mapSecond(res -> context.lookup(Registries.BIOME).getOrThrow(res))));
//            context.register(EAST_WORLD, new LevelStem(
//                    dimensionTypes.getOrThrow(IMMDimensionTypes.EAST_WORLD),
//                    new NoiseBasedChunkGenerator(
//                            MultiNoiseBiomeSource.createFromList(new Climate.ParameterList<>(builder.build())),
//                            context.lookup(Registries.NOISE_SETTINGS).getOrThrow(IMMNoiseSettings.EAST_WORLD)
//                    )
//            ));
//        }
        context.register(SPIRIT_WORLD, new LevelStem(
                dimensionTypes.getOrThrow(IMMDimensionTypes.SPIRIT_WORLD),
                new FlatLevelSource(new FlatLevelGeneratorSettings(
                        Optional.empty(), biomes.getOrThrow(Biomes.THE_VOID), List.of()
                ))
        ));
    }

    static ResourceKey<LevelStem> create(String name) {
        return LevelHelper.stem().createKey(Util.prefix(name));
    }

}
