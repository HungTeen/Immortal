package hungteen.immortal.item.rune;

import hungteen.immortal.api.interfaces.IGetterRune;
import hungteen.immortal.item.ItemTabs;
import net.minecraft.world.item.Item;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-06 18:27
 **/
public class RuneItem extends Item {

    public RuneItem() {
        super(new Properties().tab(ItemTabs.RUNE));
    }
}
