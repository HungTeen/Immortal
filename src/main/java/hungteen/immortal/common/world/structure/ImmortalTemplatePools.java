package hungteen.immortal.common.world.structure;

import hungteen.immortal.utils.Util;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-20 10:20
 **/
public class ImmortalTemplatePools {

    private static final DeferredRegister<StructureTemplatePool> TEMPLATE_POOLS = DeferredRegister.create(Registry.TEMPLATE_POOL_REGISTRY, Util.id());

    public static final RegistryObject<StructureTemplatePool> PLAINS_VILLAGE_START = TEMPLATE_POOLS.register("village/plains/town_centers", SpiritualPlainsVillage::getStartPool);
    public static final RegistryObject<StructureTemplatePool> PLAINS_VILLAGE_HOUSE = TEMPLATE_POOLS.register("village/plains/houses", SpiritualPlainsVillage::getHousePool);
    public static final RegistryObject<StructureTemplatePool> PLAINS_VILLAGE_STREET = TEMPLATE_POOLS.register("village/plains/streets", SpiritualPlainsVillage::getStreetPool);
    public static final RegistryObject<StructureTemplatePool> PLAINS_VILLAGE_DECOR = TEMPLATE_POOLS.register("village/plains/decor", SpiritualPlainsVillage::getDecorPool);
    public static final RegistryObject<StructureTemplatePool> OVERWORLD_TRADING_MARKET_START = TEMPLATE_POOLS.register("trading_market/overworld/center", OverworldTradingMarket::getStartPool);

    public static void register(IEventBus event){
        TEMPLATE_POOLS.register(event);
    }
}
