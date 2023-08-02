package hungteen.imm.common.impl.raid;

import com.mojang.serialization.Codec;
import hungteen.htlib.api.interfaces.raid.IRaidType;
import hungteen.htlib.common.impl.raid.HTRaidTypes;
import hungteen.imm.common.world.entity.trial.BreakThroughRaid;
import hungteen.imm.util.Util;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-08-01 17:44
 **/
public class IMMRaidHandler {

    public static final IRaidType<BreakThroughRaid> BREAK_THROUGH_TRIAL = HTRaidTypes.register(new IRaidType<>() {
        @Override
        public Codec<BreakThroughRaid> codec() {
            return BreakThroughRaid.CODEC;
        }

        @Override
        public String getName() {
            return "break_through_trial";
        }

        @Override
        public String getModID() {
            return Util.id();
        }
    });


    public static void init() {

    }
}
