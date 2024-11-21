package hungteen.imm.common.misc;

import hungteen.htlib.util.helper.impl.LootHelper;
import hungteen.imm.util.Util;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * @program Immortal
 * @author PangTeen
 * @create 2023/10/2 17:00
 **/
public interface IMMLoots {

    /* Chest Loot Tables */

    ResourceKey<LootTable> PLAINS_TRADING_MARKET_COMMON = chest("trading_market/plains_common");
    ResourceKey<LootTable> PLAINS_TRADING_MARKET_RARE = chest("trading_market/plains_rare");

    private static ResourceKey<LootTable> entity(String name) {
        return create("entities/" + name);
    }

    private static ResourceKey<LootTable> chest(String name) {
        return create("chests/" + name);
    }

    private static ResourceKey<LootTable> create(String name) {
        return create(Util.prefix(name));
    }

    private static ResourceKey<LootTable> create(ResourceLocation location) {
        return BuiltInLootTables.register(LootHelper.get().createKey(location));
    }

}
