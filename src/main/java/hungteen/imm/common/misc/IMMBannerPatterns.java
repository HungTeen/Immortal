package hungteen.imm.common.misc;

import hungteen.imm.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/8 19:51
 */
public interface IMMBannerPatterns {

    DeferredRegister<BannerPattern> BANNER_PATTERNS = DeferredRegister.create(Registries.BANNER_PATTERN, Util.id());

    RegistryObject<BannerPattern> CONTINUOUS_MOUNTAIN = register("continuous_mountain");
    RegistryObject<BannerPattern> FLOWING_CLOUD = register("flowing_cloud");
    RegistryObject<BannerPattern> FOLDED_THUNDER = register("folded_thunder");
    RegistryObject<BannerPattern> RHOMBUS = register("rhombus");
    RegistryObject<BannerPattern> TALISMAN = register("talisman");
    RegistryObject<BannerPattern> COILED_LOONG = register("coiled_loong");
    RegistryObject<BannerPattern> HOVERING_PHOENIX = register("hovering_phoenix");

    static RegistryObject<BannerPattern> register(String name) {
        return BANNER_PATTERNS.register(name, () -> new BannerPattern(name));
    }

    /**
     * {@link hungteen.imm.ImmortalMod#defferRegister(IEventBus)}
     */
    static void register(IEventBus event) {
        BANNER_PATTERNS.register(event);
    }

}
