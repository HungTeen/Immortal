package hungteen.imm.common.entity.human.setting.trade;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-06-08 23:14
 **/
public record TradeEntry(List<ItemStack> costItems, List<ItemStack> resultItems) {
    public static final Codec<TradeEntry> CODEC = RecordCodecBuilder.<TradeEntry>mapCodec(instance -> instance.group(
            ItemStack.CODEC.listOf().fieldOf("cost_items").forGetter(TradeEntry::costItems),
            ItemStack.CODEC.listOf().fieldOf("result_items").forGetter(TradeEntry::resultItems)
    ).apply(instance, TradeEntry::new)).codec();

    public boolean match(List<ItemStack> items){
        if(this.costItems().size() > items.size()) return false;
        for(int i = 0; i < Math.min(items.size(), this.costItems().size()); i++){
            if(! this.isRequiredItem(items.get(i), this.costItems().get(i)) || items.get(i).getCount() < this.costItems().get(i).getCount()) return false;
        }
        return true;
    }

    private boolean isRequiredItem(ItemStack requireItem, ItemStack item) {
        if (item.isEmpty() && requireItem.isEmpty()) {
            return true;
        } else {
            ItemStack itemstack = requireItem.copy();
            if (itemstack.getItem().isDamageable(itemstack)) {
                itemstack.setDamageValue(itemstack.getDamageValue());
            }

            return ItemStack.isSame(itemstack, item) && (!item.hasTag() || itemstack.hasTag() && NbtUtils.compareNbt(item.getTag(), itemstack.getTag(), false));
        }
    }
    
}
