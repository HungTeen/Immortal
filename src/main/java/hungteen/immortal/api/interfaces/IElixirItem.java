package hungteen.immortal.api.interfaces;

import hungteen.immortal.api.registry.IRealm;
import hungteen.immortal.api.registry.ISpiritualRoot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

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
