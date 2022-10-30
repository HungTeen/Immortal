package hungteen.immortal.common.blockentity;

import hungteen.htlib.blockentity.HTNameableBlockEntity;
import hungteen.immortal.common.item.ImmortalItems;
import hungteen.immortal.common.menu.SpiritualStoveMenu;
import hungteen.immortal.common.tag.ImmortalItemTags;
import hungteen.immortal.utils.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-09 09:30
 **/
public class SpiritualStoveBlockEntity extends ContainerBlockEntity implements MenuProvider {

    private static final int[] SLOTS_FOR_DIRECTIONS = new int[]{1, 2, 3};
    /* first slot is for flame gourd, the rest are spiritual stone slots. */
    protected NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);
    protected final ContainerData accessData = new ContainerData() {
        @Override
        public int get(int p_39284_) {
            return 0;
        }

        @Override
        public void set(int p_39285_, int p_39286_) {

        }

        @Override
        public int getCount() {
            return 0;
        }
    };
    private int burnTick = 0;

    public SpiritualStoveBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ImmortalBlockEntities.SPIRITUAL_STOVE.get(), blockPos, blockState);
    }

    public boolean isConsumingFlame(){
        return true;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return SLOTS_FOR_DIRECTIONS;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new SpiritualStoveMenu(id, inventory, this, this.accessData);
    }

}
