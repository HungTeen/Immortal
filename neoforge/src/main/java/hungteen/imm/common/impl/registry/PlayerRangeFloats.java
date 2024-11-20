package hungteen.imm.common.impl.registry;

import hungteen.htlib.api.registry.HTSimpleRegistry;
import hungteen.htlib.api.registry.RangeNumber;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.network.chat.MutableComponent;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-13 22:13
 **/
public class PlayerRangeFloats {

    private static final HTSimpleRegistry<RangeNumber<Float>> NUMBERS = HTRegistryManager.simple(Util.prefix("player_range_float"));

    public static final RangeNumber<Float> MAX_SPIRITUAL_MANA = register(new PlayerData("max_spiritual_mana", 0, 0, Float.MAX_VALUE));
    public static final RangeNumber<Float> SPIRITUAL_MANA = register(new PlayerData("spiritual_mana", 0, 0, Float.MAX_VALUE));
    public static final RangeNumber<Float> BREAK_THROUGH_PROGRESS = register(new PlayerData("break_through_progress", 0, 0, 1));

    public static HTSimpleRegistry<RangeNumber<Float>> registry() {
        return NUMBERS;
    }

    public static RangeNumber<Float> register(RangeNumber<Float> number){
        return registry().register(number);
    }

    public record PlayerData(String name, float defaultValue, float minValue, float maxValue) implements RangeNumber<Float> {

        @Override
        public Float defaultData() {
            return this.defaultValue;
        }

        @Override
        public Float getMaxData() {
            return this.maxValue;
        }

        @Override
        public Float getMinData() {
            return this.minValue;
        }

        @Override
        public String getModID() {
            return Util.id();
        }

        @Override
        public MutableComponent getComponent() {
            return TipUtil.misc("player_data." + getName());
        }

    }
}
