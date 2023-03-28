package hungteen.immortal.common.world.structure;

import com.mojang.datafixers.util.Pair;
import hungteen.immortal.utils.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-20 09:39
 **/
public class ImmortalStructures {

    private static final Map<ResourceKey<Structure>, Function<Registry<Biome>, ? extends Structure>> STRUCTURE_MAP = new HashMap<>();
    private static final Map<ResourceKey<StructureSet>, Function<Registry<Structure>, StructureSet>> STRUCTURE_SET_MAP = new HashMap<>();

//    /* Structure */
//    public static final ResourceKey<Structure> SPIRITUAL_PLAINS_VILLAGE = registerStructure("spiritual_plains_village");
//    public static final ResourceKey<Structure> OVERWORLD_TRADING_MARKET = registerStructure("overworld_trading_market");
//
//    /* Structure Set */
//    public static final ResourceKey<StructureSet> SPIRITUAL_PLAINS_VILLAGE_SET = registerStructureSet("spiritual_plains_villages");
//    public static final ResourceKey<StructureSet> OVERWORLD_TRADING_MARKET_SET = registerStructureSet("overworld_trading_markets");
//
    /**
     * {@link hungteen.immortal.ImmortalMod#defferRegister(IEventBus)}
     */
    public static void register(){
//        register(
//                OVERWORLD_TRADING_MARKET, OverworldTradingMarket.getStructure(),
//                OVERWORLD_TRADING_MARKET_SET, OverworldTradingMarket.getStructureSet()
//        );
//        register(
//                SPIRITUAL_PLAINS_VILLAGE, SpiritualPlainsVillage.getStructure(),
//                SPIRITUAL_PLAINS_VILLAGE_SET, SpiritualPlainsVillage.getStructureSet()
//        );
    }

    private static void register(ResourceKey<Structure> key1, Function<Registry<Biome>, ? extends Structure> value1, ResourceKey<StructureSet> key2, Function<Registry<Structure>, StructureSet> value2){
        STRUCTURE_MAP.put(key1, value1);
        STRUCTURE_SET_MAP.put(key2, value2);
    }

    public static List<Pair<ResourceKey<Structure>, ? extends Structure>> getStructures(Registry<Biome> biomes){
        return STRUCTURE_MAP.entrySet().stream().map(entry -> Pair.of(entry.getKey(), entry.getValue().apply(biomes))).collect(Collectors.toList());
    }

    public static List<Pair<ResourceKey<StructureSet>, StructureSet>> getStructureSets(Registry<Structure> structures){
        return STRUCTURE_SET_MAP.entrySet().stream().map(entry -> Pair.of(entry.getKey(), entry.getValue().apply(structures))).collect(Collectors.toList());
    }

    private static ResourceKey<Structure> registerStructure(String name) {
        return ResourceKey.create(Registries.STRUCTURE, Util.prefix(name));
    }

    private static ResourceKey<StructureSet> registerStructureSet(String name) {
        return ResourceKey.create(Registries.STRUCTURE_SET, Util.prefix(name));
    }
}
