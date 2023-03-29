package hungteen.imm.common.event;

import hungteen.imm.ImmortalMod;
import hungteen.imm.common.event.handler.PlayerEventHandler;
import hungteen.imm.common.item.artifacts.HammerItem;
import hungteen.imm.common.network.EmptyClickPacket;
import hungteen.imm.common.network.NetworkHandler;
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
public class IMMPlayerEvents {

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

//    @SubscribeEvent
//    public static void onPlayerActivateSpell(PlayerSpellEvent.ActivateSpellEvent.Post event) {
//        SpellManager.checkSpellAction(event.getEntity(), event.getSpell(), event.getLevel());
//    }

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
