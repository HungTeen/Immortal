package hungteen.imm.common.menu.container;

import hungteen.imm.common.entity.human.HumanEntity;
import hungteen.imm.common.entity.human.setting.trade.TradeOffer;
import hungteen.imm.common.entity.human.setting.trade.TradeOffers;
import hungteen.imm.common.menu.MerchantTradeMenu;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/18 15:35
 */
public class TradeContainer implements Container {

    private final MerchantTradeMenu menu;
    private final HumanEntity merchant;
    private final NonNullList<ItemStack> costStacks;
    private final NonNullList<ItemStack> resultStacks;
    @Nullable
    private TradeOffer activeOffer;
    private int selectionHint;

    public TradeContainer(MerchantTradeMenu menu, HumanEntity trader, int costSize, int resultSize) {
        this.menu = menu;
        this.merchant = trader;
        this.costStacks = NonNullList.withSize(costSize, ItemStack.EMPTY);
        this.resultStacks = NonNullList.withSize(resultSize, ItemStack.EMPTY);
    }

    @Override
    public int getContainerSize() {
        return this.getCostSize() + this.getResultSize();
    }

    @Override
    public boolean isEmpty() {
        return this.isCostSlotEmpty() && this.isResultSlotEmpty();
    }

    @Override
    public ItemStack getItem(int pos) {
        return this.isResultSlot(pos) ? this.resultStacks.get(pos - this.getCostSize()) : this.costStacks.get(pos);
    }

    @Override
    public ItemStack removeItem(int pos, int count) {
        ItemStack itemstack = this.getItem(pos);
        // 售卖品直接全部拿走，钱该拿多少拿多少。
        if (this.isResultSlot(pos) && !itemstack.isEmpty()) {
            return ContainerHelper.removeItem(this.resultStacks, pos - this.getCostSize(), itemstack.getCount());
        } else {
            ItemStack stack = ContainerHelper.removeItem(this.costStacks, pos, count);
            if (!stack.isEmpty() && this.isCostSlot(pos)) {
                this.updateSellItem();
            }

            return stack;
        }
    }

    @Override
    public ItemStack removeItemNoUpdate(int pos) {
        return this.isResultSlot(pos) ? ContainerHelper.takeItem(this.resultStacks, pos - this.getCostSize()) : ContainerHelper.takeItem(this.costStacks, pos);
    }

    @Override
    public void setItem(int pos, ItemStack itemStack) {
        if(this.isResultSlot(pos)){
            this.resultStacks.set(pos - this.getCostSize(), itemStack);
        } else {
            this.costStacks.set(pos, itemStack);
        }

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
        return this.merchant.getTradingPlayer() == player;
    }

    @Override
    public void clearContent() {
        this.costStacks.clear();
        this.resultStacks.clear();
    }

    public void updateSellItem() {
        this.activeOffer = null;
        // 没放钱。
        if (this.isCostSlotEmpty()) {
            this.clearResultSlots();
        } else {
            final TradeOffers offers = this.menu.getTrades();
            if (! offers.isEmpty()) {
                final TradeOffer offer = match(offers, this.getCostStacks(), this.selectionHint);
                if (offer != null && offer.valid()) {
                    this.activeOffer = offer;
                    this.fillResultSlots(this.activeOffer);
                } else {
                    this.clearResultSlots();
                }
            }
//            this.merchant.notifyTradeUpdated(this.getItem(2));
        }
    }

    @Nullable
    public static TradeOffer match(TradeOffers trades, List<ItemStack> items, int pos){
        if (pos >= 0 && pos < trades.size() && trades.get(pos).match(items)) {
            return trades.get(pos);
        }
        for (TradeOffer trade : trades) {
            if (trade.match(items)) {
                return trade;
            }
        }
        return null;
    }

    public NonNullList<ItemStack> getCostStacks(){
        return this.costStacks;
    }

    public NonNullList<ItemStack> getResultStacks(){
        return this.resultStacks;
    }

    public boolean isCostSlotEmpty(){
        for(int i = 0; i < this.getCostSize(); ++ i){
            if(! this.getItem(i).isEmpty()) return false;
        }
        return true;
    }

    public boolean isResultSlotEmpty(){
        for(int i = this.getCostSize(); i < this.getContainerSize(); ++ i){
            if(! this.getItem(i).isEmpty()) return false;
        }
        return true;
    }

    public void clearResultSlots(){
        for(int i = this.getCostSize(); i < this.getContainerSize(); ++ i){
            this.setItem(i, ItemStack.EMPTY);
        }
    }

    public void fillResultSlots(TradeOffer result){
        for(int i = 0; i < Math.min(this.getResultSize(), result.getTradeEntry().resultItems().size()); ++ i){
            this.setItem(i + this.getCostSize(), result.getTradeEntry().resultItems().get(i).copy());
        }
    }

    public void setSelectionHint(int pos) {
        this.selectionHint = pos;
        this.updateSellItem();
    }

    public int getCostSize() {
        return this.costStacks.size();
    }

    public int getResultSize() {
        return this.resultStacks.size();
    }

    public boolean isCostSlot(int pos){
        return pos < this.getCostSize();
    }

    public boolean isResultSlot(int pos){
        return ! isCostSlot(pos);
    }

    public TradeOffer getActiveOffer() {
        return this.activeOffer;
    }

}
