package hungteen.imm.common.world.levelgen;

import hungteen.imm.common.world.levelgen.structure.OverworldTradingMarket;
import hungteen.imm.common.world.levelgen.structure.SpiritualPlainsVillage;
import hungteen.imm.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-20 10:20
 **/
public class IMMTemplatePools {
    
    public static final ResourceKey<StructureTemplatePool> PLAINS_VILLAGE_START = create("village/plains/town_centers");
    public static final ResourceKey<StructureTemplatePool> PLAINS_VILLAGE_END = create("village/plains/end");
    public static final ResourceKey<StructureTemplatePool> PLAINS_VILLAGE_HOUSE = create("village/plains/houses");
    public static final ResourceKey<StructureTemplatePool> PLAINS_VILLAGE_STREET = create("village/plains/streets");
    public static final ResourceKey<StructureTemplatePool> PLAINS_VILLAGE_DECOR = create("village/plains/decor");
    public static final ResourceKey<StructureTemplatePool> OVERWORLD_TRADING_MARKET_START = create("trading_market/overworld/center");

    public static void register(BootstapContext<StructureTemplatePool> context){
        OverworldTradingMarket.initPools(context);
        SpiritualPlainsVillage.initPools(context);
    }

    private static ResourceKey<StructureTemplatePool> create(String name) {
        return ResourceKey.create(Registries.TEMPLATE_POOL, Util.prefix(name));
    }
    
}
