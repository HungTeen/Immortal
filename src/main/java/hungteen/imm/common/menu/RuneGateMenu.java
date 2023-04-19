package hungteen.imm.common.menu;

import hungteen.imm.common.item.runes.info.FilterRuneItem;
import hungteen.imm.common.rune.RuneCategories;
import hungteen.imm.common.rune.filter.*;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-18 09:14
 **/
public class RuneGateMenu extends RuneBaseMenu{

    public static final int INPUT_SLOT_NUM = 3;
    private final DataSlot selectedGateIndex = DataSlot.standalone();
    private final DataSlot validStatus = DataSlot.standalone();
    public final Container inputContainer;
    public final ResultContainer resultContainer;

    public RuneGateMenu(int id, Inventory inventory) {
        this(id, inventory, ContainerLevelAccess.NULL);
    }

    public RuneGateMenu(int id, Inventory inventory, ContainerLevelAccess access) {
        super(id, IMMMenus.RUNE_GATE.get(), inventory, access);
        this.inputContainer = new SimpleContainer(INPUT_SLOT_NUM){
            @Override
            public void setChanged() {
                super.setChanged();
//                RuneCraftingMenu.this.slotsChanged(this);
//                RuneCraftingMenu.this.clientSlotUpdateListener.run();
            }
        };
        this.resultContainer = new ResultContainer();

        this.addInventories(51, 96, 1, INPUT_SLOT_NUM, 0, (slot, x, y) -> {
            return new Slot(this.inputContainer, slot, x, y){
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.getItem() instanceof FilterRuneItem<?> item && item.getGateRune(stack).isPresent();
                }
            };
        });
        this.addSlot(new Slot(this.resultContainer, 3, 123, 96){

            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(Player player, ItemStack stack) {
                RuneGateMenu.this.craftItem(player, stack);
                super.onTake(player, stack);
            }
        });
        this.addPlayerInventory(inventory, 15, 131);
        this.addPlayerHotBar(inventory, 15, 131 + 57);

        this.addDataSlot(this.selectedGateIndex);
        this.addDataSlot(this.validStatus);
    }

    public void craftItem(Player player, ItemStack stack){
//        if(this.lastInputRune != null){
//            stack.onCraftedBy(player.level, player, stack.getCount());
//            this.resultContainer.awardUsedRecipes(player);
//            this.inputContainer.removeItem(0, this.lastInputRune.requireAmethyst());
//            this.inputContainer.removeItem(1, this.lastInputRune.requireRedStone());
//            if(this.canCraft(this.lastInputRune)){
//                this.setupResultSlot();
//            }
//
//            this.access.execute((level, pos) -> {
//                long time = level.getGameTime();
//                if(this.lastSoundTime != time){
//                    BlockUtil.playSound(level, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT);
//                    this.lastSoundTime = time;
//                }
//            });
//        }
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (this.isValidButtonIndex(id)) {
            this.setSelectedGateIndex(id);
            this.setupResultSlot();
            return true;
        }
        return super.clickMenuButton(player, id);
    }

    protected void setupResultSlot() {
        if (this.isValidButtonIndex(this.getSelectedGateIndex())) {
            final IFilterRuneType<?> type = this.getGateTypes().get(this.getSelectedGateIndex());
            final ItemStack itemstack = getResult(type);
            if (itemstack.isItemEnabled(this.level.enabledFeatures())) {
                this.resultContainer.setItem(0, itemstack);
            } else {
                this.resultContainer.setItem(0, ItemStack.EMPTY);
            }
        } else {
            this.resultContainer.setItem(0, ItemStack.EMPTY);
        }

        this.broadcastChanges();
    }

    protected ItemStack getResult(IFilterRuneType<?> type){
        FilterRuneItem<?> sameItem = null;
        final List<IFilterRune> runes = new ArrayList<>();
        for(int i = 0; i < INPUT_SLOT_NUM; ++ i){
            final ItemStack stack = this.inputContainer.getItem(i);
            if(! stack.isEmpty() && stack.getItem() instanceof FilterRuneItem<?> item){
                if(sameItem == null){
                    sameItem = item;
                } else if(! stack.is(sameItem)){
                    //符文类型不一致，报错。
                    this.setValidStatus(1);
                    return ItemStack.EMPTY;
                }
                item.getGateRune(stack).ifPresent(runes::add);
            }
        }
        //多于一个符文才能合成。
        if(sameItem != null && runes.size() > 1){
            ItemStack stack = new ItemStack(sameItem);
            if(type == FilterRuneTypes.AND){
                sameItem.setGateRune(stack, new AndGateRune(sameItem, runes));
            } else if(type == FilterRuneTypes.OR){
                sameItem.setGateRune(stack, new OrGateRune(sameItem, runes));
            }
            return stack.copy();
        }
        return ItemStack.EMPTY;
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
                item.onCraftedBy(itemstack1, player.level, player);
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
                if(!this.moveItemStackTo(itemstack1, INPUT_SLOT_NUM + 1, INPUT_SLOT_NUM + 1 + 27, false)) {
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

    public List<IFilterRuneType<?>> getGateTypes() {
        return FilterRuneTypes.registry().getValues();
    }

    public int getGateNums() {
        return this.getGateTypes().size();
    }

    public int getSelectedGateIndex() {
        return selectedGateIndex.get();
    }

    public void setSelectedGateIndex(int id) {
        this.selectedGateIndex.set(id);
    }

    public int getValidStatus() {
        return validStatus.get();
    }

    public void setValidStatus(int status) {
        validStatus.set(status);
    }

    public boolean isValidButtonIndex(int pos) {
        return pos >= 0 && pos < this.getGateNums();
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.resultContainer.removeItemNoUpdate(0);
        this.access.execute((level, pos) -> {
            this.clearContainer(player, this.inputContainer);
        });
    }

    @Override
    public RuneCategories getRuneCategory() {
        return RuneCategories.GATE;
    }

}