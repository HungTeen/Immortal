package hungteen.immortal.common.event;

import hungteen.immortal.ImmortalMod;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-22 23:04
 **/
@Mod.EventBusSubscriber(modid = ImmortalMod.MOD_ID)
public class ImmortalEntityEvents {

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event){
    }
}
