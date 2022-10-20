package hungteen.immortal.api.registry;

import hungteen.htlib.interfaces.IComponentEntry;
import net.minecraft.world.item.Rarity;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-16 12:19
 **/
public interface IElixirRank extends IComponentEntry {

    /**
     * 稀有度。
     */
    Rarity getRarity();
}
