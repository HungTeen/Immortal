package hungteen.immortal.data.codec;

import com.mojang.serialization.Lifecycle;
import hungteen.htlib.data.HTCodecGen;
import hungteen.immortal.common.world.dimension.ImmortalDimensions;
import hungteen.immortal.utils.Util;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-17 12:28
 **/
public class DimensionGen extends HTCodecGen {

    public DimensionGen(DataGenerator generator) {
        super(generator, Util.id());
    }

    @Override
    public void run(CachedOutput cache) {
        WritableRegistry<LevelStem> registry = new MappedRegistry<>(Registry.LEVEL_STEM_REGISTRY, Lifecycle.experimental(), null);
        Registry<DimensionType> dimensionTypes = access().registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY);
        Registry<Biome> biomes = access().registryOrThrow(Registry.BIOME_REGISTRY);
        Registry<StructureSet> structureSets = access().registryOrThrow(Registry.STRUCTURE_SET_REGISTRY);
        Registry<NoiseGeneratorSettings> generatorSettings = access().registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY);
        Registry<NormalNoise.NoiseParameters> noiseParameters = access().registryOrThrow(Registry.NOISE_REGISTRY);

        registry.register(ImmortalDimensions.SPIRITUAL_LAND, new LevelStem(
                ImmortalDimensions.SPIRITUAL_LAND_TYPE.getHolder().get(),
                new NoiseBasedChunkGenerator(
                        structureSets,
                        noiseParameters,
                        ImmortalDimensions.SPIRITUAL_LAND_PRESET.biomeSource(biomes, true),
                        generatorSettings.getOrCreateHolderOrThrow(NoiseGeneratorSettings.OVERWORLD)
                )
        ), Lifecycle.stable());

        register(cache, Registry.LEVEL_STEM_REGISTRY, registry, LevelStem.CODEC);
    }

    @Override
    public String getName() {
        return this.modId + " dimensions";
    }

}
