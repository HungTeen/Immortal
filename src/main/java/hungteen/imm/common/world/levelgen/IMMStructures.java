package hungteen.imm.common.world.levelgen;

import hungteen.imm.common.tag.IMMBiomeTags;
import hungteen.imm.common.world.levelgen.structure.TeleportRuinStructure;
import hungteen.imm.util.Util;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;

import java.util.Map;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-20 09:39
 **/
public class IMMStructures {

    public static final ResourceKey<Structure> TELEPORT_RUIN = create("teleport_ruin");
    public static final ResourceKey<Structure> SPIRITUAL_PLAINS_VILLAGE = create("spiritual_plains_village");
    public static final ResourceKey<Structure> OVERWORLD_TRADING_MARKET = create("overworld_trading_market");

    public static void register(BootstapContext<Structure> context) {
//        OverworldTradingMarket.initStructures(context);
//        SpiritualPlainsVillage.initStructures(context);
        final HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        context.register(TELEPORT_RUIN, new TeleportRuinStructure(
                new Structure.StructureSettings(
                        biomes.getOrThrow(IMMBiomeTags.HAS_TELEPORT_RUIN),
                        Map.of(),
                        GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
                        TerrainAdjustment.NONE
                ))
        );
    }

    private static ResourceKey<Structure> create(String name) {
        return ResourceKey.create(Registries.STRUCTURE, Util.prefix(name));
    }

}
