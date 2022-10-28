package hungteen.immortal.common.menu;

import hungteen.htlib.menu.HTContainerMenu;
import hungteen.immortal.common.tag.ImmortalItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-28 18:18
 **/
public class ElixirFurnaceMenu extends HTContainerMenu {

    private final Container container;
    private final ContainerData accessData;

    public ElixirFurnaceMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainer(4), new SimpleContainerData(1));
    }

    public ElixirFurnaceMenu(int id, Inventory inventory, Container container, ContainerData accessData) {
        super(id, ImmortalMenus.ELIXIR_FURNACE.get());
        this.container = container;
        this.accessData = accessData;

        this.addInventories(this.container, 73, 53, 3, 3, 0);

        this.addInventoryAndHotBar(inventory, 19, 140);
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

}
