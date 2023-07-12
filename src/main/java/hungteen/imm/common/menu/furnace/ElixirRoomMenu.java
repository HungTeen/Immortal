package hungteen.imm.common.menu.furnace;

import hungteen.imm.common.blockentity.ElixirRoomBlockEntity;
import hungteen.imm.common.menu.IMMMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-28 18:18
 **/
public class ElixirRoomMenu extends FunctionalFurnaceMenu<ElixirRoomBlockEntity> {

    public ElixirRoomMenu(int id, Inventory inventory, FriendlyByteBuf buffer) {
        this(id, inventory, new SimpleContainerData(3), buffer.readBlockPos());
    }

    public ElixirRoomMenu(int id, Inventory inventory, ContainerData accessData, BlockPos pos) {
        super(id, IMMMenus.ELIXIR_ROOM.get(), inventory, accessData, pos);

        this.addInventories(73, 53, 3, 3, 0, (i, x, y) -> {
            return new SlotItemHandler(this.blockEntity.getItemHandler(), i, x, y) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return ElixirRoomMenu.this.blockEntity.isSmeltStart();
                }
            };
        });

        this.addInventoryAndHotBar(inventory, 19, 140);

        this.addDataSlots(this.accessData);
    }

    public ItemStack getResultItem(){
        return this.blockEntity.getResultItem();
    }

    public int getSmeltingTick(){
        return this.accessData.get(0);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotId) {
        return ItemStack.EMPTY;
    }

}
