package hungteen.imm.common.menu;

import hungteen.htlib.common.menu.HTContainerMenu;
import hungteen.imm.api.spell.Spell;
import hungteen.imm.api.spell.TalismanSpell;
import hungteen.imm.api.spell.TriggerCondition;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.codec.SpellInstance;
import hungteen.imm.common.cultivation.SpellManager;
import hungteen.imm.common.cultivation.TriggerConditions;
import hungteen.imm.common.item.IMMItems;
import hungteen.imm.common.tag.IMMItemTags;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-24 11:19
 **/
public class InscriptionTableMenu extends HTContainerMenu {

    public static final int TOP_HEIGHT = 111;
    public static final int BOTTOM_HEIGHT = 106;
    public static final int SMALL_WIDTH = 74;
    public static final int LARGE_WIDTH = 92;
    public static final int BOTTOM_WIDTH = 198;
    public static final int GAP_HEIGHT = 3;
    public static final int GAP_WIDTH = 3;
    public final ContainerLevelAccess access;
    protected final Container inputSlots;
    protected final ResultContainer resultSlot;
    private final Player player;
    public List<Spell> spells = new ArrayList<>();
    public List<TriggerCondition> conditions = new ArrayList<>();
    private Spell selectedSpell;
    private TriggerCondition triggerCondition;

    public InscriptionTableMenu(int id, Inventory inventory) {
        this(id, inventory, ContainerLevelAccess.NULL);
    }

    public InscriptionTableMenu(int id, Inventory inventory, ContainerLevelAccess access) {
        super(id, IMMMenuTypes.INSCRIPTION_TABLE.get());
        this.access = access;
        this.player = inventory.player;
        this.inputSlots = new SimpleContainer(2){
            @Override
            public void setChanged() {
                super.setChanged();
                InscriptionTableMenu.this.slotsChanged(this);
            }
        };
        this.resultSlot = new ResultContainer();
        this.addSlot(new Slot(this.inputSlots, 0, LARGE_WIDTH + GAP_WIDTH + 13, 39) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                // 只有符箓或不可堆叠的物品可以放进来。
                return stack.getMaxStackSize() == 1 || stack.is(IMMItemTags.EMPTY_TALISMANS);
            }
        });
        this.addSlot(new Slot(this.inputSlots, 1, LARGE_WIDTH + GAP_WIDTH + 29, 69) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(IMMItems.CINNABAR.get());
            }
        });
        this.addSlot(new Slot(this.resultSlot, 0, LARGE_WIDTH + GAP_WIDTH + 45, 39) {
            @Override
            public boolean mayPickup(Player player) {
                return !resultSlot.isEmpty();
            }

            @Override
            public void onTake(Player player, ItemStack stack) {
                super.onTake(player, stack);
                inputSlots.removeItem(0, 1);
                inputSlots.removeItem(1, 1);
            }
        });
        final int leftX = ((getTotalWidth() - BOTTOM_WIDTH) >> 1) + 19;
        final int topY = TOP_HEIGHT + GAP_HEIGHT + 16;
        this.addPlayerInventory(inventory, leftX, topY);
        this.addPlayerHotBar(inventory, leftX, topY + 58);
    }

    @Override
    public void slotsChanged(Container inventory) {
        super.slotsChanged(inventory);
        if (inventory == this.inputSlots) {
            this.spells = getAlternativeSpells();
            this.conditions = getAlternativeConditions();
            this.createResult();
        }
    }

    public void createResult(){
        ItemStack result = ItemStack.EMPTY;
        if(isTalisman()){
            // 选择了法术 & 法术是符箓法术。
            if(selectedSpell != null && selectedSpell.spell() instanceof TalismanSpell talismanSpell) {
                result = talismanSpell.getTalismanItem(selectedSpell.level());
            }
        } else if(isValidArtifact() && selectedSpell != null && triggerCondition != null && matchArtifact(selectedSpell)) {
            ItemStack copy = getCoreItem().copy();
            SpellInstance spellInstance = new SpellInstance(selectedSpell, triggerCondition);
            SpellManager.putSpellInItem(copy, spellInstance);
            result = copy;
        }
        this.resultSlot.setItem(0, result);
    }

    /**
     * @return 获取所有可以候选的法术。
     */
    public List<Spell> getAlternativeSpells() {
        if(player != null && ! getCoreItem().isEmpty()){
            return PlayerUtil.getLearnedSpells(player).stream().filter(spell -> {
                if(isTalisman()){
                    return matchTalisman(spell);
                } else if(isValidArtifact()){
                    return matchArtifact(spell);
                }
                return false;
            }).toList();
        }
        return List.of();
    }

    public List<TriggerCondition> getAlternativeConditions() {
        if(isValidArtifact()){
            return TriggerConditions.registry().helper().values();
        }
        return List.of();
    }

    public boolean matchTalisman(Spell spell){
        return spell.spell() instanceof TalismanSpell;
    }

    public boolean matchArtifact(Spell spell) {
        return !matchTalisman(spell);
    }

    public Optional<Spell> getSelectedSpell() {
        return Optional.ofNullable(selectedSpell);
    }

    public void setSelectedSpell(Spell spell) {
        this.selectedSpell = spell;
        this.createResult();
    }

    public Optional<TriggerCondition> getTriggerCondition() {
        return Optional.ofNullable(triggerCondition);
    }

    public void setTriggerCondition(TriggerCondition condition) {
        this.triggerCondition = condition;
        this.createResult();
    }

    public boolean isTalisman(){
        return getCoreItem().is(IMMItemTags.EMPTY_TALISMANS);
    }

    /**
     * @return 是否是可以绘制法术的法器（法术槽没满）。
     */
    public boolean isValidArtifact(){
        ItemStack stack = getCoreItem();
        return !stack.isEmpty() && !isTalisman() && stack.getMaxStackSize() == 1 && !SpellManager.isSpellFull(stack);
    }

    public ItemStack getCoreItem(){
        return this.inputSlots.getItem(0);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotId) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotId);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            result = itemstack1.copy();
            if (slotId < 3) {
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
                if (!this.moveItemStackTo(itemstack1, 3, 3 + 27, false)) {
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
    public boolean stillValid(Player player) {
        return this.access.evaluate((level, blockPos) -> {
            return level.getBlockState(blockPos).is(IMMBlocks.INSCRIPTION_TABLE.get()) && player.canInteractWithBlock(blockPos, 4);
        }, false);
    }

    public static int getTotalHeight(){
        return TOP_HEIGHT + BOTTOM_HEIGHT + GAP_HEIGHT;
    }

    public static int getTotalWidth(){
        return SMALL_WIDTH + LARGE_WIDTH * 2 + GAP_WIDTH * 2;
    }

}
