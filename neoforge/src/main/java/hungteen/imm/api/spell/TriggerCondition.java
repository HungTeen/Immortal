package hungteen.imm.api.spell;

import hungteen.htlib.api.registry.SimpleEntry;
import net.minecraft.network.chat.Component;

/**
 * 法术触发时机。
 * @author PangTeen
 * @program Immortal
 * @create 2025/6/16 9:58
 **/
public interface TriggerCondition extends SimpleEntry {

    /**
     * @return 详细描述。
     */
    Component getDescription();
}
