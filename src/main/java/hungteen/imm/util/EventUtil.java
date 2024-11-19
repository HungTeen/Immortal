package hungteen.imm.util;

import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.common.NeoForge;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/8/19 15:18
 */
public class EventUtil {

    public static boolean post(Event event){
        if(NeoForge.EVENT_BUS.post(event) instanceof ICancellableEvent cancellableEvent){
            return cancellableEvent.isCanceled();
        }
        return false;
    }

}
