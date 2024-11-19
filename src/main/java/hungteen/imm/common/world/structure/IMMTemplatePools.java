package hungteen.imm.common.world.structure;

import hungteen.imm.common.world.structure.structures.PlainsTradingMarket;
import hungteen.imm.common.world.structure.structures.SpiritLab;
import hungteen.imm.common.world.structure.structures.SpiritualFlameAltar;
import hungteen.imm.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
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
    ResourceKey<StructureTemplatePool> SPIRITUAL_FLAME_ALTAR_CENTER = create("spiritual_flame_altar/center");
    ResourceKey<StructureTemplatePool> SPIRITUAL_FLAME_ALTAR_SIDE_PLATES = create("spiritual_flame_altar/side_plates");
    ResourceKey<StructureTemplatePool> SPIRITUAL_FLAME_ALTAR_COLUMNS = create("spiritual_flame_altar/columns");
    ResourceKey<StructureTemplatePool> SPIRITUAL_FLAME_ALTAR_WALLS = create("spiritual_flame_altar/walls");
    ResourceKey<StructureTemplatePool> SPIRITUAL_FLAME_ALTAR_BI_FANG = create("spiritual_flame_altar/bi_fang");
    ResourceKey<StructureTemplatePool> SPIRIT_LAB_CENTER = create("spirit_lab/center");
    ResourceKey<StructureTemplatePool> SPIRIT_LAB_MAIN = create("spirit_lab/labs");
    ResourceKey<StructureTemplatePool> SPIRIT_LAB_SIDE = create("spirit_lab/sides");
    ResourceKey<StructureTemplatePool> SPIRIT_LAB_HOUSES = create("spirit_lab/houses");
    ResourceKey<StructureTemplatePool> SPIRIT_LAB_DECORS = create("spirit_lab/decors");
    ResourceKey<StructureTemplatePool> SPIRIT_LAB_TREES = create("spirit_lab/trees");

    static void register(BootstrapContext<StructureTemplatePool> context){
        PlainsTradingMarket.initPools(context);
        SpiritualFlameAltar.initPools(context);
        SpiritLab.initPools(context);
    }

    private static ResourceKey<StructureTemplatePool> create(String name) {
        return ResourceKey.create(Registries.TEMPLATE_POOL, Util.prefix(name));
    }
    
}
