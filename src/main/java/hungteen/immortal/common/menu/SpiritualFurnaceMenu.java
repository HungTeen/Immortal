package hungteen.immortal.common.menu;

import hungteen.htlib.menu.HTContainerMenu;
import hungteen.immortal.common.block.ImmortalBlocks;
import hungteen.immortal.common.blockentity.ElixirRoomBlockEntity;
import hungteen.immortal.common.blockentity.SpiritualFurnaceBlockEntity;
import hungteen.immortal.common.item.ImmortalItems;
import hungteen.immortal.common.tag.ImmortalItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-09 10:48
 **/
public class SpiritualFurnaceMenu extends HTContainerMenu {

    private final SpiritualFurnaceBlockEntity blockEntity;
    private final ContainerData accessData;
    private final ContainerLevelAccess accessLevel;
    private final Player player;

    public SpiritualFurnaceMenu(int id, Inventory inventory, FriendlyByteBuf buffer) {
        this(id, inventory, new SimpleContainerData(3), buffer.readBlockPos());
    }

    public SpiritualFurnaceMenu(int id, Inventory inventory, ContainerData accessData, BlockPos pos) {
        super(id, ImmortalMenus.SPIRITUAL_FURNACE.get());
        this.accessData = accessData;
        this.player = inventory.player;
        this.accessLevel = ContainerLevelAccess.create(inventory.player.level, pos);
        BlockEntity blockEntity = inventory.player.level.getBlockEntity(pos);
        if(blockEntity instanceof SpiritualFurnaceBlockEntity){
            this.blockEntity = (SpiritualFurnaceBlockEntity) blockEntity;
        } else{
            throw new RuntimeException("Invalid block entity !");
        }

        this.addSlot(new SlotItemHandler(this.blockEntity.getItemHandler(), 0, 91, 25){
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(ImmortalItems.FLAME_GOURD.get());
            }
        });

        for(int i = 0; i < 3; ++ i){
            this.addSlot(new SlotItemHandler(this.blockEntity.getItemHandler(), 1 + i, 73 + 18 * i, 62){
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.is(ImmortalItemTags.SPIRITUAL_STONES);
                }
            });
        }

        this.addInventoryAndHotBar(inventory, 19, 108);

        this.addDataSlots(this.accessData);
    }

    public int getFlameValue(){
        return this.accessData.get(0);
    }

    public int getMaxValue(){
        return this.accessData.get(2);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotId) {
        return super.quickMoveStack(player, slotId);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.accessLevel, player, ImmortalBlocks.SPIRITUAL_FURNACE.get());
    }

}
