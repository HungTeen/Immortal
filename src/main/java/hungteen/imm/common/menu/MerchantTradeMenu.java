package hungteen.imm.common.menu;

import hungteen.htlib.common.menu.HTContainerMenu;
import hungteen.imm.common.entity.human.HumanEntity;
import hungteen.imm.common.entity.human.setting.trade.TradeOffers;
import hungteen.imm.common.menu.container.TradeContainer;
import hungteen.imm.common.menu.slot.TradeResultSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/18 14:01
 */
public class MerchantTradeMenu extends HTContainerMenu {

    protected static final int COST_SIZE = 2;
    protected static final int RESULT_SIZE = 1;
    private final Player player;
    private final HumanEntity merchant;
    private final TradeContainer tradeContainer;

    public MerchantTradeMenu(int id, Inventory playerInv, FriendlyByteBuf extraData){
        this(id, playerInv, extraData.readInt());
    }

    public MerchantTradeMenu(int id, Inventory playerInv, int entityId) {
        super(id, IMMMenus.CULTIVATOR_TRADE.get());
        this.player = playerInv.player;
        if(this.player.level.getEntity(entityId) instanceof HumanEntity humanEntity){
            this.merchant = humanEntity;
        } else throw new RuntimeException("No merchant found !");
        this.tradeContainer = new TradeContainer(this, this.merchant, COST_SIZE, RESULT_SIZE);

        this.addSlot(new Slot(this.tradeContainer, 0, 136, 37));
        this.addSlot(new Slot(this.tradeContainer, 1, 162, 37));
        this.addSlot(new TradeResultSlot(this.tradeContainer, playerInv.player, this.merchant, 2, 220, 37));

        this.addInventoryAndHotBar(playerInv, 108, 84);
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
        this.merchant.setTradingPlayer(null);
        if (! this.merchant.level.isClientSide) {
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
        return this.merchant.getTradeOffers();
    }

    @Override
    public boolean stillValid(Player player) {
        return this.merchant.getTradingPlayer() == player;
    }
}
