package hungteen.imm.common.menu;

import hungteen.htlib.common.menu.HTContainerMenu;
import hungteen.imm.client.ClientTrader;
import hungteen.imm.common.entity.human.setting.trade.TradeOffers;
import hungteen.imm.common.menu.container.TradeContainer;
import hungteen.imm.common.menu.slot.TradeResultSlot;
import hungteen.imm.util.interfaces.Trader;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * {@link net.minecraft.world.inventory.MerchantMenu}
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/18 14:01
 */
public class MerchantTradeMenu extends HTContainerMenu {

    public static final int COST_SIZE = 2;
    public static final int RESULT_SIZE = 1;
    private static final int OUT_SLOT_SIZE = COST_SIZE + RESULT_SIZE;
    private final Player player;
    private final Trader trader;
    private final TradeContainer tradeContainer;

    public MerchantTradeMenu(int id, Inventory playerInv){
        this(id, playerInv, new ClientTrader(playerInv.player));
    }

    public MerchantTradeMenu(int id, Inventory playerInv, Trader trader) {
        super(id, IMMMenus.CULTIVATOR_TRADE.get());
        this.player = playerInv.player;
        this.trader = trader;
        this.tradeContainer = new TradeContainer(this, this.trader, COST_SIZE, RESULT_SIZE);

        this.addSlot(new Slot(this.tradeContainer, 0, 146, 37));
        this.addSlot(new Slot(this.tradeContainer, 1, 172, 37));
        this.addSlot(new TradeResultSlot(this.tradeContainer, playerInv.player, this.trader, 2, 230, 37));

        this.addInventoryAndHotBar(playerInv, 118, 84);
    }

    @Override
    public void slotsChanged(Container container) {
        this.tradeContainer.updateSellItem();
        super.slotsChanged(container);
    }

    @Override
    public boolean clickMenuButton(Player player, int slotId) {
        if(this.isValidButtonIndex(slotId)){
            this.setSelectionHint(slotId);
            this.tryMoveItems(slotId);
            return true;
        }
        return super.clickMenuButton(player, slotId);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.trader.setTradingPlayer(null);
        if (! this.trader.isClientSide()) {
            if (! player.isAlive() || player instanceof ServerPlayer serverPlayer && serverPlayer.hasDisconnected()) {
                for(int i = 0; i < this.tradeContainer.getContainerSize(); ++ i){
                    if(this.tradeContainer.isCostSlot(i)){
                        ItemStack itemstack = this.tradeContainer.removeItemNoUpdate(i);
                        if (!itemstack.isEmpty()) {
                            player.drop(itemstack, false);
                        }
                    }
                }
            } else if (player instanceof ServerPlayer) {
                for(int i = 0; i < this.tradeContainer.getContainerSize(); ++ i){
                    if(this.tradeContainer.isCostSlot(i)){
                        player.getInventory().placeItemBackInInventory(this.tradeContainer.removeItemNoUpdate(i));
                    }
                }
            }
        }
    }

    /**
     * 选择交易项后，尝试自动放入payItems.
     * @param pos 代表第几格交易项。
     */
    public void tryMoveItems(int pos) {
        if (pos >= 0 && pos < this.getTrades().size()) {
            // 清空钱的槽。
            for(int i = 0; i < this.tradeContainer.getContainerSize(); ++ i){
                if(this.tradeContainer.isCostSlot(i) && ! this.tradeContainer.getItem(i).isEmpty()){
                    ItemStack itemstack = this.tradeContainer.getItem(i);
                    if (!this.moveItemStackTo(itemstack, 3, 39, true)) {
                        return;
                    }
                    this.tradeContainer.setItem(i, itemstack);
                }
            }

            // 自动填补钱的槽位。
            if (this.tradeContainer.isCostSlotEmpty()) {
                List<ItemStack> stacks = this.getTrades().get(pos).getTradeEntry().costItems();
                for(int i = 0; i < stacks.size(); ++ i){
                    this.moveFromInventoryToPaymentSlot(i, stacks.get(i));
                }
            }
        }
    }

    /**
     * 将所需的物品移动到钱的槽位。
     * @param pos 移动到第几个槽位。
     * @param requireStack 需要凑多少钱。
     */
    private void moveFromInventoryToPaymentSlot(int pos, ItemStack requireStack) {
        if (!requireStack.isEmpty()) {
            for(int i = 3; i < 39; ++i) {
                ItemStack curStack = this.slots.get(i).getItem();
                if (!curStack.isEmpty() && ItemStack.isSameItemSameTags(requireStack, curStack)) {
                    ItemStack oldStack = this.tradeContainer.getItem(pos);
                    int oldCnt = oldStack.isEmpty() ? 0 : oldStack.getCount();
                    int cnt = Math.min(requireStack.getMaxStackSize() - oldCnt, curStack.getCount());
                    ItemStack newStack = curStack.copy();
                    int newCnt = oldCnt + cnt;
                    curStack.shrink(cnt);
                    newStack.setCount(newCnt);
                    this.tradeContainer.setItem(pos, newStack);
                    if (newCnt >= requireStack.getMaxStackSize()) {
                        break;
                    }
                }
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotId) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotId);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (slotId == COST_SIZE) {
                if (!this.moveItemStackTo(itemstack1, OUT_SLOT_SIZE, OUT_SLOT_SIZE + 36, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
                this.playTradeSound();
            } else if (slotId > OUT_SLOT_SIZE) {
                if (slotId < OUT_SLOT_SIZE + 27) {
                    if (!this.moveItemStackTo(itemstack1, OUT_SLOT_SIZE + 27, this.slots.size(), false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if(!this.moveItemStackTo(itemstack1, OUT_SLOT_SIZE, OUT_SLOT_SIZE + 27, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(itemstack1, OUT_SLOT_SIZE, this.slots.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    private void playTradeSound() {
        if (! this.trader.isClientSide() && this.trader instanceof Entity entity) {
            entity.level().playLocalSound(entity.getX(), entity.getY(), entity.getZ(), this.trader.getNotifyTradeSound(), SoundSource.NEUTRAL, 1.0F, 1.0F, false);
        }
    }

    public void setSelectionHint(int pos) {
        this.tradeContainer.setSelectionHint(pos);
    }

    public boolean isValidButtonIndex(int pos) {
        return pos >= 0 && pos < this.getTrades().size();
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return false;
    }

    public TradeOffers getTrades() {
        return this.trader.getTradeOffers();
    }

    public Trader getTrader() {
        return trader;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.trader.getTradingPlayer() == player;
    }
}
