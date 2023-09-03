package hungteen.imm.common.world.levelgen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import hungteen.htlib.util.helper.registry.LevelHelper;
import hungteen.imm.common.world.levelgen.dimension.EastWorldDimension;
import hungteen.imm.util.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-17 10:55
 **/
public interface IMMLevelStems {

    ResourceKey<LevelStem> EAST_WORLD = create("east_world");

    static void register(BootstapContext<LevelStem> context){
        final HolderGetter<MultiNoiseBiomeSourceParameterList> holderGetter = context.lookup(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST);
        final ImmutableList.Builder<Pair<Climate.ParameterPoint, Holder<Biome>>> builder = ImmutableList.builder();
        EastWorldDimension.addBiomes(resourceKeyPair -> builder.add(resourceKeyPair.mapSecond(res -> context.lookup(Registries.BIOME).getOrThrow(res))));
        context.register(EAST_WORLD, new LevelStem(
                context.lookup(Registries.DIMENSION_TYPE).getOrThrow(IMMDimensionTypes.EAST_WORLD),
                new NoiseBasedChunkGenerator(
                        MultiNoiseBiomeSource.createFromList(new Climate.ParameterList<>(builder.build())),
                        context.lookup(Registries.NOISE_SETTINGS).getOrThrow(IMMNoiseSettings.EAST_WORLD)
                )
        ));
    }

    static ResourceKey<LevelStem> create(String name) {
        return LevelHelper.stem().createKey(Util.prefix(name));
    }

}
