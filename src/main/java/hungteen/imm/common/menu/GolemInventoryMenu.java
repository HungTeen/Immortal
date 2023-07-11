package hungteen.imm.common.menu;

import hungteen.htlib.common.menu.HTContainerMenu;
import hungteen.imm.common.entity.golem.GolemEntity;
import hungteen.imm.common.item.runes.BehaviorRuneItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-24 11:19
 **/
public class GolemInventoryMenu extends HTContainerMenu {

    private static final int TOP_HEIGHT = 25;
    private final Container runeContainer;
//    private final Container itemContainer;
    private final GolemEntity golem;
    private final int runeRows;

    public GolemInventoryMenu(int id, Inventory inventory, FriendlyByteBuf buf) {
        this(id, inventory, buf.readInt());
    }

    public GolemInventoryMenu(int id, Inventory inventory, int golemId) {
        super(id, IMMMenus.GOLEM_INVENTORY.get());
        final Entity entity=  inventory.player.level().getEntity(golemId);
        assert entity instanceof GolemEntity;
        this.golem = (GolemEntity) entity;
        this.runeContainer = this.golem.getRuneInventory();
//        this.itemContainer = this.golem.getItemInventory();
        this.runeContainer.startOpen(inventory.player);

        this.runeRows = (this.runeContainer.getContainerSize() + 8) / 9;
        this.addInventories(15, TOP_HEIGHT, this.runeRows, 9, 0, (i, x, y) -> {
            return new Slot(this.runeContainer, i, x, y){

                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.getItem() instanceof BehaviorRuneItem;
                }

                @Override
                public boolean isActive() {
                    //多余的槽位无效。
                    return i < GolemInventoryMenu.this.runeContainer.getContainerSize();
                }
            };
        });
        final int height = TOP_HEIGHT + 18 * this.runeRows + 12;
        this.addPlayerInventory(inventory, 15, height);
        this.addPlayerHotBar(inventory, 15, height + 57);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.golem.stopInteracting();
        runeContainer.stopOpen(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int id) {
        ItemStack result = ItemStack.EMPTY;
        final Slot slot = this.slots.get(id);
        if (slot.hasItem()) {
            ItemStack slotItem = slot.getItem();
            result = slotItem.copy();
            if (id < this.runeRows * 9) {
                if (!this.moveItemStackTo(slotItem, this.runeRows * 9, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotItem, 0, this.runeRows * 9, false)) {
                return ItemStack.EMPTY;
            }

            if (slotItem.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return result;
    }

    public int getRuneRows() {
        return runeRows;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.golem.getInteractPlayer() == player;
    }

}
