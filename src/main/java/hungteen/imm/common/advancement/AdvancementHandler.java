package hungteen.imm.common.advancement;

import hungteen.imm.common.advancement.trigger.SpiritualPearlTrigger;
import net.minecraft.advancements.CriteriaTriggers;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/2/25 10:49
 */
public class AdvancementHandler {

    public static void init() {
        CriteriaTriggers.register(SpiritualPearlTrigger.INSTANCE);
    }

}
