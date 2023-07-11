package hungteen.imm.common.menu.slot;

import hungteen.imm.common.entity.human.HumanEntity;
import hungteen.imm.common.entity.human.setting.trade.TradeOffer;
import hungteen.imm.common.menu.container.TradeContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/6/10 11:08
 */
public class TradeResultSlot extends Slot {

    private final TradeContainer tradeContainer;
    private final Player player;
    private final HumanEntity merchant;
    private int removeCount;

    public TradeResultSlot(TradeContainer tradeContainer, Player player, HumanEntity human, int slotId, int x, int y) {
        super(tradeContainer, slotId, x, y);
        this.player = player;
        this.merchant = human;
        this.tradeContainer = tradeContainer;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack remove(int cnt) {
        if (this.hasItem()) {
            this.removeCount += Math.min(cnt, this.getItem().getCount());
        }

        return super.remove(cnt);
    }

    @Override
    protected void onQuickCraft(ItemStack stack, int cnt) {
        this.removeCount += cnt;
        this.checkTakeAchievements(stack);
    }

    @Override
    protected void checkTakeAchievements(ItemStack stack) {
        stack.onCraftedBy(this.player.level(), this.player, this.removeCount);
        this.removeCount = 0;
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
        this.checkTakeAchievements(stack);
        TradeOffer activeOffer = this.tradeContainer.getActiveOffer();
        if (activeOffer != null) {
            if (activeOffer.take(tradeContainer.getCostStacks())) {
                this.merchant.notifyTrade(activeOffer);
//                player.awardStat(Stats.TRADED_WITH_VILLAGER);
//                this.tradeContainer.setItem(0, itemstack);
//                this.tradeContainer.setItem(1, itemstack1);
            }
        }
    }

}
