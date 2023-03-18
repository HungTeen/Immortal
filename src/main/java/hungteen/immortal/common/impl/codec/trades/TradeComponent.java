package hungteen.immortal.common.impl.codec.trades;

import hungteen.immortal.api.registry.ITradeComponent;
import hungteen.immortal.api.registry.ITradeType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/18 13:03
 */
public abstract class TradeComponent implements ITradeComponent {

    private final List<ItemStack> costItems;

    public TradeComponent(List<ItemStack> costItems) {
        this.costItems = costItems;
    }

    public List<ItemStack> getCostItems() {
        return costItems;
    }
}
