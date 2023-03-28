package hungteen.imm.data.codec;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-20 10:11
 **/
public class StructureGen {

//    public StructureGen(DataGenerator generator) {
//        super(generator, Util.id());
//    }
//
//    @Override
//    public void run(CachedOutput cache) {
//        WritableRegistry<Structure> structureRegistry = new MappedRegistry<>(Registry.STRUCTURE_REGISTRY, Lifecycle.experimental(), null);
//        WritableRegistry<StructureSet> structureSetRegistry = new MappedRegistry<>(Registry.STRUCTURE_SET_REGISTRY, Lifecycle.experimental(), null);
//        Registry<Biome> biomes = access().registryOrThrow(Registry.BIOME_REGISTRY);
//        Registry<Structure> structures = access().registryOrThrow(Registry.STRUCTURE_REGISTRY);
//
//        ImmortalStructures.getStructures(biomes).forEach(pair -> {
//            structureRegistry.register(pair.getFirst(), pair.getSecond(), Lifecycle.experimental());
//        });
//
//        ImmortalStructures.getStructureSets(structureRegistry).forEach(pair -> {
//            structureSetRegistry.register(pair.getFirst(), pair.getSecond(), Lifecycle.experimental());
//        });
//
////        Structure structure = SpiritualPlainsVillage.getStructure().apply(biomes);
////        structureRegistry.register(ImmortalStructures.SPIRITUAL_PLAINS_VILLAGE, structure, Lifecycle.experimental());
////
////        StructureSet structureSet = SpiritualPlainsVillage.getStructureSet().apply(structureRegistry);
////        structureSetRegistry.register(ImmortalStructures.SPIRITUAL_PLAINS_VILLAGE_SET, structureSet, Lifecycle.experimental());
//
//        register(cache, Registry.STRUCTURE_REGISTRY, structureRegistry, Structure.DIRECT_CODEC);
//        register(cache, Registry.STRUCTURE_SET_REGISTRY, structureSetRegistry, StructureSet.DIRECT_CODEC);
//    }
//
//    @Override
//    public String getName() {
//        return this.modId + " structures";
//    }
}
