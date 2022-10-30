package hungteen.immortal.common.blockentity;

import hungteen.htlib.blockentity.HTNameableBlockEntity;
import hungteen.immortal.common.item.ImmortalItems;
import hungteen.immortal.common.tag.ImmortalItemTags;
import hungteen.immortal.utils.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-27 15:00
 **/
public abstract class ContainerBlockEntity extends HTNameableBlockEntity implements WorldlyContainer {

    public ContainerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState state) {
        super(blockEntityType, blockPos, state);
    }

    public abstract NonNullList<ItemStack> getItems();

    /**
     * Ignore direction.
     */
    public boolean canPlaceItem(int id, ItemStack stack) {
        return true;
    }

    @Override
    public boolean canPlaceItemThroughFace(int id, ItemStack stack, @Nullable Direction direction) {
        return this.canPlaceItem(id, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int id, ItemStack stack, Direction direction) {
        return true;
    }

    @Override
    public int getContainerSize() {
        return getItems().size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack stack : getItems()) {
            if(! stack.isEmpty()){
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int id) {
        return getItems().get(id);
    }

    @Override
    public ItemStack removeItem(int id, int count) {
        return ContainerHelper.removeItem(this.getItems(), id, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int id) {
        return ContainerHelper.takeItem(this.getItems(), id);
    }

    @Override
    public void setItem(int id, ItemStack stack) {
        this.getItems().set(id, stack);
        if(stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
    }

    @Override
    public void setChanged() {

    }

    @Override
    public boolean stillValid(Player player) {
        return BlockUtil.stillValid(player, this);
    }

    @Override
    public void clearContent() {
        this.getItems().clear();
    }

    @Override
    protected Component getDefaultName() {
        return TextComponent.EMPTY;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("ContainerItems", ContainerHelper.saveAllItems(new CompoundTag(), this.getItems()));
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if(tag.contains("ContainerItems")){
            ContainerHelper.loadAllItems(tag.getCompound("ContainerItems"), this.getItems());
        }
    }

}
