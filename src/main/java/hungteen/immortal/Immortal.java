package hungteen.immortal;

import com.mojang.logging.LogUtils;
import hungteen.immortal.common.capability.CapabilityHandler;
import hungteen.immortal.common.command.CommandHandler;
import hungteen.immortal.common.impl.SpiritualRoots;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
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

    // Mod ID.
    public static final String MOD_ID = "immortal";

    public Immortal() {
        //get mod event bus.
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(EventPriority.NORMAL, CapabilityHandler::registerCapabilities);

        //get forge event bus.
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addGenericListener(Entity.class, CapabilityHandler::attachCapabilities);
        forgeBus.addListener(EventPriority.NORMAL, CommandHandler::init);

        ModConfigs.init();

        coreRegister();
    }

    /**
     * register stuffs at {@link Immortal#Immortal()}.
     */
    public static void coreRegister() {
        SpiritualRoots.SpiritualRoot.register();
    }

}
