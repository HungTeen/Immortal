package hungteen.imm.common.world.structure;

import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.entity.IMMMobCategories;
import hungteen.imm.common.tag.IMMBiomeTags;
import hungteen.imm.common.world.structure.structures.TeleportRuinStructure;
import hungteen.imm.util.Util;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-11-20 09:39
 **/
public interface IMMStructures {

    ResourceKey<Structure> TELEPORT_RUIN = create("teleport_ruin");
    ResourceKey<Structure> PLAINS_TRADING_MARKET = create("plains_trading_market");
    ResourceKey<Structure> SPIRITUAL_FLAME_ALTAR = create("spiritual_flame_altar");
    ResourceKey<Structure> SPIRIT_LAB = create("spirit_lab");

    static void register(BootstrapContext<Structure> context) {
//        SpiritualPlainsVillage.initStructures(context);
        final HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        final HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);
        context.register(TELEPORT_RUIN, new TeleportRuinStructure(
                new Structure.StructureSettings(
                        biomes.getOrThrow(IMMBiomeTags.HAS_TELEPORT_RUIN),
                        Map.of(),
                        GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
                        TerrainAdjustment.NONE
                ))
        );
        context.register(PLAINS_TRADING_MARKET, new JigsawStructure(
                new Structure.StructureSettings(
                        biomes.getOrThrow(IMMBiomeTags.HAS_PLAINS_TRADING_MARKET),
                        Map.of(
                                IMMMobCategories.HUMAN, new StructureSpawnOverride(
                                        StructureSpawnOverride.BoundingBoxType.STRUCTURE,
                                        WeightedRandomList.create(List.of(
                                                        new MobSpawnSettings.SpawnerData(
                                                                IMMEntities.SPIRITUAL_BEGINNER_CULTIVATOR.get(), 25, 1, 1
                                                        ),
                                                        new MobSpawnSettings.SpawnerData(
                                                                IMMEntities.EMPTY_CULTIVATOR.get(), 10, 1, 1
                                                        )
                                                )
                                        )
                                )
                        ),
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.BEARD_THIN
                ),
                pools.getOrThrow(IMMTemplatePools.PLAINS_TRADING_MARKET_START),
                Optional.empty(),
                6,
                ConstantHeight.of(new VerticalAnchor.Absolute(0)),
                true,
                Optional.of(Heightmap.Types.WORLD_SURFACE_WG),
                60,
                List.of(),
                JigsawStructure.DEFAULT_DIMENSION_PADDING,
                JigsawStructure.DEFAULT_LIQUID_SETTINGS
        ));
        context.register(SPIRITUAL_FLAME_ALTAR, new JigsawStructure(
                new Structure.StructureSettings(
                        biomes.getOrThrow(IMMBiomeTags.HAS_SPIRITUAL_FLAME_ALTAR),
                        Map.of(),
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.BEARD_THIN
                ),
                pools.getOrThrow(IMMTemplatePools.SPIRITUAL_FLAME_ALTAR_CENTER),
                Optional.empty(),
                5,
                ConstantHeight.of(new VerticalAnchor.Absolute(0)),
                true,
                Optional.of(Heightmap.Types.WORLD_SURFACE_WG),
                50,
                List.of(),
                JigsawStructure.DEFAULT_DIMENSION_PADDING,
                JigsawStructure.DEFAULT_LIQUID_SETTINGS
        ));
        context.register(SPIRIT_LAB, new JigsawStructure(
                new Structure.StructureSettings(
                        biomes.getOrThrow(IMMBiomeTags.HAS_SPIRIT_LAB),
                        Map.of(),
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.BEARD_THIN
                ),
                pools.getOrThrow(IMMTemplatePools.SPIRIT_LAB_CENTER),
                Optional.empty(),
                4,
                ConstantHeight.of(new VerticalAnchor.Absolute(0)),
                true,
                Optional.of(Heightmap.Types.WORLD_SURFACE_WG),
                50,
                List.of(),
                JigsawStructure.DEFAULT_DIMENSION_PADDING,
                JigsawStructure.DEFAULT_LIQUID_SETTINGS
        ));
    }

    static ResourceKey<Structure> create(String name) {
        return ResourceKey.create(Registries.STRUCTURE, Util.prefix(name));
    }

}
