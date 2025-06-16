package hungteen.imm.common.menu;

import hungteen.imm.common.item.runes.BehaviorRuneItem;
import hungteen.imm.common.item.runes.filter.FilterRuneItem;
import hungteen.imm.common.cultivation.rune.RuneCategories;
import hungteen.imm.common.cultivation.rune.behavior.IBehaviorRune;
import hungteen.imm.util.BlockUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-04-21 22:22
 **/
public class RuneBindMenu extends RuneBaseMenu{

    public static final int INPUT_SLOT_NUM = 6;
    public final Container inputContainer;
    public final ResultContainer resultContainer;
    private Runnable clientSlotUpdateListener = () -> {
    };
    private IBehaviorRune currentBehavior;
    private long lastSoundTime;

    public RuneBindMenu(int id, Inventory inventory) {
        this(id, inventory, ContainerLevelAccess.NULL);

    }

    public RuneBindMenu(int id, Inventory inventory, ContainerLevelAccess access) {
        super(id, IMMMenuTypes.RUNE_BIND.get(), inventory, access);
        this.inputContainer = new SimpleContainer(INPUT_SLOT_NUM) {

            @Override
            public void setChanged() {
                super.setChanged();
                RuneBindMenu.this.slotsChanged(this);
                RuneBindMenu.this.clientSlotUpdateListener.run();
            }
        };
        this.resultContainer = new ResultContainer();
        this.addInventories(58, 64, 1, 1, 0, (slot, x, y) -> {
            return new Slot(this.inputContainer, slot, x, y) {

                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.getItem() instanceof BehaviorRuneItem;
                }
            };
        });

        this.addInventories(51, 92, 1, INPUT_SLOT_NUM - 1, 1, (slot, x, y) -> {
            return new Slot(this.inputContainer, slot, x, y) {

                @Override
                public boolean isActive() {
                    return validSlot(this.getSlotIndex());
                }

                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.getItem() instanceof FilterRuneItem<?> item && item.getGateRune(stack).isPresent() && matchFilter(this.getSlotIndex(), stack);
                }
            };
        });
        this.addSlot(new Slot(this.resultContainer, INPUT_SLOT_NUM, 115, 64) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(Player player, ItemStack stack) {
                RuneBindMenu.this.craftItem(player, stack);
                super.onTake(player, stack);
            }
        });
        this.addPlayerInventory(inventory, 15, 131);
        this.addPlayerHotBar(inventory, 15, 131 + 57);
    }

    public void craftItem(Player player, ItemStack stack) {
        for(int i = 1; i < inputContainer.getContainerSize(); ++ i){
            if(! inputContainer.getItem(i).isEmpty()){
                inputContainer.removeItem(i, 1);
            }
        }
        //delay remove to avoid skipping
        inputContainer.removeItem(0, 1);
        this.setupResultSlot();

        this.access.execute((level, pos) -> {
            long time = level.getGameTime();
            if (this.lastSoundTime != time) {
                BlockUtil.playSound(level, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT);
                this.lastSoundTime = time;
            }
        });
    }

    @Override
    public void slotsChanged(Container container) {
        super.slotsChanged(container);
        if(container.getItem(0).getItem() instanceof BehaviorRuneItem item){
            this.currentBehavior = item.getRune();
        } else if(container.getItem(0).isEmpty() && this.currentBehavior != null){
            this.access.execute((level, pos) -> {
                this.clearContainer(player, this.inputContainer);
            });
            this.currentBehavior = null;
        }
        this.setupResultSlot();
    }

    protected void setupResultSlot() {
        if(this.currentBehavior != null){
            final ItemStack stack = this.inputContainer.getItem(0).copy();
            if(stack.getItem() instanceof BehaviorRuneItem item){
                for(int i = 1; i < INPUT_SLOT_NUM; ++ i){
                    final ItemStack filterStack = this.inputContainer.getItem(i);
                    if(this.matchFilter(i, filterStack) && filterStack.getItem() instanceof FilterRuneItem<?> filterItem){
                        final int pos = i - 1;
                        filterItem.getGateRune(filterStack).ifPresent(rune -> {
                            item.setFilter(stack, pos, rune);
                        });
                    }
                }
            }
            if(! ItemStack.isSameItemSameComponents(stack, this.inputContainer.getItem(0))){
                this.resultContainer.setItem(0, stack);
            } else {
                this.resultContainer.setItem(0, ItemStack.EMPTY);
            }
        } else {
            this.resultContainer.setItem(0, ItemStack.EMPTY);
        }
        this.broadcastChanges();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotId) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotId);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            Item item = itemstack1.getItem();
            result = itemstack1.copy();
            if (slotId == INPUT_SLOT_NUM) {
                item.onCraftedBy(itemstack1, player.level(), player);
                if (!this.moveItemStackTo(itemstack1, INPUT_SLOT_NUM + 1, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, result);
            } else if (slotId < INPUT_SLOT_NUM) {
                if (!this.moveItemStackTo(itemstack1, INPUT_SLOT_NUM + 1, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotId < INPUT_SLOT_NUM + 1 + 27) {
                if (!this.moveItemStackTo(itemstack1, 0, INPUT_SLOT_NUM, false)) {
                    return ItemStack.EMPTY;
                }
                if (!this.moveItemStackTo(itemstack1, INPUT_SLOT_NUM + 1 + 27, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.moveItemStackTo(itemstack1, 0, INPUT_SLOT_NUM, false)) {
                    return ItemStack.EMPTY;
                }
                if (!this.moveItemStackTo(itemstack1, INPUT_SLOT_NUM + 1, INPUT_SLOT_NUM + 1 + 27, false)) {
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

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.resultContainer.removeItemNoUpdate(0);
        this.access.execute((level, pos) -> {
            this.clearContainer(player, this.inputContainer);
        });
    }

    public boolean matchFilter(int slotId, ItemStack stack){
        return validSlot(slotId) && stack.getItem() == this.currentBehavior.getFilterItems().get(slotId - 1).get();
    }

    public boolean validSlot(int slotId) {
        return this.currentBehavior != null && slotId > 0 && slotId - 1 < this.currentBehavior.maxSlot();
    }

    @Nullable
    public IBehaviorRune getCurrentBehavior() {
        return currentBehavior;
    }

    public void setClientSlotUpdateListener(Runnable clientSlotUpdateListener) {
        this.clientSlotUpdateListener = clientSlotUpdateListener;
    }

    @Override
    public RuneCategories getRuneCategory() {
        return RuneCategories.BIND;
    }

}
