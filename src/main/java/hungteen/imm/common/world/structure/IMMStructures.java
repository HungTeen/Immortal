package hungteen.imm.common.world.structure;

import hungteen.imm.common.tag.IMMBiomeTags;
import hungteen.imm.common.world.structure.structures.TeleportRuinStructure;
import hungteen.imm.util.Util;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;

import java.util.Map;
import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-20 09:39
 **/
public interface IMMStructures {

    ResourceKey<Structure> TELEPORT_RUIN = create("teleport_ruin");
    ResourceKey<Structure> PLAINS_TRADING_MARKET = create("plains_trading_market");
    //    ResourceKey<Structure> SPIRITUAL_PLAINS_VILLAGE = create("spiritual_plains_village");

    static void register(BootstapContext<Structure> context) {
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
                        Map.of(),
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.BEARD_THIN
                ),
                pools.getOrThrow(IMMTemplatePools.PLAINS_TRADING_MARKET_START),
                Optional.empty(),
                6,
                ConstantHeight.of(new VerticalAnchor.Absolute(0)),
                true,
                Optional.of(Heightmap.Types.WORLD_SURFACE_WG),
                80
        ));
    }

    static ResourceKey<Structure> create(String name) {
        return ResourceKey.create(Registries.STRUCTURE, Util.prefix(name));
    }

}
