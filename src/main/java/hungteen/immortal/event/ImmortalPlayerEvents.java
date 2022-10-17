package hungteen.immortal.event;

import hungteen.immortal.ImmortalMod;
import hungteen.immortal.SpellManager;
import hungteen.immortal.api.events.PlayerSpellEvent;
import hungteen.immortal.capability.player.PlayerDataManager;
import hungteen.immortal.event.handler.PlayerEventHandler;
import hungteen.immortal.impl.Spells;
import hungteen.immortal.network.EmptyClickPacket;
import hungteen.immortal.network.NetworkHandler;
import hungteen.immortal.utils.PlayerUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.system.CallbackI;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-25 15:52
 **/
@Mod.EventBusSubscriber(modid = ImmortalMod.MOD_ID)
public class ImmortalPlayerEvents {

    @SubscribeEvent
    public static void tickPlayer(TickEvent.PlayerTickEvent ev) {
        if(! ev.player.level.isClientSide){
            PlayerUtil.tick(ev.player);
        }
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
    public static void onPlayerActivateSpell(PlayerSpellEvent.ActivateSpellEvent.Post ev) {
        SpellManager.checkSpellAction(ev.getPlayer(), ev.getSpell(), ev.getLevel());
    }

    @SubscribeEvent
    public static void onPlayerInteractSpec(PlayerInteractEvent.EntityInteractSpecific ev) {
        if(! ev.getPlayer().level.isClientSide) {
            PlayerEventHandler.rayTrace(ev.getPlayer());
        }
    }

    @SubscribeEvent
    public static void onPlayerRightClickItem(PlayerInteractEvent.RightClickItem ev) {
        if(! ev.getPlayer().level.isClientSide) {
            PlayerEventHandler.rayTrace(ev.getPlayer());
        }
    }

    @SubscribeEvent
    public static void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock ev) {
        if(! ev.getPlayer().level.isClientSide) {
            PlayerEventHandler.rayTrace(ev.getPlayer());
        }
    }

    /**
     * Only Client side !
     */
    @SubscribeEvent
    public static void onPlayerRightClickEmpty(PlayerInteractEvent.RightClickEmpty ev) {
        NetworkHandler.sendToServer(new EmptyClickPacket());
    }

    @SubscribeEvent
    public static void onPlayerTossItem(ItemTossEvent ev) {
        if(! ev.getPlayer().level.isClientSide){
            PlayerEventHandler.onTossItem(ev.getPlayer(), ev.getEntityItem());
        }
    }

}
