package hungteen.immortal;

import com.mojang.logging.LogUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.stream.Collectors;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-23 23:32
 **/
@Mod(Immortal.MOD_ID)
public class Immortal {

    // Directly reference a slf4j logger。
    private static final Logger LOGGER = LogUtils.getLogger();
    // Mod ID.
    public static final String MOD_ID = "immortal";
    // Mod Version.
    public static final String MOD_VERSION = "0.1";

    public Immortal()
    {
    }

}
