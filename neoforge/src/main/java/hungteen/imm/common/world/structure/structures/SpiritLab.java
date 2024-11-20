package hungteen.imm.common.world.structure.structures;

import com.mojang.datafixers.util.Pair;
import hungteen.htlib.util.helper.impl.FeatureHelper;
import hungteen.imm.common.world.feature.IMMStructurePlacements;
import hungteen.imm.common.world.structure.IMMProcessorLists;
import hungteen.imm.common.world.structure.IMMTemplatePools;
import hungteen.imm.util.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.List;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/10/29 9:14
 **/
public class SpiritLab {

    public static void initPools(BootstrapContext<StructureTemplatePool> context) {
        final HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);
        final HolderGetter<StructureProcessorList> processors = context.lookup(Registries.PROCESSOR_LIST);
        final HolderGetter<PlacedFeature> features = FeatureHelper.placed().lookup(context);
        final Holder<StructureTemplatePool> empty = pools.getOrThrow(Pools.EMPTY);
        final Holder<StructureProcessorList> none = processors.getOrThrow(IMMProcessorLists.EMPTY);
        final Holder<PlacedFeature> cherry = features.getOrThrow(IMMStructurePlacements.LAB_CHERRY_CHECKED);

        context.register(IMMTemplatePools.SPIRIT_LAB_CENTER, new StructureTemplatePool(
                empty,
                List.of(
                        Pair.of(
                                StructurePoolElement.legacy(name("center_01"))
                                        .apply(StructureTemplatePool.Projection.RIGID), 2
                        )
                )
        ));

        context.register(IMMTemplatePools.SPIRIT_LAB_MAIN, new StructureTemplatePool(
                empty,
                List.of(
                        Pair.of(
                                StructurePoolElement.legacy(name("lab_01"))
                                        .apply(StructureTemplatePool.Projection.RIGID), 2
                        )
                )
        ));

        context.register(IMMTemplatePools.SPIRIT_LAB_SIDE, new StructureTemplatePool(
                empty,
                List.of(
                        Pair.of(
                                StructurePoolElement.legacy(name("side_plate_01"))
                                        .apply(StructureTemplatePool.Projection.RIGID), 16
                        )
                )
        ));

        context.register(IMMTemplatePools.SPIRIT_LAB_HOUSES, new StructureTemplatePool(
                empty,
                List.of(
                        Pair.of(
                                StructurePoolElement.legacy(name("house_01"))
                                        .apply(StructureTemplatePool.Projection.RIGID), 10
                        ),
                        Pair.of(
                                StructurePoolElement.legacy(name("house_02"))
                                        .apply(StructureTemplatePool.Projection.RIGID), 8
                        ),
                        Pair.of(
                                StructurePoolElement.empty().apply(StructureTemplatePool.Projection.RIGID), 4
                        )
                )
        ));

        context.register(IMMTemplatePools.SPIRIT_LAB_DECORS, new StructureTemplatePool(
                empty,
                List.of(
                        Pair.of(
                                StructurePoolElement.legacy(name("decor_01"))
                                        .apply(StructureTemplatePool.Projection.RIGID), 5
                        )
                )
        ));

        context.register(IMMTemplatePools.SPIRIT_LAB_TREES, new StructureTemplatePool(
                empty,
                List.of(
                        Pair.of(
                                StructurePoolElement.feature(cherry)
                                        .apply(StructureTemplatePool.Projection.RIGID), 5
                        ),
                        Pair.of(
                                StructurePoolElement.empty().apply(StructureTemplatePool.Projection.RIGID), 3
                        )
                )
        ));
    }

    private static String name(String name) {
        return Util.prefixName("spirit_lab/" + name);
    }

}
