package hungteen.immortal.common.misc;

import hungteen.immortal.utils.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BannerPatternTags;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/8 19:51
 */
public class ImmortalBannerPatterns {

    private static DeferredRegister<BannerPattern> BANNER_PATTERNS = DeferredRegister.create(Registries.BANNER_PATTERN, Util.id());

    public static final RegistryObject<BannerPattern> CONTINUOUS_MOUNTAIN = BANNER_PATTERNS.register("continuous_mountain", () -> new BannerPattern("continuous_mountain"));
    public static final RegistryObject<BannerPattern> FLOWING_CLOUD = BANNER_PATTERNS.register("flowing_cloud", () -> new BannerPattern("flowing_cloud"));
    public static final RegistryObject<BannerPattern> FOLDED_THUNDER = BANNER_PATTERNS.register("folded_thunder", () -> new BannerPattern("folded_thunder"));
    public static final RegistryObject<BannerPattern> RHOMBUS = BANNER_PATTERNS.register("rhombus", () -> new BannerPattern("rhombus"));

    /**
     * {@link hungteen.immortal.ImmortalMod#defferRegister(IEventBus)}
     */
    public static void register(IEventBus event){
        BANNER_PATTERNS.register(event);
    }

}
