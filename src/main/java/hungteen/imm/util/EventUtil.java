package hungteen.imm.util;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/8/19 15:18
 */
public class EventUtil {

    public static boolean post(Event event){
        return MinecraftForge.EVENT_BUS.post(event);
    }

}
