package hungteen.imm.common.world.levelgen;

import hungteen.imm.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-20 09:39
 **/
public class IMMStructures {

    public static final ResourceKey<Structure> SPIRITUAL_PLAINS_VILLAGE = registerStructure("spiritual_plains_village");
    public static final ResourceKey<Structure> OVERWORLD_TRADING_MARKET = registerStructure("overworld_trading_market");

    public static void register(BootstapContext<Structure> context) {
//        OverworldTradingMarket.initStructures(context);
//        SpiritualPlainsVillage.initStructures(context);
    }

    private static ResourceKey<Structure> registerStructure(String name) {
        return ResourceKey.create(Registries.STRUCTURE, Util.prefix(name));
    }

}
