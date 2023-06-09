package hungteen.imm.common.entity.human.setting.trade;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-06-08 23:14
 * @param costItems 此交易需要花费的物品，请不要让costItem之间有相同物品，判断容易出bug！
 * @param resultItems 此交易购买得到的物品。
 * @param tradeCount 此交易可以进行多少次。
 **/
public record TradeEntry(List<ItemStack> costItems, List<ItemStack> resultItems, IntProvider tradeCount) {
    public static final Codec<TradeEntry> CODEC = RecordCodecBuilder.<TradeEntry>mapCodec(instance -> instance.group(
            ItemStack.CODEC.listOf().fieldOf("cost_items").forGetter(TradeEntry::costItems),
            ItemStack.CODEC.listOf().fieldOf("result_items").forGetter(TradeEntry::resultItems),
            IntProvider.NON_NEGATIVE_CODEC.fieldOf("trade_count").forGetter(TradeEntry::tradeCount)
    ).apply(instance, TradeEntry::new)).codec();

    /**
     * 随机获取最大交易次数。
     */
    public int sampleTradeCount(RandomSource randomSource){
        return tradeCount().sample(randomSource);
    }

    /**
     * 交易项是否匹配。
     * @param payItems 付款。
     */
    public boolean match(List<ItemStack> payItems){
        if(this.costItems().size() > payItems.size()) return false;
        for(int i = 0; i < Math.min(payItems.size(), this.costItems().size()); i++){
            if(! this.isRequiredItem(payItems.get(i), this.costItems().get(i)) || payItems.get(i).getCount() < this.costItems().get(i).getCount()) return false;
        }
        return true;
    }

    /**
     * 两个物品之间是否匹配。
     * @param payItem 当前的物品。
     * @param costItem 至少需要的物品。
     * @return 是否匹配。
     */
    private boolean isRequiredItem(ItemStack payItem, ItemStack costItem) {
        if (costItem.isEmpty() && payItem.isEmpty()) {
            return true;
        } else {
            ItemStack itemstack = payItem.copy();
            if (itemstack.getItem().isDamageable(itemstack)) {
                itemstack.setDamageValue(itemstack.getDamageValue());
            }

            return ItemStack.isSame(itemstack, costItem) && (!costItem.hasTag() || itemstack.hasTag() && NbtUtils.compareNbt(costItem.getTag(), itemstack.getTag(), false));
        }
    }
    
}
