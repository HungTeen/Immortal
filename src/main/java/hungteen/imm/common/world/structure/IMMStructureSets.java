package hungteen.imm.common.world.structure;

import hungteen.imm.utils.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.StructureSet;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/28 20:17
 */
public class IMMStructureSets {

    public static final ResourceKey<StructureSet> SPIRITUAL_PLAINS_VILLAGE_SET = registerStructureSet("spiritual_plains_villages");
    public static final ResourceKey<StructureSet> OVERWORLD_TRADING_MARKET_SET = registerStructureSet("overworld_trading_markets");

    public static void register(BootstapContext<StructureSet> context) {

    }

    private static ResourceKey<StructureSet> registerStructureSet(String name) {
        return ResourceKey.create(Registries.STRUCTURE_SET, Util.prefix(name));
    }

}
