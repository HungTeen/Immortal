package hungteen.imm.common.world.structure.structures;

import com.mojang.datafixers.util.Pair;
import hungteen.imm.util.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-09-26 10:45
 **/
public class SpiritualPlainsVillage {

    public static void initStructures(BootstrapContext<Structure> context) {
//        context.initialize(IMMStructures.SPIRITUAL_PLAINS_VILLAGE, new JigsawStructure(
//                new Structure.StructureSettings(
//                        context.lookup(Registries.BIOME).getOrThrow(BiomeTags.HAS_VILLAGE_PLAINS),
//                        Map.of(),
//                        GenerationStep.Decoration.SURFACE_STRUCTURES,
//                        TerrainAdjustment.BEARD_THIN
//                ),
//                context.lookup(Registries.TEMPLATE_POOL).getOrThrow(IMMTemplatePools.PLAINS_VILLAGE_START),
//                Optional.empty(),
//                6,
//                ConstantHeight.of(new VerticalAnchor.Absolute(0)),
//                true,
//                Optional.of(Heightmap.Types.WORLD_SURFACE_WG),
//                80
//        ));
    }
//
//    public static Function<Registry<Structure>, StructureSet> getStructureSet() {
//        return (structureRegistry) -> new StructureSet(
//                structureRegistry.getHolderOrThrow(ImmortalStructures.SPIRITUAL_PLAINS_VILLAGE),
//                new RandomSpreadStructurePlacement(
//                        20,
//                        8,
//                        RandomSpreadType.LINEAR,
//                        1080133475
//                )
//        );
//    }
//
//    public static StructureProcessorList getStreetProcessor(){
//        return new StructureProcessorList(ImmutableList.of(
//                new RuleProcessor(
//                        ImmutableList.of(
//                                new ProcessorRule(
//                                        new RandomBlockMatchTest(Blocks.COBBLESTONE, 0.1F),
//                                        AlwaysTrueTest.INSTANCE,
//                                        Blocks.GRASS_BLOCK.defaultBlockState()
//                                ),
//                                new ProcessorRule(
//                                        new RandomBlockMatchTest(Blocks.COBBLESTONE, 0.4F),
//                                        AlwaysTrueTest.INSTANCE,
//                                        Blocks.MOSSY_COBBLESTONE.defaultBlockState()
//                                ),
//                                new ProcessorRule(
//                                        new BlockMatchTest(Blocks.GRASS_BLOCK),
//                                        new BlockMatchTest(Blocks.WATER),
//                                        Blocks.WATER.defaultBlockState()
//                                ),
//                                new ProcessorRule(
//                                        new BlockMatchTest(Blocks.DIRT),
//                                        new BlockMatchTest(Blocks.WATER),
//                                        Blocks.WATER.defaultBlockState()
//                                )
//                        )
//                )
//        ));
//    }

    public static void initPools(BootstrapContext<StructureTemplatePool> context){
        final HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);
        final HolderGetter<StructureProcessorList> processors = context.lookup(Registries.PROCESSOR_LIST);
        final Holder<StructureTemplatePool> empty = pools.getOrThrow(Pools.EMPTY);
        final Holder<StructureProcessorList> moss = processors.getOrThrow(ProcessorLists.MOSSIFY_20_PERCENT);

//        context.initialize(IMMTemplatePools.PLAINS_VILLAGE_START, new StructureTemplatePool(
//                empty,
//                pools(
//                        getPoolElements("town_centers/meeting_point", moss, StructureTemplatePool.Projection.RIGID, List.of(10))
//                )
//        ));

//        context.initialize(IMMTemplatePools.PLAINS_VILLAGE_END, new StructureTemplatePool(
//                empty,
//                pools(
//                        getPoolElements("town_centers/meeting_point", moss, StructureTemplatePool.Projection.RIGID, List.of(10))
//                )
//        ));
//
//        context.initialize(IMMTemplatePools.PLAINS_VILLAGE_STREET, new StructureTemplatePool(
//                empty,
//                pools(
//                        getPoolElements("town_centers/meeting_point", moss, StructureTemplatePool.Projection.RIGID, List.of(10))
//                )
//        ));
    }

