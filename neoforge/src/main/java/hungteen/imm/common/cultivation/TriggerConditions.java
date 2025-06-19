package hungteen.imm.common.cultivation;

import hungteen.htlib.api.registry.HTCustomRegistry;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.imm.api.spell.TriggerCondition;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2025/6/16 9:59
 **/
public interface TriggerConditions {

    HTCustomRegistry<TriggerCondition> SPELL_TYPES = HTRegistryManager.custom(Util.prefix("trigger_condition"));

    TriggerCondition ATTACK = register("attack", TriggerCondition.MAIN_HAND);
    TriggerCondition TOSS = register("toss", TriggerCondition.EMPTY);
    TriggerCondition HURT = register("hurt", TriggerCondition.ALL);
    TriggerCondition BREAK = register("break", TriggerCondition.MAIN_HAND);
    TriggerCondition RIGHT_CLICK = register("right_click", TriggerCondition.EMPTY);
    TriggerCondition SHOOT = register("shoot", TriggerCondition.HANDS);
    TriggerCondition IMPACT = register("impact", TriggerCondition.ARMOR);
    TriggerCondition CAST = register("cast", TriggerCondition.ARMOR);
    TriggerCondition COOLDOWN = register("cooldown", TriggerCondition.ARMOR);
    TriggerCondition SWING = register("swing", TriggerCondition.HANDS);

    static TriggerCondition register(String name, List<EquipmentSlot> slots) {
        TriggerConditionImpl triggerCondition = new TriggerConditionImpl(name, slots){
            @Override
            public boolean compatWith(LivingEntity living, ItemStack stack) {
                return true;
            }
        };
        return registry().register(triggerCondition.getLocation(), triggerCondition);
    }

    static Component getSlotComponent(TriggerCondition condition){
        List<EquipmentSlot> slots = condition.getValidSlots();
        if(! slots.isEmpty()){
            MutableComponent component = TipUtil.spell("condition.slots");
            component.append(getSlotComponent(slots.get(0)));
            for(int i = 1; i < slots.size(); ++ i){
                component.append(",").append(getSlotComponent(slots.get(i)));
            }
            return component;
        }
        return Component.empty();
    }

    static Component getSlotComponent(EquipmentSlot slot){
        return TipUtil.spell("condition." + slot.getName());
    }

    static HTCustomRegistry<TriggerCondition> registry(){
        return SPELL_TYPES;
    }

    abstract class TriggerConditionImpl implements TriggerCondition {

        private final String name;
        private final List<EquipmentSlot> slots;

        protected TriggerConditionImpl(String name, List<EquipmentSlot> slots) {
            this.name = name;
            this.slots = slots;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public List<EquipmentSlot> getValidSlots() {
            return slots;
        }

        @Override
        public MutableComponent getComponent() {
            return TipUtil.spell("condition." + name);
        }

        @Override
        public Component getDescription() {
            return TipUtil.spell("condition." + name + ".desc");
        }

        @Override
        public String getModID() {
            return Util.id();
        }

    }
}
