package hungteen.immortal.common.menu.container;

import hungteen.immortal.api.registry.ITradeComponent;
import hungteen.immortal.common.entity.human.HumanEntity;
import hungteen.immortal.common.menu.CultivatorTradeMenu;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/18 15:35
 */
public class TradeContainer implements Container {

    private final CultivatorTradeMenu menu;
    private final HumanEntity trader;
    private final NonNullList<ItemStack> itemStacks;
    private final int costSize;
    private final int resultSize;
    @Nullable
    private ITradeComponent activeOffer;
    private int selectionHint;

    public TradeContainer(CultivatorTradeMenu menu, HumanEntity trader, int costSize, int resultSize) {
        this.menu = menu;
        this.trader = trader;
        this.costSize = costSize;
        this.resultSize = resultSize;
        this.itemStacks = NonNullList.withSize(costSize + resultSize, ItemStack.EMPTY);
    }

    @Override
    public int getContainerSize() {
        return this.itemStacks.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack itemstack : this.itemStacks) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int pos) {
        return this.itemStacks.get(pos);
    }

    @Override
    public ItemStack removeItem(int pos, int count) {
        ItemStack itemstack = this.itemStacks.get(pos);
        // 售卖品直接全部拿走，钱该拿多少拿多少。
        if (this.isResultSlot(pos) && !itemstack.isEmpty()) {
            return ContainerHelper.removeItem(this.itemStacks, pos, itemstack.getCount());
        } else {
            ItemStack stack = ContainerHelper.removeItem(this.itemStacks, pos, count);
            if (!stack.isEmpty() && this.isCostSlot(pos)) {
                this.updateSellItem();
            }

            return stack;
        }
    }

    @Override
    public ItemStack removeItemNoUpdate(int pos) {
        return ContainerHelper.takeItem(this.itemStacks, pos);
    }

    @Override
    public void setItem(int pos, ItemStack itemStack) {
        this.itemStacks.set(pos, itemStack);
        if (!itemStack.isEmpty() && itemStack.getCount() > this.getMaxStackSize()) {
            itemStack.setCount(this.getMaxStackSize());
        }

        if (this.isCostSlot(pos)) {
            this.updateSellItem();
        }

    }

    @Override
    public void setChanged() {
        this.updateSellItem();
    }

    @Override
    public boolean stillValid(Player player) {
        return this.trader.getTradingPlayer() == player;
    }

    @Override
    public void clearContent() {
        this.itemStacks.clear();
    }

    public void updateSellItem() {
        this.activeOffer = null;
        // 没放钱
        if (this.isCostSlotEmpty()) {
            this.clearResultSlots();
        } else {
            List<ITradeComponent> trades = this.menu.getTrades();
            if (! trades.isEmpty()) {
                ITradeComponent merchantoffer = match(trades, this.getCosts(), this.selectionHint);

                if (merchantoffer != null) {
                    this.activeOffer = merchantoffer;
                    this.activeOffer.deal(this.trader, this.trader.getTradingPlayer());
//                    this.setItem(2, merchantoffer.assemble());
                } else {
                    this.clearResultSlots();
                }
            }
//            this.merchant.notifyTradeUpdated(this.getItem(2));
        }
    }

    @Nullable
    public static ITradeComponent match(List<ITradeComponent> trades, List<ItemStack> items, int pos){
        if (pos >= 0 && pos < trades.size() && trades.get(pos).match(items)) {
            return trades.get(pos);
        }
        for (ITradeComponent trade : trades) {
            if (trade.match(items)) {
                return trade;
            }
        }
        return null;
    }

    private List<ItemStack> getCosts(){
        return this.itemStacks.subList(0, this.costSize);
    }

    public boolean isCostSlotEmpty(){
        for(int i = 0; i < this.costSize; ++ i){
            if(! this.getItem(i).isEmpty()) return false;
        }
        return true;
    }

    public void clearResultSlots(){
        for(int i = this.costSize; i < this.getContainerSize(); ++ i){
            this.setItem(i, ItemStack.EMPTY);
        }
    }

    public int getCostSize() {
        return costSize;
    }

    public int getResultSize() {
        return resultSize;
    }

    public boolean isCostSlot(int pos){
        return pos < this.costSize;
    }

    public boolean isResultSlot(int pos){
        return pos >= this.costSize;
    }
}
