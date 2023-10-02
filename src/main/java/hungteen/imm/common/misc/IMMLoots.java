package hungteen.imm.common.misc;

import com.google.common.collect.Sets;
import hungteen.imm.util.Util;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.Set;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/10/2 17:00
 **/
public interface IMMLoots {

    Set<ResourceLocation> LOCATIONS = Sets.newHashSet();

    ResourceLocation PLAINS_TRADING_MARKET_COMMON = chest("trading_market/plains_common");
    ResourceLocation PLAINS_TRADING_MARKET_RARE = chest("trading_market/plains_rare");

    private static ResourceLocation chest(String name) {
        return create("chests/" + name);
    }

    private static ResourceLocation create(String name) {
        return create(Util.prefix(name));
    }

    private static ResourceLocation create(ResourceLocation location) {
        if (LOCATIONS.add(location)) {
            return location;
        } else {
            throw new IllegalArgumentException(location + " is already a registered built-in loot table");
        }
    }

    static Set<ResourceLocation> all() {
        return Collections.unmodifiableSet(LOCATIONS);
    }
}
