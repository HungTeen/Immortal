package hungteen.immortal.common.world.structure;

import net.minecraft.core.Registry;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-03-24 22:43
 **/
public class OverworldTradingMarket {

    public static Function<Registry<Structure>, StructureSet> getStructureSet() {
        return (structureRegistry) -> new StructureSet(
                structureRegistry.getOrCreateHolderOrThrow(ImmortalStructures.OVERWORLD_TRADING_MARKET),
                new RandomSpreadStructurePlacement(
                        32,
                        8,
                        RandomSpreadType.LINEAR,
                        397815004
                )
        );
    }

    public static Function<Registry<Biome>, JigsawStructure> getStructure() {
//        new JigsawStructure(structure(
//                BiomeTags.HAS_PILLAGER_OUTPOST,
//                Map.of(
//                        MobCategory.MONSTER, new StructureSpawnOverride(
//                                StructureSpawnOverride.BoundingBoxType.STRUCTURE,
//                                WeightedRandomList.create(
//                                        new MobSpawnSettings.SpawnerData(EntityType.PILLAGER, 1, 1, 1))
//                        )
//                ),
//                GenerationStep.Decoration.SURFACE_STRUCTURES,
//                TerrainAdjustment.BEARD_THIN
//        ),
//                PillagerOutpostPools.START,
//                7,
//                ConstantHeight.of(VerticalAnchor.absolute(0)),
//                true,
//                Heightmap.Types.WORLD_SURFACE_WG)
//    )
        return (biomeRegistry) -> new JigsawStructure(
                new Structure.StructureSettings(
                        biomeRegistry.getOrCreateTag(BiomeTags.HAS_VILLAGE_PLAINS),
                        Map.of(),
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.BEARD_THIN
                ),
                ImmortalTemplatePools.PLAINS_VILLAGE_START_POOL.getHolder().get(),
                Optional.empty(),
                6,
                ConstantHeight.of(new VerticalAnchor.Absolute(0)),
                true,
                Optional.of(Heightmap.Types.WORLD_SURFACE_WG),
                80
        );
    }
}
