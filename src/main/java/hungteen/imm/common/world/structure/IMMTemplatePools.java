package hungteen.imm.common.world.structure;

import hungteen.imm.common.world.structure.structures.PlainsTradingMarket;
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
public interface IMMTemplatePools {
    
    ResourceKey<StructureTemplatePool> PLAINS_TRADING_MARKET_START = create("trading_market/plains/town_centers");
    ResourceKey<StructureTemplatePool> PLAINS_TRADING_MARKET_END = create("trading_market/plains/end");
    ResourceKey<StructureTemplatePool> PLAINS_TRADING_MARKET_STREET = create("trading_market/plains/streets");
    ResourceKey<StructureTemplatePool> PLAINS_TRADING_MARKET_HOUSE = create("trading_market/plains/houses");
    ResourceKey<StructureTemplatePool> PLAINS_TRADING_MARKET_DECOR = create("trading_market/plains/decor");
    ResourceKey<StructureTemplatePool> PLAINS_TRADING_MARKET_CULTIVATORS = create("trading_market/plains/cultivators");
    ResourceKey<StructureTemplatePool> PLAINS_TRADING_MARKET_TREES = create("trading_market/plains/trees");
    ResourceKey<StructureTemplatePool> PLAINS_TRADING_MARKET_ANIMALS = create("trading_market/plains/animals");

    static void register(BootstapContext<StructureTemplatePool> context){
        PlainsTradingMarket.initPools(context);
//        SpiritualPlainsVillage.initPools(context);
    }

//    static SpawnEntityPoolElement entity(EntityType<?> type, StructureTemplatePool.Projection projection){
//        return new SpawnEntityPoolElement(type, projection);
//    }

    private static ResourceKey<StructureTemplatePool> create(String name) {
        return ResourceKey.create(Registries.TEMPLATE_POOL, Util.prefix(name));
    }
    
}
