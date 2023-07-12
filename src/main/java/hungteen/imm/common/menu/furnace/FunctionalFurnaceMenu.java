package hungteen.imm.common.menu.furnace;

import hungteen.htlib.common.menu.HTContainerMenu;
import hungteen.imm.common.blockentity.FunctionalFurnaceBlockEntity;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkHooks;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-07-12 19:43
 **/
public abstract class FunctionalFurnaceMenu<T extends FunctionalFurnaceBlockEntity> extends HTContainerMenu {

    protected T blockEntity;
    protected final ContainerData accessData;
    protected final ContainerLevelAccess accessLevel;
    protected final Player player;

    public FunctionalFurnaceMenu(int id, MenuType<?> menuType, Inventory inventory, ContainerData accessData, BlockPos pos) {
        super(id, menuType);
        this.accessData = accessData;
        this.player = inventory.player;
        this.accessLevel = ContainerLevelAccess.create(inventory.player.level(), pos);
        final BlockEntity blockEntity = player.level().getBlockEntity(pos);
        this.blockEntity = (T) blockEntity;
        assert this.blockEntity != null;
        this.blockEntity.opened();
        this.blockEntity.update();
    }

    @Override
    public boolean clickMenuButton(Player player, int slotId) {
        if(slotId == 0 && this.canSwitchToFurnaceMenu()){
            if(player instanceof ServerPlayer serverPlayer){
                NetworkHooks.openScreen(serverPlayer, this.getBlockEntity().getFurnaceBlockEntity(), buf -> {
                    buf.writeBlockPos(this.blockEntity.getFurnaceBlockEntity().getBlockPos());
                });
            }
            return true;
        }
        return false;
    }

    public T getBlockEntity(){
        return this.blockEntity;
    }

    public boolean canSwitchToFurnaceMenu(){
        return this.getBlockEntity().getFurnaceBlockEntity() != null;
    }

    @Override
    public boolean stillValid(Player player) {
        return PlayerUtil.stillValid(this.accessLevel, player, getBlockEntity().getBlockState().getBlock(), 100);
    }

}
