package hungteen.imm.common.world.levelgen;

import hungteen.imm.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/6/14 10:06
 */
public interface IMMNoiseParamLists {

//    public static final ResourceKey<MultiNoiseBiomeSourceParameterList> EAST_WORLD = create("east_world");
//
//    public static final MultiNoiseBiomeSourceParameterList.Preset EAST_WORLD_PRESET = new MultiNoiseBiomeSourceParameterList.Preset(
//            Util.prefix("east_world"),
//            new MultiNoiseBiomeSourceParameterList.Preset.SourceProvider() {
//                @Override
//                public <T> Climate.ParameterList<T> apply(Function<ResourceKey<Biome>, T> function) {
//                    final ImmutableList.Builder<Pair<Climate.ParameterPoint, T>> builder = ImmutableList.builder();
//                    EastWorldDimension.addBiomes(resourceKeyPair -> builder.add(resourceKeyPair.mapSecond(function)));
//                    return new Climate.ParameterList<>(builder.build());
//                }
//            }
//    );

    static void register(BootstrapContext<MultiNoiseBiomeSourceParameterList> context) {
//        final HolderGetter<Biome> holdergetter = context.lookup(Registries.BIOME);
//        context.initialize(EAST_WORLD, new MultiNoiseBiomeSourceParameterList(EAST_WORLD_PRESET, holdergetter));
    }

    static ResourceKey<MultiNoiseBiomeSourceParameterList> create(String name) {
        return ResourceKey.create(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST, Util.prefix(name));
    }
}
