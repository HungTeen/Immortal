package hungteen.imm.common.world.structure.structures;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import hungteen.imm.common.world.structure.IMMProcessorLists;
import hungteen.imm.common.world.structure.IMMPools;
import hungteen.imm.util.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.data.worldgen.placement.VillagePlacements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-03-24 22:43
 **/
public class PlainsTradingMarket {

    public static StructureProcessorList getStreetProcessor() {
        return new StructureProcessorList(ImmutableList.of(
                new RuleProcessor(
                        ImmutableList.of(
                                new ProcessorRule(
                                        new RandomBlockMatchTest(Blocks.COBBLESTONE, 0.1F),
                                        AlwaysTrueTest.INSTANCE,
                                        Blocks.GRASS_BLOCK.defaultBlockState()
                                ),
                                new ProcessorRule(
                                        new RandomBlockMatchTest(Blocks.COBBLESTONE, 0.4F),
                                        AlwaysTrueTest.INSTANCE,
                                        Blocks.MOSSY_COBBLESTONE.defaultBlockState()
                                ),
                                new ProcessorRule(
                                        new BlockMatchTest(Blocks.GRASS_BLOCK),
                                        new BlockMatchTest(Blocks.WATER),
                                        Blocks.WATER.defaultBlockState()
                                ),
                                new ProcessorRule(
                                        new BlockMatchTest(Blocks.DIRT),
                                        new BlockMatchTest(Blocks.WATER),
                                        Blocks.WATER.defaultBlockState()
                                )
                        )
                )
        ));
    }

    /**
     * {@link net.minecraft.data.worldgen.PlainVillagePools}
     */
    public static void initPools(BootstrapContext<StructureTemplatePool> context) {
        final HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);
        final HolderGetter<StructureProcessorList> processors = context.lookup(Registries.PROCESSOR_LIST);
        final HolderGetter<PlacedFeature> features = context.lookup(Registries.PLACED_FEATURE);
        final Holder<StructureTemplatePool> empty = pools.getOrThrow(Pools.EMPTY);
//        final Holder<StructureTemplatePool> terminate = pools.getOrThrow(Pools.);
        final Holder<StructureProcessorList> moss = processors.getOrThrow(ProcessorLists.MOSSIFY_20_PERCENT);
        final Holder<StructureProcessorList> none = processors.getOrThrow(IMMProcessorLists.EMPTY);
        final Holder<StructureProcessorList> streetRot = processors.getOrThrow(IMMProcessorLists.PLAINS_TRADING_MARKET_STREET_ROT);

        context.register(IMMPools.PLAINS_TRADING_MARKET_START, new StructureTemplatePool(
                empty,
                pools(
                        getPoolElements("town_centers/meeting_point", moss, StructureTemplatePool.Projection.RIGID, List.of(10))
                )
        ));

        context.register(IMMPools.PLAINS_TRADING_MARKET_END, new StructureTemplatePool(
                empty,
                pools(
                        getPoolElements("town_centers/meeting_point", moss, StructureTemplatePool.Projection.RIGID, List.of(10))
                )
        ));

        context.register(IMMPools.PLAINS_TRADING_MARKET_STREET, new StructureTemplatePool(
                empty,
                pools(
                        getPoolElements("streets/straight", streetRot, StructureTemplatePool.Projection.TERRAIN_MATCHING, List.of(4, 4, 7, 7)),
                        getPoolElements("streets/crossroad", streetRot, StructureTemplatePool.Projection.TERRAIN_MATCHING, List.of(2, 1, 2, 2, 2)),
                        getPoolElements("streets/corner", 3, streetRot, StructureTemplatePool.Projection.TERRAIN_MATCHING, 2)
                )
        ));

        context.register(IMMPools.PLAINS_TRADING_MARKET_HOUSE, new StructureTemplatePool(
                empty,
                pools(
                        getPoolElements("houses/decor", none, StructureTemplatePool.Projection.RIGID, List.of(5, 6, 7, 7, 7, 4, 4, 6)),
                        getPoolElements("houses/house", none, StructureTemplatePool.Projection.RIGID, List.of(12, 12, 10, 10, 10))
                )
        ));

        context.register(IMMPools.PLAINS_TRADING_MARKET_DECOR, new StructureTemplatePool(
                empty,
                List.of(
                        Pair.of(
                                StructurePoolElement.feature(features.getOrThrow(VillagePlacements.FLOWER_PLAIN_VILLAGE))
                                        .apply(StructureTemplatePool.Projection.RIGID), 2
                        ),
                        Pair.of(
                                StructurePoolElement.feature(features.getOrThrow(VillagePlacements.PILE_HAY_VILLAGE))
                                        .apply(StructureTemplatePool.Projection.RIGID), 1
                        ),
                        Pair.of(
                                StructurePoolElement.feature(features.getOrThrow(VillagePlacements.OAK_VILLAGE))
                                        .apply(StructureTemplatePool.Projection.RIGID), 1
                        ),
                        Pair.of(
                                StructurePoolElement.empty().apply(StructureTemplatePool.Projection.RIGID), 3
                        )
                )
        ));

        context.register(IMMPools.PLAINS_TRADING_MARKET_TREES, new StructureTemplatePool(
                empty,
                List.of(
                        Pair.of(
                                StructurePoolElement.feature(features.getOrThrow(VillagePlacements.OAK_VILLAGE))
                                        .apply(StructureTemplatePool.Projection.RIGID), 1
                        )
                )
        ));

        context.register(IMMPools.PLAINS_TRADING_MARKET_CULTIVATORS, new StructureTemplatePool(
                empty,
                List.of(
                        Pair.of(
                                StructurePoolElement.legacy(Util.prefixName("entities/spiritual_beginner_cultivator")).apply(StructureTemplatePool.Projection.RIGID), 2
                        )
                )
        ));

        context.register(IMMPools.PLAINS_TRADING_MARKET_ANIMALS, new StructureTemplatePool(
                empty,
                List.of(
                        Pair.of(
                                StructurePoolElement.legacy("village/common/animals/sheep_1").apply(StructureTemplatePool.Projection.RIGID), 2
                        )
                )
        ));
    }

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
        return Util.prefixName("trading_market/plains/" + name);
    }

    private static ResourceLocation res(String name) {
        return Util.prefix("trading_market/plains/" + name);
    }
}
