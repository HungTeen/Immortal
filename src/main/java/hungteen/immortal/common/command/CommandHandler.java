package hungteen.immortal.common.command;

import hungteen.immortal.Immortal;
import net.minecraftforge.event.RegisterCommandsEvent;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-25 16:37
 **/
public class CommandHandler {

    /**
     * {@link Immortal#Immortal()}
     */
    public static void init(RegisterCommandsEvent event) {
        ImmortalCommand.register(event.getDispatcher());
    }

}
