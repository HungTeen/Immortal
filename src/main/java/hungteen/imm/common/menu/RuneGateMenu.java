package hungteen.imm.common.menu;

import hungteen.imm.common.item.runes.info.FilterRuneItem;
import hungteen.imm.common.rune.RuneCategories;
import hungteen.imm.common.rune.filter.FilterRuneTypes;
import hungteen.imm.common.rune.filter.IFilterRuneType;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-18 09:14
 **/
public class RuneGateMenu extends RuneBaseMenu{

    public static final int INPUT_SLOT_NUM = 3;
    private final DataSlot selectedGateIndex = DataSlot.standalone();
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
                    return stack.getItem() instanceof FilterRuneItem<?>;
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

    public List<IFilterRuneType<?>> getGateTypes() {
        return FilterRuneTypes.registry().getValues().stream().filter(IFilterRuneType::isGateRune).toList();
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
