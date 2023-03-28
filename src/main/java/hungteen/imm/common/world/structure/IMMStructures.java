package hungteen.imm.common.world.structure;

import hungteen.imm.utils.Util;
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

    /* Structure */
    public static final ResourceKey<Structure> SPIRITUAL_PLAINS_VILLAGE = registerStructure("spiritual_plains_village");
    public static final ResourceKey<Structure> OVERWORLD_TRADING_MARKET = registerStructure("overworld_trading_market");

    public static void register(BootstapContext<Structure> context) {
        context.register(SPIRITUAL_PLAINS_VILLAGE, )
    }

    private static ResourceKey<Structure> registerStructure(String name) {
        return ResourceKey.create(Registries.STRUCTURE, Util.prefix(name));
    }

}
