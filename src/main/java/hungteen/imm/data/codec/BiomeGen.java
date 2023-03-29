package hungteen.imm.data.codec;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-18 13:26
 **/
public class BiomeGen {

//    public BiomeGen(DataGenerator generator) {
//        super(generator, Util.id());
//    }
//
//    @Override
//    public void run(CachedOutput cache) {
//        WritableRegistry<Biome> biomeRegistry = new MappedRegistry<>(Registry.BIOME_REGISTRY, Lifecycle.experimental(), null);
//
//        this.getBiomes().forEach((rl, biome) -> {
//            biomeRegistry.register(ResourceKey.create(Registry.BIOME_REGISTRY, rl), biome, Lifecycle.experimental());
//        });
//
//        register(cache, Registry.BIOME_REGISTRY, biomeRegistry, Biome.DIRECT_CODEC);
//    }
//
//    private Map<ResourceLocation, Biome> getBiomes() {
//        return ImmortalBiomes.biomes().entrySet().stream()
//                .collect(Collectors.toUnmodifiableMap(entry -> entry.getKey().location(), Map.Entry::getValue));
//    }
//
//    @Override
//    public String getName() {
//        return this.modId +" biomes";
//    }

}
