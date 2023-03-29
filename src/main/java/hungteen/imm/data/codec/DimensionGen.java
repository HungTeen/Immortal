package hungteen.imm.data.codec;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-17 12:28
 **/
public class DimensionGen {

//    public DimensionGen(DataGenerator generator) {
//        super(generator, Util.id());
//    }
//
//    @Override
//    public void run(CachedOutput cache) {
//        WritableRegistry<LevelStem> registry = new MappedRegistry<>(Registry.LEVEL_STEM_REGISTRY, Lifecycle.experimental(), null);
//        Registry<Biome> biomes = access().registryOrThrow(Registry.BIOME_REGISTRY);
//        Registry<StructureSet> structureSets = access().registryOrThrow(Registry.STRUCTURE_SET_REGISTRY);
//        Registry<NormalNoise.NoiseParameters> noiseParameters = access().registryOrThrow(Registry.NOISE_REGISTRY);
//
//        registry.register(ImmortalDimensions.SPIRITUAL_LAND, new LevelStem(
//                ImmortalDimensions.SPIRITUAL_LAND_TYPE.getHolder().get(),
//                new NoiseBasedChunkGenerator(
//                        structureSets,
//                        noiseParameters,
//                        ImmortalDimensions.SPIRITUAL_LAND_PRESET.biomeSource(biomes, true),
//                        ImmortalNoiseGenSettings.SPIRITUAL_LAND_SETTINGS.getHolder().get()
//                )
//        ), Lifecycle.stable());
//
//        register(cache, Registry.LEVEL_STEM_REGISTRY, registry, LevelStem.CODEC);
//    }
//
//    @Override
//    public String getName() {
//        return this.modId + " dimensions";
//    }

}
