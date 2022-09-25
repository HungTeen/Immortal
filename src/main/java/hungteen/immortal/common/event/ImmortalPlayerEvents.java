package hungteen.immortal.common.event;

import hungteen.immortal.Immortal;
import hungteen.immortal.common.capability.player.PlayerDataManager;
import hungteen.immortal.common.event.handler.PlayerEventHandler;
import hungteen.immortal.utils.PlayerUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-25 15:52
 **/
@Mod.EventBusSubscriber(modid = Immortal.MOD_ID)
public class ImmortalPlayerEvents {

    @SubscribeEvent
    public static void tickPlayer(TickEvent.PlayerTickEvent ev) {
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent ev) {
        if (! ev.getPlayer().level.isClientSide) {
            PlayerEventHandler.onPlayerLogin(ev.getPlayer());
        }
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent ev) {
        if (! ev.getPlayer().level.isClientSide) {
            PlayerEventHandler.onPlayerLogout(ev.getPlayer());
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone ev) {
        PlayerEventHandler.clonePlayerData(ev.getOriginal(), ev.getPlayer(), ev.isWasDeath());
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent ev) {
        if(! ev.getPlayer().level.isClientSide) {
            PlayerUtil.getOptManager(ev.getPlayer()).ifPresent(PlayerDataManager::syncToClient);
        }
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent ev) {
        if(! ev.getPlayer().level.isClientSide) {
            PlayerUtil.getOptManager(ev.getPlayer()).ifPresent(PlayerDataManager::syncToClient);
        }
    }

    @SubscribeEvent
    public static void onPlayerInteractSpec(PlayerInteractEvent.EntityInteractSpecific ev) {
    }

}
