package hungteen.imm.common.world.structure.structures;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import hungteen.imm.common.world.structure.IMMProcessorLists;
import hungteen.imm.common.world.structure.IMMTemplatePools;
import hungteen.imm.util.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.data.worldgen.placement.VillagePlacements;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import java.util.List;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/10/20 22:58
 **/
public class SpiritualFlameAltar {

    public static StructureProcessorList getAltarProcessor() {
        return new StructureProcessorList(ImmutableList.of(
                new RuleProcessor(
                        ImmutableList.of(
                                new ProcessorRule(
                                        new RandomBlockMatchTest(Blocks.STONE_BRICKS, 0.1F),
                                        AlwaysTrueTest.INSTANCE,
                                        Blocks.CRACKED_STONE_BRICKS.defaultBlockState()
                                )
                        )
                )
        ));
    }

    public static void initPools(BootstapContext<StructureTemplatePool> context) {
        final HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);
        final HolderGetter<StructureProcessorList> processors = context.lookup(Registries.PROCESSOR_LIST);
        final Holder<StructureTemplatePool> empty = pools.getOrThrow(Pools.EMPTY);
        final Holder<StructureProcessorList> none = processors.getOrThrow(IMMProcessorLists.EMPTY);
        final Holder<StructureProcessorList> brickCrack = processors.getOrThrow(IMMProcessorLists.FLAME_ALTAR_CRACKED);

        context.register(IMMTemplatePools.SPIRITUAL_FLAME_ALTAR_CENTER, new StructureTemplatePool(
                empty,
                List.of(
                        Pair.of(
                                StructurePoolElement.legacy(name("altar"), brickCrack)
                                        .apply(StructureTemplatePool.Projection.RIGID), 2
                        )
                )
        ));

        context.register(IMMTemplatePools.SPIRITUAL_FLAME_ALTAR_SIDE_PLATES, new StructureTemplatePool(
                empty,
                List.of(
                        Pair.of(
                                StructurePoolElement.legacy(name("side_plate_01"))
                                        .apply(StructureTemplatePool.Projection.RIGID), 2
                        ),
                        Pair.of(
                                StructurePoolElement.legacy(name("side_plate_02"))
                                        .apply(StructureTemplatePool.Projection.RIGID), 2
                        ),
                        Pair.of(
                                StructurePoolElement.legacy(name("side_plate_03"))
                                        .apply(StructureTemplatePool.Projection.RIGID), 2
                        ),
                        Pair.of(
                                StructurePoolElement.legacy(name("side_plate_04"))
                                        .apply(StructureTemplatePool.Projection.RIGID), 2
                        )
                )
        ));

        context.register(IMMTemplatePools.SPIRITUAL_FLAME_ALTAR_COLUMNS, new StructureTemplatePool(
                empty,
                List.of(
                        Pair.of(
                                StructurePoolElement.legacy(name("column_01"), brickCrack)
                                        .apply(StructureTemplatePool.Projection.RIGID), 10
                        ),
                        Pair.of(
                                StructurePoolElement.legacy(name("column_02"), brickCrack)
                                        .apply(StructureTemplatePool.Projection.RIGID), 4
                        ),
                        Pair.of(
                                StructurePoolElement.legacy(name("column_03"), brickCrack)
                                        .apply(StructureTemplatePool.Projection.RIGID), 10
                        ),
                        Pair.of(
                                StructurePoolElement.empty().apply(StructureTemplatePool.Projection.RIGID), 1
                        )
                )
        ));

        context.register(IMMTemplatePools.SPIRITUAL_FLAME_ALTAR_WALLS, new StructureTemplatePool(
                empty,
                List.of(
                        Pair.of(
                                StructurePoolElement.legacy(name("wall_01"), brickCrack)
                                        .apply(StructureTemplatePool.Projection.RIGID), 10
                        ),
                        Pair.of(
                                StructurePoolElement.legacy(name("wall_02"), brickCrack)
                                        .apply(StructureTemplatePool.Projection.RIGID), 10
                        ),
                        Pair.of(
                                StructurePoolElement.legacy(name("wall_03"), brickCrack)
                                        .apply(StructureTemplatePool.Projection.RIGID), 10
                        ),
                        Pair.of(
                                StructurePoolElement.empty().apply(StructureTemplatePool.Projection.RIGID), 15
                        )
                )
        ));

        context.register(IMMTemplatePools.SPIRITUAL_FLAME_ALTAR_BI_FANG, new StructureTemplatePool(
                empty,
                List.of(
                        Pair.of(
                                StructurePoolElement.legacy(Util.prefixName("entities/bi_fang"))
                                        .apply(StructureTemplatePool.Projection.RIGID), 5
                        )
                )
        ));
    }

    private static String name(String name) {
        return Util.prefixName("spiritual_flame_altar/" + name);
    }

}
