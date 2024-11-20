package hungteen.imm.common.menu;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import hungteen.imm.common.rune.ICraftableRune;
import hungteen.imm.common.rune.RuneCategories;
import hungteen.imm.util.BlockUtil;
import hungteen.imm.util.ItemUtil;
import net.minecraft.sounds.SoundEvents;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-09 22:59
 **/
public class RuneCraftingMenu extends RuneBaseMenu {

    public static final int INPUT_SLOT_NUM = 2;
    private static final Predicate<ItemStack> AMETHYST_PREDICATE = stack -> stack.is(Items.AMETHYST_SHARD);
    private static final Predicate<ItemStack> LAPIS_PREDICATE = stack -> stack.is(Items.LAPIS_LAZULI);
    private final DataSlot selectedRecipeIndex = DataSlot.standalone();
    public final Container inputContainer;
    public final ResultContainer resultContainer;
    private List<Pair<ICraftableRune, ItemStack>> recipes = Lists.newArrayList();
    private List<ItemStack> lastInputItems = Lists.newArrayList();
    private ICraftableRune lastInputRune;
    private Runnable clientSlotUpdateListener = () -> {
    };
    private long lastSoundTime;

    public RuneCraftingMenu(int id, Inventory inventory) {
        this(id, inventory, ContainerLevelAccess.NULL);
    }

    public RuneCraftingMenu(int id, Inventory inventory, ContainerLevelAccess access) {
        super(id, IMMMenus.RUNE_CRAFT.get(), inventory, access);
        this.inputContainer = new SimpleContainer(INPUT_SLOT_NUM){
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
                    return this.getContainerSlot() == 0 ? (AMETHYST_PREDICATE.test(stack) || LAPIS_PREDICATE.test(stack)) : stack.is(Items.REDSTONE);
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
                RuneCraftingMenu.this.craftItem(player, stack);
                super.onTake(player, stack);
            }
        });
        this.addPlayerInventory(inventory, 15, 131);
        this.addPlayerHotBar(inventory, 15, 131 + 57);

        this.addDataSlot(this.selectedRecipeIndex);
    }

    public void craftItem(Player player, ItemStack stack){
        if(this.lastInputRune != null){
            stack.onCraftedBy(player.level(), player, stack.getCount());
            this.resultContainer.awardUsedRecipes(player, List.of(stack));
            this.inputContainer.removeItem(0, this.lastInputRune.requireMaterial());
            this.inputContainer.removeItem(1, this.lastInputRune.requireRedStone());
            if(this.canCraft(this.lastInputRune)){
                this.setupResultSlot();
            }

            this.access.execute((level, pos) -> {
                long time = level.getGameTime();
                if(this.lastSoundTime != time){
                    BlockUtil.playSound(level, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT);
                    this.lastSoundTime = time;
                }
            });
        }
    }

    @Override
    public void slotsChanged(Container container) {
        super.slotsChanged(container);
        if(inputSlotChanged()){
            this.updateRecipes(container);
        }
    }

    protected void updateRecipes(Container container){
        this.recipes.clear();
        this.setSelectedRecipeIndex(-1);
        this.resultContainer.setItem(0, ItemStack.EMPTY);
//        if (!p_40305_.isEmpty()) {
            this.recipes = ItemUtil.getCraftableRunes().stream().filter(pair -> canCraft(pair.getFirst())).collect(Collectors.toList());
//        }
    }

    private boolean inputSlotChanged(){
        while(this.lastInputItems.size() < INPUT_SLOT_NUM) {
            this.lastInputItems.add(ItemStack.EMPTY);
        }
        boolean changed = false;
        for(int i = 0; i < INPUT_SLOT_NUM; ++ i) {
            if (!ItemStack.isSameItemSameComponents(this.lastInputItems.get(i), this.inputContainer.getItem(i))) {
                changed = true;
                this.lastInputItems.set(i, this.inputContainer.getItem(i).copy());
            }
        }
        return changed;
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
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return slot.container != this.resultContainer && super.canTakeItemForPickAll(stack, slot);
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
                item.onCraftedBy(itemstack1, player.level(), player);
                if (!this.moveItemStackTo(itemstack1, 3, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, result);
            } else if (slotId < 2) {
                if (!this.moveItemStackTo(itemstack1, 3, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotId < 3 + 27) {
                if (!this.moveItemStackTo(itemstack1, 0, 2, false)) {
                    return ItemStack.EMPTY;
                }
                if (!this.moveItemStackTo(itemstack1, 3 + 27, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.moveItemStackTo(itemstack1, 0, 2, false)) {
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
        if (this.isValidButtonIndex(id)) {
            this.setSelectedRecipeIndex(id);
            this.setupResultSlot();
            return true;
        }
        return super.clickMenuButton(player, id);
    }

    protected void setupResultSlot() {
        if (!this.recipes.isEmpty() && this.isValidButtonIndex(this.getSelectedRecipeIndex())) {
            final Pair<ICraftableRune, ItemStack> recipe = this.recipes.get(this.getSelectedRecipeIndex());
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

    public boolean canCraft(@Nullable ICraftableRune rune){
        final ItemStack stack = this.inputContainer.getItem(0);
        if(rune != null && ((rune.costAmethyst() && AMETHYST_PREDICATE.test(stack)) || (! rune.costAmethyst() && LAPIS_PREDICATE.test(stack)))){
            return stack.getCount() >= rune.requireMaterial()
                    && this.inputContainer.getItem(1).getCount() >= rune.requireRedStone();
        }
        return false;
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

    public void setSelectedRecipeIndex(int index) {
        this.selectedRecipeIndex.set(index);
        if (isValidButtonIndex(index)) {
            this.lastInputRune = this.recipes.get(index).getFirst();
        } else {
            this.lastInputRune = null;
        }
    }

    public void setClientSlotUpdateListener(Runnable clientSlotUpdateListener) {
        this.clientSlotUpdateListener = clientSlotUpdateListener;
    }

    public boolean isValidButtonIndex(int pos) {
        return pos >= 0 && pos < this.recipes.size();
    }

    @Override
    public RuneCategories getRuneCategory() {
        return RuneCategories.CRAFT;
    }

}
