package hungteen.immortal.api.interfaces;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-16 12:18
 **/
public interface IElixirItem {

    ItemStack getElixirItem();

    /**
     * use {@link hungteen.immortal.api.enums.ElixirRarity}.
     */
    Rarity getElixirRarity();

}
