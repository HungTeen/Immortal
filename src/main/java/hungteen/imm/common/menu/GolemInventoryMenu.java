package hungteen.imm.common.menu;

import hungteen.htlib.common.menu.HTContainerMenu;
import hungteen.imm.common.entity.golem.GolemEntity;
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
        final Entity entity=  inventory.player.level.getEntity(golemId);
        assert entity instanceof GolemEntity;
        this.golem = (GolemEntity) entity;
        this.runeContainer = this.golem.getRuneInventory();
//        this.itemContainer = this.golem.getItemInventory();
        this.runeContainer.startOpen(inventory.player);

        this.runeRows = (this.runeContainer.getContainerSize() + 8) / 9;
        this.addInventories(15, TOP_HEIGHT + 1, this.runeRows, 9, 0, (i, x, y) -> {
            return new Slot(this.runeContainer, i, x, y){
                @Override
                public boolean isActive() {
                    //多余的槽位无效。
                    return i < GolemInventoryMenu.this.runeContainer.getContainerSize();
                }
            };
        });

        this.addInventoryAndHotBar(inventory, 8, 84);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        runeContainer.stopOpen(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotId) {
        return ItemStack.EMPTY;
    }

    public int getRuneRows() {
        return runeRows;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.golem.getInteractPlayer() == player;
    }

}
