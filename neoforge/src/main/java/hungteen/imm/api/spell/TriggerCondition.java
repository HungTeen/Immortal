package hungteen.imm.api.spell;

import hungteen.htlib.api.registry.SimpleEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * 法术触发时机。
 * @author PangTeen
 * @program Immortal
 * @create 2025/6/16 9:58
 **/
public interface TriggerCondition extends SimpleEntry {

    List<EquipmentSlot> ALL = List.of(EquipmentSlot.values());
    List<EquipmentSlot> ARMOR = List.of(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET);
    List<EquipmentSlot> HANDS = List.of(EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
    List<EquipmentSlot> MAIN_HAND = List.of(EquipmentSlot.MAINHAND);
    List<EquipmentSlot> EMPTY = List.of();

    boolean compatWith(LivingEntity living, ItemStack stack);

    /**
     * 获取此触发条件所需检查的装备位置。
     */
    List<EquipmentSlot> getValidSlots();

    /**
     * @return 详细描述。
     */
    Component getDescription();
}
