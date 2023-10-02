package hungteen.imm.common.world.structure;

import hungteen.imm.common.world.structure.structures.PlainsTradingMarket;
import hungteen.imm.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-21 11:48
 **/
public interface IMMProcessorLists {

    ResourceKey<StructureProcessorList> EMPTY = create("empty");
    ResourceKey<StructureProcessorList> PLAINS_TRADING_MARKET_STREET_ROT = create("plains_trading_market_street_rot");

    static void register(BootstapContext<StructureProcessorList> context) {
        context.register(EMPTY, new StructureProcessorList(List.of()));
        context.register(PLAINS_TRADING_MARKET_STREET_ROT, PlainsTradingMarket.getStreetProcessor());
    }

    static ResourceKey<StructureProcessorList> create(String name){
        return ResourceKey.create(Registries.PROCESSOR_LIST, Util.prefix(name));
    }

}