//    public static StructureTemplatePool getStreetPool(BootstapContext<StructureTemplatePool> context) {
//        return new StructureTemplatePool(
//                new ResourceLocation("minecraft:village/plains/terminators"),
//                pools(
//                        getPoolElements("streets/straight", ImmortalProcessors.SPIRITUAL_PLAINS_STREETS.getHolder().get(), StructureTemplatePool.Projection.TERRAIN_MATCHING, List.of(4, 4, 7, 7)),
//                        getPoolElements("streets/crossroad", ImmortalProcessors.SPIRITUAL_PLAINS_STREETS.getHolder().get(), StructureTemplatePool.Projection.TERRAIN_MATCHING, List.of(2, 1, 2, 2, 2)),
//                        getPoolElements("streets/corner", 3, ImmortalProcessors.SPIRITUAL_PLAINS_STREETS.getHolder().get(), StructureTemplatePool.Projection.TERRAIN_MATCHING, 2)
//                )
//        );
//    }
//
//    public static StructureTemplatePool getHousePool(BootstapContext<StructureTemplatePool> context) {
//        PlainVillagePools
//        return new StructureTemplatePool(
//                res("houses"),
//                new ResourceLocation("empty"),
//                pools(
//                        getPoolElements("houses/decor", ProcessorLists.EMPTY, StructureTemplatePool.Projection.RIGID, List.of(5, 6, 7, 7, 7, 4, 4, 6)),
//                        getPoolElements("houses/house", ProcessorLists.EMPTY, StructureTemplatePool.Projection.RIGID, List.of(12, 12, 10, 10, 10))
//                )
//        );
//    }
//
//    public static StructureTemplatePool getDecorPool(BootstapContext<StructureTemplatePool> context) {
//        return new StructureTemplatePool(
//                res("decor"),
//                new ResourceLocation("empty"),
//                List.of(
////                        Pair.of(
////                                StructurePoolElement.feature(VegetationPlacements.FLOWER_DEFAULT).apply(StructureTemplatePool.Projection.RIGID), 2
////                        ),
////                        Pair.of(
////                                StructurePoolElement.empty().apply(StructureTemplatePool.Projection.RIGID), 2
////                        )
//                )
//        );
//    }

    private static List<Pair<StructurePoolElement, Integer>> pools(List<Pair<StructurePoolElement, Integer>>... pools) {
        return Arrays.stream(pools).flatMap(List::stream).collect(Collectors.toList());
    }

    private static List<Pair<StructurePoolElement, Integer>> getPoolElements(String name, Holder<StructureProcessorList> processor, StructureTemplatePool.Projection projection, List<Integer> weights) {
        List<Pair<StructurePoolElement, Integer>> elements = new ArrayList<>();
        for (int i = 0; i < weights.size(); i++) {
            elements.add(getPoolElement(name, i + 1, processor, projection, weights.get(i)));
        }
        return elements;
    }

    private static List<Pair<StructurePoolElement, Integer>> getPoolElements(String name, int count, Holder<StructureProcessorList> processor, StructureTemplatePool.Projection projection, int weight) {
        List<Pair<StructurePoolElement, Integer>> elements = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            elements.add(getPoolElement(name, i + 1, processor, projection, weight));
        }
        return elements;
    }

    private static Pair<StructurePoolElement, Integer> getPoolElement(String name, int id, Holder<StructureProcessorList> processor, StructureTemplatePool.Projection projection, int weight) {
        final String ids = id > 10 ? ("_" + id) : ("_0" + id);
        return Pair.of(StructurePoolElement.legacy(name(name + ids), processor).apply(projection), weight);
    }

    private static String name(String name) {
        return Util.prefixName("village/plains/" + name);
    }

    private static ResourceLocation res(String name) {
        return Util.prefix("village/plains/" + name);
    }

}
