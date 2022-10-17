package hungteen.immortal.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;
import hungteen.immortal.data.codec.CodecGen;
import hungteen.immortal.utils.Util;
import hungteen.immortal.world.dimension.ImmortalDimensions;
import net.minecraft.core.*;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.biome.TheEndBiomeSource;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.function.Function;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-17 12:28
 **/
public class DimensionGen extends CodecGen {

    public DimensionGen(DataGenerator generator) {
        super(generator, Util.id());
    }

    @Override
    public void run(HashCache cache) {
        Registry<LevelStem> twilight = dimensions(access());
        register(cache, Registry.LEVEL_STEM_REGISTRY, twilight, LevelStem.CODEC);
    }


    public Registry<LevelStem> dimensions(RegistryAccess access) {
        WritableRegistry<LevelStem> registry = new MappedRegistry<>(Registry.LEVEL_STEM_REGISTRY, Lifecycle.experimental(), (Function<LevelStem, Holder.Reference<LevelStem>>)null);
//        Registry<DimensionType> dimensionTypes = access.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY);
        Registry<Biome> biomes = access.registryOrThrow(Registry.BIOME_REGISTRY);
        Registry<StructureSet> structureSets = access.registryOrThrow(Registry.STRUCTURE_SET_REGISTRY);
        Registry<NoiseGeneratorSettings> generatorSettings = access.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY);
        Registry<NormalNoise.NoiseParameters> noiseParameters = access.registryOrThrow(Registry.NOISE_REGISTRY);
        registry.register(ImmortalDimensions.SPIRITUAL_LAND, new LevelStem(
                Holder.direct(twilightDimType()),
//                dimensionTypes.getOrCreateHolder(ImmortalDimensions.SPIRITUAL_LAND_TYPE),
                new NoiseBasedChunkGenerator(
                        structureSets,
                        noiseParameters,
                        MultiNoiseBiomeSource.Preset.OVERWORLD.biomeSource(biomes, true),
                        0,
                        generatorSettings.getOrCreateHolder(NoiseGeneratorSettings.OVERWORLD)
                ), true
        ), Lifecycle.stable());
        return registry;
    }

    private DimensionType twilightDimType() {
        return DimensionType.create(OptionalLong.empty(), true, false, false, true, 1.0D, false, false, true, false, true, -64, 384, 384, BlockTags.INFINIBURN_OVERWORLD, DimensionType.OVERWORLD_EFFECTS, 0.0F);
    }

    @Override
    public String getName() {
        return this.modId + "dimensions";
    }

}
