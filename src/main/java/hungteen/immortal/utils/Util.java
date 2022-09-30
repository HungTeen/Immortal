package hungteen.immortal.utils;

import com.mojang.logging.LogUtils;
import hungteen.immortal.ImmortalMod;
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

    public static String id(){
        return ImmortalMod.MOD_ID;
    }

    /**
     * get resource with mc prefix.
     */
    public static ResourceLocation mcPrefix(String name) {
        return new ResourceLocation(name);
    }

    /**
     * get resource with forge prefix.
     */
    public static ResourceLocation forgePrefix(String name) {
        return new ResourceLocation("forge", name);
    }

    /**
     * get resource with mod prefix.
     */
    public static ResourceLocation prefix(String name) {
        return new ResourceLocation(id(), name);
    }

    public static void debug(String message, Object... arguments){
        LOGGER.debug(message, arguments);
    }

    public static void warn(String message, Object... arguments){
        LOGGER.warn(message, arguments);
    }

}
