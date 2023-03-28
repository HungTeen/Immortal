package hungteen.imm.common.world.structure;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import hungteen.imm.common.tag.ImmortalBiomeTags;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.PillagerOutpostPools;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
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

//    public static Function<Registry<Biome>, JigsawStructure> getStructure() {
//        return (biomeRegistry) -> new JigsawStructure(
//                new Structure.StructureSettings(
//                        biomeRegistry.getOrCreateTag(ImmortalBiomeTags.HAS_OVERWORLD_TRADING_MARKET),
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
//                ImmortalTemplatePools.OVERWORLD_TRADING_MARKET_START.getHolder().get(),
//                Optional.empty(),
//                6,
//                ConstantHeight.of(new VerticalAnchor.Absolute(0)),
//                true,
//                Optional.empty(),
//                80
//        );
//    }
//
//    public static Function<Registry<Structure>, StructureSet> getStructureSet() {
//        return (structureRegistry) -> new StructureSet(
//                structureRegistry.getHolderOrThrow(ImmortalStructures.OVERWORLD_TRADING_MARKET),
//                new RandomSpreadStructurePlacement(
//                        32,
//                        8,
//                        RandomSpreadType.LINEAR,
//                        397815004
//                )
//        );
//    }

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
