package hungteen.immortal.common.menu;

import hungteen.htlib.menu.HTContainerMenu;
import hungteen.immortal.common.block.ImmortalBlocks;
import hungteen.immortal.common.blockentity.SpiritualStoveBlockEntity;
import hungteen.immortal.common.tag.ImmortalItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-09 10:48
 **/
public class SpiritualStoveMenu extends HTContainerMenu {

    private final Container container;
    private final ContainerData accessData;

    public SpiritualStoveMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainer(4), new SimpleContainerData(1));
    }

    public SpiritualStoveMenu(int id, Inventory inventory, Container container, ContainerData accessData) {
        super(id, ImmortalMenus.SPIRITUAL_STOVE.get());
        this.container = container;
        this.accessData = accessData;

        this.addInventories(this.container, 91, 25, 1, 1, 0);

        for(int i = 0; i < 3; ++ i){
            this.addSlot(new Slot(this.container, 1 + i, 73 + 18 * i, 62){
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.is(ImmortalItemTags.SPIRITUAL_STONES);
                }
            });
        }

        this.addInventoryAndHotBar(inventory, 19, 108);
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

}
