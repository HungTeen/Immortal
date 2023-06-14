package hungteen.imm.util;

import com.mojang.logging.LogUtils;
import hungteen.htlib.util.helper.ForgeHelper;
import hungteen.htlib.util.helper.IModIDHelper;
import hungteen.htlib.util.helper.VanillaHelper;
import hungteen.imm.CommonProxy;
import hungteen.imm.ImmortalMod;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-24 15:00
 **/
public class Util {

    // Directly reference a slf4j logger。
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final IModIDHelper HELPER = () -> ImmortalMod.MOD_ID;

    public static IModIDHelper get(){
        return HELPER;
    }

    public static String id(){
        return ImmortalMod.MOD_ID;
    }

    public static String mc(){
        return VanillaHelper.get().getModID();
    }

    public static String forge(){
        return ForgeHelper.get().getModID();
    }

    /**
     * get resource with mc prefix.
     */
    public static ResourceLocation mcPrefix(String name) {
        return VanillaHelper.get().prefix(name);
    }

    /**
     * get resource with forge prefix.
     */
    public static ResourceLocation forgePrefix(String name) {
        return ForgeHelper.get().prefix(name);
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

    public static CommonProxy getProxy(){
        return ImmortalMod.PROXY;
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
