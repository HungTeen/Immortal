package hungteen.immortal.common.event;

import hungteen.immortal.Immortal;
import hungteen.immortal.common.event.handler.PlayerEventHandler;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-25 16:32
 **/
@Mod.EventBusSubscriber(modid = Immortal.MOD_ID)
public class ImmortalLivingEvents {

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent ev) {
        /* handle player death */
        if(ev.getEntity() instanceof Player) {
            PlayerEventHandler.handlePlayerDeath(ev, (Player) ev.getEntity());
        }
    }

}
