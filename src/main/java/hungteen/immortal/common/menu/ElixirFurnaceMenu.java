package hungteen.immortal.common.menu;

import hungteen.htlib.menu.HTContainerMenu;
import hungteen.immortal.api.registry.ISpiritualRoot;
import hungteen.immortal.common.block.ImmortalBlocks;
import hungteen.immortal.common.blockentity.ElixirFurnaceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-28 18:18
 **/
public class ElixirFurnaceMenu extends HTContainerMenu {

    private final ElixirFurnaceBlockEntity blockEntity;
    private final ContainerData accessData;
    private final ContainerLevelAccess accessLevel;
    private final Player player;

    public ElixirFurnaceMenu(int id, Inventory inventory, FriendlyByteBuf buffer) {
        this(id, inventory, new SimpleContainerData(3), buffer.readBlockPos());
    }

    public ElixirFurnaceMenu(int id, Inventory inventory, ContainerData accessData, BlockPos pos) {
        super(id, ImmortalMenus.ELIXIR_FURNACE.get());
        this.accessData = accessData;
        this.player = inventory.player;
        this.accessLevel = ContainerLevelAccess.create(inventory.player.level, pos);
        BlockEntity blockEntity = inventory.player.level.getBlockEntity(pos);
        if(blockEntity instanceof ElixirFurnaceBlockEntity){
            this.blockEntity = (ElixirFurnaceBlockEntity) blockEntity;
            this.blockEntity.update();
        } else{
            throw new RuntimeException("Invalid block entity !");
        }

//        this.addInventories(this.container, 73, 53, 3, 3, 0);
        for(int i = 0; i < 3; ++ i){
            for(int j = 0; j < 3; ++ j){
                this.addSlot(new SlotItemHandler(this.blockEntity.getItemHandler(), i * 3 + j, 73 + i * 18, 53 + j * 18){
//                    @Override
//                    public void setChanged() {
//                        super.setChanged();
//                        ElixirFurnaceMenu.this.slotsChanged(this.container);
//                    }

                    @Override
                    public boolean mayPlace(@NotNull ItemStack stack) {
                        return ElixirFurnaceMenu.this.blockEntity.getSmeltingState() != ElixirFurnaceBlockEntity.SmeltingStates.SMELTING;
                    }
                });
            }
        }

        this.addInventoryAndHotBar(inventory, 19, 140);

        this.addDataSlots(this.accessData);
    }

    public ItemStack getResultItem(){
        return this.blockEntity.getResultItem();
    }

    public Map<ISpiritualRoot, Integer> getRecipeMap() {
        return this.blockEntity.getRecipeMap();
    }

    public Map<ISpiritualRoot, Integer> getSpiritualMap() {
        return this.blockEntity.getSpiritualMap();
    }

    public ElixirFurnaceBlockEntity.SmeltingStates getElixirStates() {
        return this.blockEntity.getSmeltingState();
    }

    public int getIngredientLimit(){
        return this.blockEntity.getIngredientLimit();
    }

    public int getSmeltingTick(){
        return this.accessData.get(0);
    }

    public int getExplodeTick(){
        return this.accessData.get(1);
    }

    public int getScore(){
        return this.accessData.get(2);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotId) {
        return super.quickMoveStack(player, slotId);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.accessLevel, player, ImmortalBlocks.ELIXIR_FURNACE.get());
    }

}
