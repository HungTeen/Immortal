package hungteen.imm.common.cultivation;

import hungteen.htlib.api.registry.HTCustomRegistry;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.imm.api.spell.TriggerCondition;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2025/6/16 9:59
 **/
public interface TriggerConditions {

    HTCustomRegistry<TriggerCondition> SPELL_TYPES = HTRegistryManager.custom(Util.prefix("trigger_condition"));

    TriggerCondition ATTACK = register("attack");

    static TriggerCondition register(String name) {
        TriggerConditionImpl triggerCondition = new TriggerConditionImpl(name);
        return registry().register(triggerCondition.getLocation(), triggerCondition);
    }

    static HTCustomRegistry<TriggerCondition> registry(){
        return SPELL_TYPES;
    }

    record TriggerConditionImpl(String name) implements TriggerCondition {

        @Override
        public MutableComponent getComponent() {
            return TipUtil.spell(name);
        }

        @Override
        public Component getDescription() {
            return TipUtil.spell(name + ".desc");
        }

        @Override
        public String getModID() {
            return Util.id();
        }

    }
}
