package hungteen.imm.common.world.levelgen.structure;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-03-24 22:43
 **/
public class OverworldTradingMarket {

    public static void initStructures(BootstapContext<Structure> context) {
//        context.register(IMMStructures.OVERWORLD_TRADING_MARKET, new JigsawStructure(
//                new Structure.StructureSettings(
//                        context.lookup(Registries.BIOME).getOrThrow(IMMBiomeTags.HAS_OVERWORLD_TRADING_MARKET),
//                        Map.of(
//                                MobCategory.CREATURE, new StructureSpawnOverride(
//                                        StructureSpawnOverride.BoundingBoxType.STRUCTURE,
//                                        WeightedRandomList.create(
//                                                new MobSpawnSettings.SpawnerData(EntityType.VILLAGER, 1, 1, 1)
//                                        )
//                                )
//                        ),
//                        GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
//                        TerrainAdjustment.BEARD_BOX
//                ),
//                context.lookup(Registries.TEMPLATE_POOL).getOrThrow(IMMTemplatePools.OVERWORLD_TRADING_MARKET_START),
//                Optional.empty(),
//                6,
//                ConstantHeight.of(new VerticalAnchor.Absolute(0)),
//                true,
//                Optional.empty(),
//                80
//        ));
    }

    public static void initStructureSets(BootstapContext<StructureSet> context) {
//        context.register(IMMStructureSets.OVERWORLD_TRADING_MARKET_SET, new StructureSet(
//                context.lookup(Registries.STRUCTURE).getOrThrow(IMMStructures.OVERWORLD_TRADING_MARKET),
//                new RandomSpreadStructurePlacement(
//                        32,
//                        8,
//                        RandomSpreadType.LINEAR,
//                        397815004
//                )
//        ));
    }

    public static void initPools(BootstapContext<StructureTemplatePool> context){
        final HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);
        final HolderGetter<StructureProcessorList> processors = context.lookup(Registries.PROCESSOR_LIST);
        final Holder<StructureTemplatePool> empty = pools.getOrThrow(Pools.EMPTY);
        final Holder<StructureProcessorList> moss = processors.getOrThrow(ProcessorLists.MOSSIFY_20_PERCENT);

//        context.register(IMMTemplatePools.OVERWORLD_TRADING_MARKET_START, new StructureTemplatePool(
//                empty,
//                pools(
//                        getPoolElements("town_centers/center", moss, StructureTemplatePool.Projection.RIGID, List.of(10))
//                )
//        ));
    }

//    public static StructureTemplatePool getStartPool() {
//        PillagerOutpostPools
//        return new StructureTemplatePool(
//                new ResourceLocation("pillager_outpost/base_plates"),
//                new ResourceLocation("empty"),
//                ImmutableList.of(Pair.of(
//                        StructurePoolElement.legacy("pillager_outpost/base_plate"), 1)
//                ),
//                StructureTemplatePool.Projection.RIGID
//        );
//        return new StructureTemplatePool(
//                res("town_centers"),
//                new ResourceLocation("empty"),
//                pools(
//                        getPoolElements("town_centers/meeting_point", ProcessorLists.EMPTY, StructureTemplatePool.Projection.RIGID, List.of(10))
//                )
//        );
//    }
}
