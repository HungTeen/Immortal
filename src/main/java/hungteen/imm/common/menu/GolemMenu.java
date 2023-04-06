package hungteen.imm.common.menu;

import hungteen.htlib.common.menu.HTContainerMenu;
import hungteen.imm.common.entity.golem.GolemEntity;
import hungteen.imm.util.EntityUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-24 11:19
 **/
public class GolemMenu extends HTContainerMenu {

    private final Container runeContainer;
    private final Container itemContainer;
    private final GolemEntity golem;

    public GolemMenu(int id, Inventory inventory, FriendlyByteBuf buf) {
        this(id, inventory, buf.readInt());
    }

    public GolemMenu(int id, Inventory inventory, int golemId) {
        super(id, IMMMenus.GOLEM_INVENTORY.get());
        final Entity entity=  inventory.player.level.getEntity(golemId);
        assert entity instanceof GolemEntity;
        this.golem = (GolemEntity) entity;
        this.runeContainer = this.golem.getRuneInventory();
        this.itemContainer = this.golem.getItemInventory();
        this.runeContainer.startOpen(inventory.player);

        this.addInventories(this.runeContainer, 8, 18, 3, 9, 0);

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

    @Override
    public boolean stillValid(Player player) {
        return EntityUtil.isEntityValid(this.golem);
    }

}
