package hungteen.immortal.common.menu;

import hungteen.htlib.common.menu.HTContainerMenu;
import hungteen.immortal.common.block.ImmortalBlocks;
import hungteen.immortal.common.blockentity.SmithingArtifactBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-13 21:51
 **/
public class SmithingArtifactMenu extends HTContainerMenu {

    private final SmithingArtifactBlockEntity blockEntity;
    private final ContainerLevelAccess accessLevel;
    private final Player player;

    public SmithingArtifactMenu(int id, Inventory inventory, FriendlyByteBuf buffer) {
        this(id, inventory, buffer.readBlockPos());
    }

    public SmithingArtifactMenu(int id, Inventory inventory, BlockPos pos) {
        super(id, ImmortalMenus.SMITHING_ARTIFACT.get());
        this.player = inventory.player;
        this.accessLevel = ContainerLevelAccess.create(inventory.player.level, pos);
        BlockEntity blockEntity = inventory.player.level.getBlockEntity(pos);
        if(blockEntity instanceof SmithingArtifactBlockEntity){
            this.blockEntity = (SmithingArtifactBlockEntity) blockEntity;
            this.blockEntity.update();
        } else{
            throw new RuntimeException("Invalid block entity !");
        }

        this.addInventories(51, 31, 5, 5, 0, (x, y, slotId) -> new SlotItemHandler(this.blockEntity.getItemHandler(), x, y, slotId) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return true;
            }

            @Override
            public void setChanged() {
                super.setChanged();
                SmithingArtifactMenu.this.blockEntity.updateRecipes();
            }
        });

        this.addInventoryAndHotBar(inventory, 15, 131);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.accessLevel, player, ImmortalBlocks.COPPER_SMITHING_ARTIFACT.get());
    }
}
