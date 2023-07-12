package hungteen.imm.common.menu.furnace;

import hungteen.htlib.common.menu.HTContainerMenu;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.blockentity.SpiritualFurnaceBlockEntity;
import hungteen.imm.common.item.IMMItems;
import hungteen.imm.common.menu.IMMMenus;
import hungteen.imm.common.tag.IMMItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.network.NetworkHooks;

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
        super(id, IMMMenus.SPIRITUAL_FURNACE.get());
        this.accessData = accessData;
        this.player = inventory.player;
        this.accessLevel = ContainerLevelAccess.create(inventory.player.level(), pos);
        BlockEntity blockEntity = inventory.player.level().getBlockEntity(pos);
        if(blockEntity instanceof SpiritualFurnaceBlockEntity){
            this.blockEntity = (SpiritualFurnaceBlockEntity) blockEntity;
            this.blockEntity.opened();
        } else{
            throw new RuntimeException("Invalid block entity !");
        }

        this.addSlot(new SlotItemHandler(this.blockEntity.getItemHandler(), 0, 91, 25){
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(IMMItems.FLAME_GOURD.get());
            }
        });

        for(int i = 0; i < 3; ++ i){
            this.addSlot(new SlotItemHandler(this.blockEntity.getItemHandler(), 1 + i, 73 + 18 * i, 62){
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.is(IMMItemTags.SPIRITUAL_STONES);
                }
            });
        }

        this.addInventoryAndHotBar(inventory, 19, 108);

        this.addDataSlots(this.accessData);
    }

    @Override
    public boolean clickMenuButton(Player player, int slotId) {
        if(slotId == 0 && this.canSwitchToFunctionalMenu()){
            if(player instanceof ServerPlayer serverPlayer){
                NetworkHooks.openScreen(serverPlayer, this.blockEntity.getFunctionalBlockEntity(), buf -> {
                    buf.writeBlockPos(this.blockEntity.getFunctionalBlockEntity().getBlockPos());
                });
            }
            return true;
        }
        return super.clickMenuButton(player, slotId);
    }

    public int getFlameValue(){
        return this.accessData.get(0);
    }

    public boolean triggered(){
        return this.accessData.get(1) == 1;
    }

    public int getMaxValue(){
        return this.accessData.get(2);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotId) {
        return ItemStack.EMPTY;
    }

    public boolean canSwitchToFunctionalMenu(){
        return this.blockEntity.getFunctionalBlockEntity() != null;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.accessLevel, player, IMMBlocks.COPPER_FURNACE.get());
    }

}
