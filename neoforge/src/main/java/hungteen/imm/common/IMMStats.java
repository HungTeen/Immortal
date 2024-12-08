package hungteen.imm.common;

import hungteen.imm.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/3/16 10:26
 */
public class IMMStats {

    private static DeferredRegister<ResourceLocation> CUSTOM_STATS = DeferredRegister.create(Registries.CUSTOM_STAT, Util.id());
    private static DeferredRegister<StatType<?>> STAT_TYPES = DeferredRegister.create(Registries.STAT_TYPE, Util.id());

//    public static final RegistryObject<ResourceLocation> CONTINUOUS_MOUNTAIN = CUSTOM_STATS.initialize("continuous_mountain", () -> new BannerPattern("continuous_mountain"));

//    /**
//     * {@link hungteen.immortal.ImmortalMod#defferRegister(IEventBus)}
//     */
    public static void register(IEventBus event){
        STAT_TYPES.register(event);
        CUSTOM_STATS.register(event);
    }

}
