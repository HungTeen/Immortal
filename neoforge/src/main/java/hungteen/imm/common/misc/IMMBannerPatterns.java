package hungteen.imm.common.misc;

import hungteen.htlib.api.registry.HTHolder;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.common.impl.registry.HTVanillaRegistry;
import hungteen.htlib.util.NeoHelper;
import hungteen.imm.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.neoforged.bus.api.IEventBus;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/8 19:51
 */
public interface IMMBannerPatterns {

    HTVanillaRegistry<BannerPattern> BANNER_PATTERNS = HTRegistryManager.vanilla(Registries.BANNER_PATTERN, Util.id());

    HTHolder<BannerPattern> CONTINUOUS_MOUNTAIN = register("continuous_mountain");
    HTHolder<BannerPattern> FLOWING_CLOUD = register("flowing_cloud");
    HTHolder<BannerPattern> FOLDED_THUNDER = register("folded_thunder");
    HTHolder<BannerPattern> RHOMBUS = register("rhombus");
    HTHolder<BannerPattern> TALISMAN = register("talisman");
    HTHolder<BannerPattern> COILED_LOONG = register("coiled_loong");
    HTHolder<BannerPattern> HOVERING_PHOENIX = register("hovering_phoenix");

    static HTHolder<BannerPattern> register(String name) {
        return BANNER_PATTERNS.register(name, () -> new BannerPattern(Util.prefix(name), name));
    }

    static void initialize(IEventBus event) {
        NeoHelper.initRegistry(BANNER_PATTERNS, event);
    }

}
