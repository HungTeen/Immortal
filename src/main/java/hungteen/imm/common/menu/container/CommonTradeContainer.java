package hungteen.imm.common.menu.container;

import hungteen.imm.common.entity.human.HumanEntity;
import hungteen.imm.common.impl.codec.HumanSettings;
import hungteen.imm.common.menu.CultivatorTradeMenu;
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
public class CommonTradeContainer implements Container {

    private final CultivatorTradeMenu menu;
    private final HumanEntity trader;
    private final NonNullList<ItemStack> itemStacks;
    private final int costSize;
    private final int resultSize;
    @Nullable
    private HumanSettings.CommonTradeEntry activeOffer;
    private int selectionHint;

    public CommonTradeContainer(CultivatorTradeMenu menu, HumanEntity trader, int costSize, int resultSize) {
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
            List<HumanSettings.CommonTradeEntry> trades = this.menu.getTrades();
            if (! trades.isEmpty()) {
                HumanSettings.CommonTradeEntry merchantoffer = match(trades, this.getCosts(), this.selectionHint);

                if (merchantoffer != null) {
                    this.activeOffer = merchantoffer;
                    this.fillResultSlots(this.activeOffer);
                } else {
                    this.clearResultSlots();
                }
            }
//            this.merchant.notifyTradeUpdated(this.getItem(2));
        }
    }

    @Nullable
    public static HumanSettings.CommonTradeEntry match(List<HumanSettings.CommonTradeEntry> trades, List<ItemStack> items, int pos){
        if (pos >= 0 && pos < trades.size() && trades.get(pos).match(items)) {
            return trades.get(pos);
        }
        for (HumanSettings.CommonTradeEntry trade : trades) {
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

    public void fillResultSlots(HumanSettings.CommonTradeEntry result){
        for(int i = 0; i < Math.min(this.getResultSize(), result.resultItems().size()); ++ i){
            this.setItem(i + this.getCostSize(), result.resultItems().get(i).copy());
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
