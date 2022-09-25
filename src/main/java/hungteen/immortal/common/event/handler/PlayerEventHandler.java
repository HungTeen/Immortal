package hungteen.immortal.common.event.handler;

import hungteen.immortal.common.capability.player.PlayerDataManager;
import hungteen.immortal.common.event.ImmortalLivingEvents;
import hungteen.immortal.common.event.ImmortalPlayerEvents;
import hungteen.immortal.utils.PlayerUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-25 15:54
 **/
public class PlayerEventHandler {

    /**
     * send packet from server to client to sync player's data.
     * {@link ImmortalPlayerEvents#onPlayerLogin(net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent)}
     */
    public static void onPlayerLogin(Player player) {
        PlayerUtil.getOptManager(player).ifPresent(PlayerDataManager::init);

    }

    /**
     * save card cd.
     * {@link ImmortalPlayerEvents#onPlayerLogout(net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent)}
     */
    public static void onPlayerLogout(Player player) {
    }

    /**
     * {@link ImmortalLivingEvents#onLivingDeath(LivingDeathEvent)}
     */
    public static void handlePlayerDeath(LivingDeathEvent ev, Player player) {
    }

    /**
     * {@link ImmortalPlayerEvents#onPlayerClone(net.minecraftforge.event.entity.player.PlayerEvent.Clone)}
     */
    public static void clonePlayerData(Player oldPlayer, Player newPlayer, boolean died) {
        PlayerUtil.getOptManager(oldPlayer).ifPresent(oldOne -> {
            PlayerUtil.getOptManager(newPlayer).ifPresent(newOne -> {
                newOne.cloneFromExistingPlayerData(oldOne, died);
            });
        });
    }

}
