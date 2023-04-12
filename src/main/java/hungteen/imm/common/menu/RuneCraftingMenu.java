package hungteen.imm.common.menu;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import hungteen.htlib.common.menu.HTContainerMenu;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.rune.ICraftableRune;
import hungteen.imm.util.ItemUtil;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-09 22:59
 **/
public class RuneCraftingMenu extends HTContainerMenu {

    public static final int INPUT_SLOT_NUM = 2;
    public static final int OUTPUT_SLOT_NUM = 1;
    private final ContainerLevelAccess access;
    private final Level level;
    private final DataSlot selectedRecipeIndex = DataSlot.standalone();
    public final Container inputContainer;
    public final ResultContainer resultContainer;
    private List<Pair<ICraftableRune, ItemStack>> recipes = Lists.newArrayList();
    private List<ItemStack> lastInputItems = Lists.newArrayList();
    private Runnable clientSlotUpdateListener = () -> {
    };

    public RuneCraftingMenu(int id, Inventory inventory) {
        this(id, inventory, ContainerLevelAccess.NULL);
    }

    public RuneCraftingMenu(int id, Inventory inventory, ContainerLevelAccess access) {
        super(id, IMMMenus.RUNE_CRAFT.get());
        this.access = access;
        this.level = inventory.player.level;
        this.inputContainer = new SimpleContainer(2){
            @Override
            public void setChanged() {
                super.setChanged();
                RuneCraftingMenu.this.slotsChanged(this);
                RuneCraftingMenu.this.clientSlotUpdateListener.run();
            }
        };
        this.resultContainer = new ResultContainer();
        this.addInventories(21, 59, INPUT_SLOT_NUM, 1, 0, (slot, x, y) -> {
            return new Slot(this.inputContainer, slot, x, y){
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return this.getContainerSlot() == 0 ? stack.is(Items.AMETHYST_SHARD) : stack.is(Items.REDSTONE);
                }
            };
        });
        this.addSlot(new Slot(this.resultContainer, 2, 154, 68){

            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(Player player, ItemStack stack) {
//                stack.onCraftedBy(player.level, player, stack.getCount());
//                StonecutterMenu.this.resultContainer.awardUsedRecipes(player);
//                ItemStack itemstack = StonecutterMenu.this.inputSlot.remove(1);
//                if (!itemstack.isEmpty()) {
//                    StonecutterMenu.this.setupResultSlot();
//                }
//
//                p_40299_.execute((p_40364_, p_40365_) -> {
//                    long l = p_40364_.getGameTime();
//                    if (StonecutterMenu.this.lastSoundTime != l) {
//                        p_40364_.playSound((Player)null, p_40365_, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
//                        StonecutterMenu.this.lastSoundTime = l;
//                    }
//
//                });
                super.onTake(player, stack);
            }
        });
        this.addPlayerInventory(inventory, 15, 131);
        this.addPlayerHotBar(inventory, 15, 131 + 57);

        this.addDataSlot(this.selectedRecipeIndex);
    }

    @Override
    public void slotsChanged(Container container) {
        if(inputSlotChanged()){
            this.updateRecipes(container);
        }
    }

    protected void updateRecipes(Container container){
        this.recipes.clear();
        this.selectedRecipeIndex.set(-1);
        this.resultContainer.setItem(0, ItemStack.EMPTY);
//        if (!p_40305_.isEmpty()) {
            this.recipes = ItemUtil.getCraftableRunes().stream().filter(pair -> {
                return this.inputContainer.getItem(0).getCount() > pair.getFirst().requireAmethyst()
                        && this.inputContainer.getItem(1).getCount() > pair.getFirst().requireRedStone();
            }).collect(Collectors.toList());
//        }
    }

    private boolean inputSlotChanged(){
        while(this.lastInputItems.size() < INPUT_SLOT_NUM) this.lastInputItems.add(ItemStack.EMPTY);
        boolean changed = false;
        for(int i = 0; i < INPUT_SLOT_NUM; ++ i) {
            if (!ItemStack.isSameItemSameTags(this.lastInputItems.get(i), this.inputContainer.getItem(i))) {
                changed = true;
                this.lastInputItems.set(i, this.inputContainer.getItem(i).copy());
            }
        }
        return changed;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.resultContainer.removeItemNoUpdate(1);
        this.access.execute((p_40313_, p_40314_) -> {
            this.clearContainer(player, this.inputContainer);
        });
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotId) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotId);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            Item item = itemstack1.getItem();
            result = itemstack1.copy();
            if (slotId == 2) {
                item.onCraftedBy(itemstack1, player.level, player);
                if (!this.moveItemStackTo(itemstack1, 3, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, result);
            } else if (slotId < 2) {
                if (!this.moveItemStackTo(itemstack1, 3, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotId < 3 + 27) {
                if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
                if (!this.moveItemStackTo(itemstack1, 3 + 27, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
                if(!this.moveItemStackTo(itemstack1, 3, 3 + 27, false)) {
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
    public boolean clickMenuButton(Player player, int id) {
        if (this.isValidSlotIndex(id)) {
            this.selectedRecipeIndex.set(id);
            this.setupResultSlot();
        }
        return true;
    }

    protected void setupResultSlot() {
        if (!this.recipes.isEmpty() && this.isValidSlotIndex(this.selectedRecipeIndex.get())) {
            final Pair<ICraftableRune, ItemStack> recipe = this.recipes.get(this.selectedRecipeIndex.get());
            final ItemStack itemstack = recipe.getSecond().copy();
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

    public boolean hasRecipes() {
        return !this.recipes.isEmpty();
    }

    public List<Pair<ICraftableRune, ItemStack>> getRecipes() {
        return this.recipes;
    }

    public int getNumRecipes() {
        return this.recipes.size();
    }

    public int getSelectedRecipeIndex() {
        return selectedRecipeIndex.get();
    }

    public void setClientSlotUpdateListener(Runnable clientSlotUpdateListener) {
        this.clientSlotUpdateListener = clientSlotUpdateListener;
    }

    @Override
    public boolean isValidSlotIndex(int pos) {
        return pos >= 0 && pos < this.recipes.size();
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, IMMBlocks.RUNE_WORK_BENCH.get());
    }
}
