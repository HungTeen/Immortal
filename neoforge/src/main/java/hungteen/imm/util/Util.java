package hungteen.imm.util;

import com.mojang.logging.LogUtils;
import hungteen.htlib.api.util.helper.HTModIDHelper;
import hungteen.htlib.util.NeoHelper;
import hungteen.htlib.util.helper.impl.VanillaHelper;
import hungteen.imm.common.IMMProxy;
import hungteen.imm.IMMInitializer;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-09-24 15:00
 **/
public class Util {

    // Directly reference a slf4j logger。
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final HTModIDHelper HELPER = () -> IMMInitializer.MOD_ID;

    public static HTModIDHelper get(){
        return HELPER;
    }

    public static HTModIDHelper mc(){
        return VanillaHelper.get();
    }

    public static HTModIDHelper neo(){
        return NeoHelper.get();
    }

    public static String id(){
        return get().getModID();
    }

    /**
     * get resource with mod prefix.
     */
    public static ResourceLocation prefix(String name) {
        return get().prefix(name);
    }

    public static boolean in(ResourceLocation location){
        return get().in(location);
    }

    public static String prefixName(String name) {
        return prefix(name).toString();
    }

    public static String toString(ResourceKey<?> resourceKey){
        return resourceKey.registry() + ":" + resourceKey.location();
    }

    public static boolean isDebugMode(){
        return getProxy().isDebugMode();
    }

    public static IMMProxy getProxy(){
        return IMMProxy.get();
    }

    public static <T> List<Holder<T>> wrap(HolderGetter<T> getter, List<ResourceKey<T>> keys){
        return keys.stream().map(getter::getOrThrow).collect(Collectors.toList());
    }

    public static void error(String message, Object... arguments){
        LOGGER.error(message, arguments);
    }

    public static void debug(String message, Object... arguments){
        LOGGER.debug(message, arguments);
    }

    public static void warn(String message, Object... arguments){
        LOGGER.warn(message, arguments);
    }

    public static void info(String message, Object... arguments){
        LOGGER.info(message, arguments);
    }

}
