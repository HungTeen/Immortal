package hungteen.immortal.common.impl.codec.trades;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.immortal.api.registry.ITradeType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/18 13:07
 */
public class ItemTradeComponent extends TradeComponent {

    public static final Codec<ItemTradeComponent> CODEC = RecordCodecBuilder.<ItemTradeComponent>mapCodec(instance -> instance.group(
            ItemStack.CODEC.listOf().fieldOf("cost_items").forGetter(ItemTradeComponent::getCostItems),
            ItemStack.CODEC.listOf().fieldOf("result_items").forGetter(ItemTradeComponent::getResultItems)
    ).apply(instance, ItemTradeComponent::new)).codec();

    private final List<ItemStack> resultItems;

    public ItemTradeComponent(List<ItemStack> costItems, List<ItemStack> resultItems) {
        super(costItems);
        this.resultItems = resultItems;
    }

    @Override
    public void deal(Entity solder, Entity customer) {

    }

    public List<ItemStack> getResultItems() {
        return resultItems;
    }

    @Override
    public ITradeType<?> getType() {
        return TradeComponents.ITEM_TRADE;
    }
}
