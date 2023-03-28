package hungteen.imm.common.misc;

import hungteen.imm.utils.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.StatType;
import net.minecraft.stats.Stats;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/16 10:26
 */
public class ImmortalStats {

    private static DeferredRegister<ResourceLocation> CUSTOM_STATS = DeferredRegister.create(Registries.CUSTOM_STAT, Util.id());
    private static DeferredRegister<StatType<?>> STAT_TYPES = DeferredRegister.create(ForgeRegistries.STAT_TYPES, Util.id());

//    public static final RegistryObject<ResourceLocation> CONTINUOUS_MOUNTAIN = CUSTOM_STATS.register("continuous_mountain", () -> new BannerPattern("continuous_mountain"));

//    /**
//     * {@link hungteen.immortal.ImmortalMod#defferRegister(IEventBus)}
//     */
    public static void register(IEventBus event){
        STAT_TYPES.register(event);
        CUSTOM_STATS.register(event);
    }

}
