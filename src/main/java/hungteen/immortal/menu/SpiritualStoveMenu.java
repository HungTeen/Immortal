package hungteen.immortal.menu;

import hungteen.htlib.menu.HTContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-09 10:48
 **/
public class SpiritualStoveMenu extends HTContainerMenu {

    private final ContainerLevelAccess access;

    public SpiritualStoveMenu(int id, Inventory inventory) {
        this(id, inventory, ContainerLevelAccess.NULL);
    }

    public SpiritualStoveMenu(int id, Inventory inventory, ContainerLevelAccess access) {
        super(id, ImmortalMenus.SPIRITUAL_STOVE.get());
        this.access = access;
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }

}
