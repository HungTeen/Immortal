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
public class PlayerRangeIntegers {

    private static final HTSimpleRegistry<RangeNumber<Integer>> NUMBERS = HTRegistryManager.simple(Util.prefix("player_range_integer"));

    /**
     * 精神力 or 神识。
     */
    public static final RangeNumber<Integer> CONSCIOUSNESS = register(new PlayerData("consciousness", 0, 0, Integer.MAX_VALUE));

    /**
     * 业障。
     */
    public static final RangeNumber<Integer> KARMA = register(new PlayerData("karma", 0, 0, Integer.MAX_VALUE));
//    public static final RangeNumber<Integer> OPPORTUNITY = initialize(new PlayerData("opportunity", 0, 0, Integer.MAX_VALUE));
//
//    public static final RangeNumber<Integer> PERSONALITY = initialize(new PlayerData("personality", 0, 0, Integer.MAX_VALUE));

    /**
     * 元素精通点。
     */
    public static final RangeNumber<Integer> ELEMENTAL_MASTERY_POINTS = register(new PlayerData("elemental_mastery_points", 0, 0, Integer.MAX_VALUE));

    /**
     * 打坐冥想tick。
     */
    public static final RangeNumber<Integer> MEDITATE_TICK = register(new PlayerData("meditate_tick", 0, 0, 100));

    /**
     * 是否开启默认轮盘，0代表需要客户端配置文件更新选项，1表示默认，2表示滚轮
     */
    public static final RangeNumber<Integer> SPELL_CIRCLE_MODE = register(new PlayerData("spell_circle_mode", 0, 0, 2));

    /**
     * 突破尝试次数。
     */
    public static final RangeNumber<Integer> BREAK_THROUGH_TRIES = register(new PlayerData("break_through_tries", 0, 0, Integer.MAX_VALUE));

    /**
     * 是否知道自身灵根。
     */
    public static final RangeNumber<Integer> KNOW_SPIRITUAL_ROOTS = register(new PlayerData("know_spiritual_roots", 0, 0, 1));

    public static HTSimpleRegistry<RangeNumber<Integer>> registry() {
        return NUMBERS;
    }

    public static RangeNumber<Integer> register(RangeNumber<Integer> number){
        return registry().register(number);
    }

    public record PlayerData(String name, int defaultValue, int minValue, int maxValue) implements RangeNumber<Integer> {

        @Override
        public Integer defaultData() {
            return this.defaultValue;
        }

        @Override
        public Integer getMaxData() {
            return this.maxValue;
        }

        @Override
        public Integer getMinData() {
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
