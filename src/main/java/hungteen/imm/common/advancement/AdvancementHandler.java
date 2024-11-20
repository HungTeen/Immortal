package hungteen.imm.common.advancement;

import hungteen.imm.common.advancement.trigger.PlayerLearnSpellTrigger;
import hungteen.imm.common.advancement.trigger.PlayerLearnSpellsTrigger;
import hungteen.imm.common.advancement.trigger.PlayerRealmChangeTrigger;
import hungteen.imm.util.Util;
import net.minecraft.advancements.CriteriaTriggers;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/2/25 10:49
 */
public class AdvancementHandler {

    public static void init() {
//        CriteriaTriggers.register(Util.prefixName("spiritual_pearl"), SpiritualPearlTrigger.INSTANCE);
        CriteriaTriggers.register(Util.prefixName("realm"), PlayerRealmChangeTrigger.INSTANCE);
        CriteriaTriggers.register(Util.prefixName("learn_spell"), PlayerLearnSpellTrigger.INSTANCE);
        CriteriaTriggers.register(Util.prefixName("learn_spells"), PlayerLearnSpellsTrigger.INSTANCE);
    }

}
