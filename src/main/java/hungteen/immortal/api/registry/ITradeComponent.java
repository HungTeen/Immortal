package hungteen.immortal.api.registry;

import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/18 12:35
 */
public interface ITradeComponent {

    List<ItemStack> getCosts();

    void deal(Entity solder, Entity customer);

    boolean isValid(Container container);

    default boolean match(List<ItemStack> items){
        return false;
    }

    /**
     * Get the type of trade.
     * @return Trade type.
     */
    ITradeType<?> getType();
}
