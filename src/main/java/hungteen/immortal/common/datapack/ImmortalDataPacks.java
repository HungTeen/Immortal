package hungteen.immortal.common.datapack;

import hungteen.immortal.ImmortalMod;
import net.minecraftforge.event.AddReloadListenerEvent;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-18 20:17
 **/
public class ImmortalDataPacks {

    /**
     * {@link ImmortalMod#ImmortalMod()}
     */
    public static void addDataPack(AddReloadListenerEvent event) {
        event.addListener(new SpellBookManager());
    }

}
