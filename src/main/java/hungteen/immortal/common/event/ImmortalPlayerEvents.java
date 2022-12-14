package hungteen.immortal.common.event;

import hungteen.immortal.ImmortalMod;
import hungteen.immortal.common.SpellManager;
import hungteen.immortal.api.events.PlayerSpellEvent;
import hungteen.immortal.common.capability.player.PlayerDataManager;
import hungteen.immortal.common.event.handler.PlayerEventHandler;
import hungteen.immortal.common.item.artifacts.HammerItem;
import hungteen.immortal.common.network.EmptyClickPacket;
import hungteen.immortal.common.network.NetworkHandler;
import hungteen.immortal.utils.PlayerUtil;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
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
@Mod.EventBusSubscriber(modid = ImmortalMod.MOD_ID)
public class ImmortalPlayerEvents {

    @SubscribeEvent
    public static void tickPlayer(TickEvent.PlayerTickEvent event) {
        if(! event.player.level.isClientSide){
            PlayerUtil.tick(event.player);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (! event.getEntity().level.isClientSide) {
            PlayerEventHandler.onPlayerLogin(event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (! event.getEntity().level.isClientSide) {
            PlayerEventHandler.onPlayerLogout(event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        PlayerEventHandler.clonePlayerData(event.getOriginal(), event.getEntity(), event.isWasDeath());
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if(! event.getEntity().level.isClientSide) {
            PlayerUtil.getOptManager(event.getEntity()).ifPresent(PlayerDataManager::syncToClient);
        }
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if(! event.getEntity().level.isClientSide) {
            PlayerUtil.getOptManager(event.getEntity()).ifPresent(PlayerDataManager::syncToClient);
        }
    }

    @SubscribeEvent
    public static void onPlayerActivateSpell(PlayerSpellEvent.ActivateSpellEvent.Post event) {
        SpellManager.checkSpellAction(event.getEntity(), event.getSpell(), event.getLevel());
    }

    @SubscribeEvent
    public static void onPlayerInteractSpec(PlayerInteractEvent.EntityInteractSpecific event) {
        if(! event.getEntity().level.isClientSide) {
            PlayerEventHandler.rayTrace(event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onPlayerRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if(! event.getEntity().level.isClientSide) {
            PlayerEventHandler.rayTrace(event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if(! event.getEntity().level.isClientSide) {
            PlayerEventHandler.rayTrace(event.getEntity());
        }
        HammerItem.smithing(event.getEntity(), event.getHand(), event.getFace(), event.getPos());
    }

    /**
     * Only Client side !
     */
    @SubscribeEvent
    public static void onPlayerRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        NetworkHandler.sendToServer(new EmptyClickPacket());
    }

    @SubscribeEvent
    public static void onPlayerTossItem(ItemTossEvent event) {
        if(! event.getPlayer().level.isClientSide){
            PlayerEventHandler.onTossItem(event.getPlayer(), event.getEntity());
        }
    }

}
