package hungteen.immortal.common.event;

import hungteen.immortal.ImmortalMod;
import hungteen.immortal.common.event.handler.PlayerEventHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-25 16:32
 **/
@Mod.EventBusSubscriber(modid = ImmortalMod.MOD_ID)
public class ImmortalLivingEvents {

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent ev) {
        /* handle player death */
        if(ev.getEntity() instanceof Player) {
            PlayerEventHandler.handlePlayerDeath(ev, (Player) ev.getEntity());
        }
    }

}
