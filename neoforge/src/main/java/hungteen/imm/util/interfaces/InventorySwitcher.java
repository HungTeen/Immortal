package hungteen.imm.util.interfaces;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/1 9:11
 **/
public interface InventorySwitcher extends InventoryCarrier {

    default boolean hasItemStack(Predicate<ItemStack> predicate) {
        for (int i = 0; i < this.getInventory().getContainerSize(); ++i) {
            if (predicate.test(this.getInventory().getItem(i))) {
                return true;
            }
        }
        return false;
    }

    default List<ItemStack> filterFromInventory(Predicate<ItemStack> predicate) {
        List<ItemStack> list = new ArrayList<>();
        for (int i = 0; i < this.getInventory().getContainerSize(); ++i) {
            if (predicate.test(this.getInventory().getItem(i))) {
                list.add(this.getInventory().getItem(i));
            }
        }
        return list;
    }

    /**
     * Switch one predicate item from inventory to equipment slot.
     */
    default boolean switchInventory(EquipmentSlot equipmentSlot, Predicate<ItemStack> predicate) {
        for (int i = 0; i < this.getInventory().getContainerSize(); ++i) {
            if (predicate.test(this.getInventory().getItem(i))) {
                ItemStack stack = self().getItemBySlot(equipmentSlot).copy();
                self().setItemSlot(equipmentSlot, this.getInventory().getItem(i).copy());
                this.getInventory().setItem(i, stack);
                return true;
            }
        }
        return false;
    }

    /**
     * Switch one predicate item from inventory to hand.
     */
    default boolean switchInventory(InteractionHand hand, Predicate<ItemStack> predicate) {
        return switchInventory(hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND, predicate);
    }

    Mob self();

}
