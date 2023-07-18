package hungteen.imm.common.menu.furnace;

import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.imm.common.blockentity.ElixirRoomBlockEntity;
import hungteen.imm.common.menu.IMMMenus;
import hungteen.imm.common.recipe.ElixirRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-28 18:18
 **/
public class ElixirRoomMenu extends FunctionalFurnaceMenu<ElixirRoomBlockEntity> {

    public static final int SLOT_ID_OFFSET = 2;
    private int selectedRecipe;

    public ElixirRoomMenu(int id, Inventory inventory, FriendlyByteBuf buffer) {
        this(id, inventory, new SimpleContainerData(3), buffer.readBlockPos());
    }

    public ElixirRoomMenu(int id, Inventory inventory, ContainerData accessData, BlockPos pos) {
        super(id, IMMMenus.ELIXIR_ROOM.get(), inventory, accessData, pos);

        this.addInventories(73, 53, 3, 3, 0, (i, x, y) -> {
            return new SlotItemHandler(this.blockEntity.getItemHandler(), i, x, y) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return ! ElixirRoomMenu.this.started();
                }

                @Override
                public boolean isActive() {
                    return ! ElixirRoomMenu.this.started();
                }
            };
        });

        this.addInventoryAndHotBar(inventory, 19, 140);

        this.addDataSlots(this.accessData);
    }

    @Override
    public boolean clickMenuButton(Player player, int slotId) {
        if(slotId == 1 && this.canStart()){
            if(EntityHelper.isServer(player)){
                this.start();
            }
            return true;
        } else if(slotId - SLOT_ID_OFFSET < this.getAvailableRecipes().size()){
            this.selectedRecipe = slotId - SLOT_ID_OFFSET;
            return true;
        }
        return super.clickMenuButton(player, slotId);
    }

    public ItemStack getResultItem(){
        return this.blockEntity.getResultItem();
    }

    public int getSmeltingTick(){
        return this.accessData.get(1);
    }

    public int getSmeltingCD(){
        return this.blockEntity.getSmeltingCD();
    }

    public void start(){
        this.blockEntity.onStart(this.player.level());
    }

    public boolean started(){
        return this.blockEntity.started();
    }

    public boolean canStart(){
        return this.blockEntity.canStart() && this.blockEntity.hasIngredient();
    }

    public List<ElixirRecipe> getAvailableRecipes(){
        return this.blockEntity.getAvailableRecipes(this.player.level());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotId) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotId);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            result = itemstack1.copy();
            if (slotId < 9) {
                if (!this.moveItemStackTo(itemstack1, 9, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotId < 9 + 27) {
                if (!this.moveItemStackTo(itemstack1, 0, 9, false)) {
                    return ItemStack.EMPTY;
                }
                if (!this.moveItemStackTo(itemstack1, 9 + 27, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.moveItemStackTo(itemstack1, 0, 9, false)) {
                    return ItemStack.EMPTY;
                }
                if (!this.moveItemStackTo(itemstack1, 9, 9 + 27, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            }

            slot.setChanged();
            if (itemstack1.getCount() == result.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
            this.broadcastChanges();
        }

        return result;
    }

}
