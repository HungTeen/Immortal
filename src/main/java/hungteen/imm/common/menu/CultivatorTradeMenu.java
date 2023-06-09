package hungteen.imm.common.menu;

import hungteen.htlib.common.menu.HTContainerMenu;
import hungteen.imm.common.entity.human.HumanEntity;
import hungteen.imm.common.entity.human.setting.trade.TradeOffer;
import hungteen.imm.common.entity.human.setting.trade.TradeOffers;
import hungteen.imm.common.impl.codec.HumanSettings;
import hungteen.imm.common.menu.container.TradeContainer;
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
public class CultivatorTradeMenu extends HTContainerMenu {

    protected static final int COST_SIZE = 2;
    protected static final int RESULT_SIZE = 1;

    private final Player player;
    private final HumanEntity merchant;
    private final TradeContainer tradeContainer;

    public CultivatorTradeMenu(int id, Inventory playerInv, FriendlyByteBuf extraData){
        this(id, playerInv, extraData.readInt());
    }

    public CultivatorTradeMenu(int id, Inventory playerInv, int entityId) {
        super(id, IMMMenus.CULTIVATOR_TRADE.get());
        this.player = playerInv.player;
        if(this.player.level.getEntity(entityId) instanceof HumanEntity humanEntity){
            this.merchant = humanEntity;
        } else throw new RuntimeException("No merchant found !");
        this.tradeContainer = new TradeContainer(this, this.merchant, COST_SIZE, RESULT_SIZE);

        this.addSlot(new Slot(this.tradeContainer, 0, 136, 37));
        this.addSlot(new Slot(this.tradeContainer, 1, 162, 37));
        this.addSlot(new Slot(this.tradeContainer, 2, 220, 37));

        this.addInventoryAndHotBar(playerInv, 108, 84);
    }

    public void tryMoveItems(int pos) {
        if (pos >= 0 && pos < this.getTrades().size()) {
            // 清空钱的槽
            for(int i = 0; i < this.tradeContainer.getContainerSize(); ++ i){
                if(this.tradeContainer.isCostSlot(i) && ! this.tradeContainer.getItem(i).isEmpty()){
                    ItemStack itemstack = this.tradeContainer.getItem(i);
                    if (!this.moveItemStackTo(itemstack, 3, 39, true)) {
                        return;
                    }
                    this.tradeContainer.setItem(i, itemstack);
                }
            }

            // 自动填补钱的槽位
            if (this.tradeContainer.isCostSlotEmpty()) {
                List<ItemStack> stacks = this.getTrades().get(pos).costItems();
                for(int i = 0; i < stacks.size(); ++ i){
                    this.moveFromInventoryToPaymentSlot(0, stacks.get(i));
                }
            }

        }
    }

    private void moveFromInventoryToPaymentSlot(int p_40061_, ItemStack p_40062_) {
        if (!p_40062_.isEmpty()) {
            for(int i = 3; i < 39; ++i) {
                ItemStack itemstack = this.slots.get(i).getItem();
                if (!itemstack.isEmpty() && ItemStack.isSameItemSameTags(p_40062_, itemstack)) {
                    ItemStack itemstack1 = this.tradeContainer.getItem(p_40061_);
                    int j = itemstack1.isEmpty() ? 0 : itemstack1.getCount();
                    int k = Math.min(p_40062_.getMaxStackSize() - j, itemstack.getCount());
                    ItemStack itemstack2 = itemstack.copy();
                    int l = j + k;
                    itemstack.shrink(k);
                    itemstack2.setCount(l);
                    this.tradeContainer.setItem(p_40061_, itemstack2);
                    if (l >= p_40062_.getMaxStackSize()) {
                        break;
                    }
                }
            }
        }

    }

    @Override
    public void slotsChanged(Container container) {
        this.tradeContainer.updateSellItem();
        super.slotsChanged(container);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.merchant.setTradingPlayer(null);
        if (! this.merchant.level.isClientSide) {
            if (! player.isAlive() || player instanceof ServerPlayer serverPlayer && serverPlayer.hasDisconnected()) {
                ItemStack itemstack = this.tradeContainer.removeItemNoUpdate(0);
                if (!itemstack.isEmpty()) {
                    player.drop(itemstack, false);
                }

                itemstack = this.tradeContainer.removeItemNoUpdate(1);
                if (!itemstack.isEmpty()) {
                    player.drop(itemstack, false);
                }
            } else if (player instanceof ServerPlayer) {
                player.getInventory().placeItemBackInInventory(this.tradeContainer.removeItemNoUpdate(0));
                player.getInventory().placeItemBackInInventory(this.tradeContainer.removeItemNoUpdate(1));
            }

        }
    }

    public TradeOffers getTrades() {
        return this.merchant.getTradeOffers();
    }

    @Override
    public boolean stillValid(Player player) {
        return this.merchant.getTradingPlayer() == player;
    }
}
