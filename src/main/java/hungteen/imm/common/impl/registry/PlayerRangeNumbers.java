package hungteen.imm.common.impl.registry;

import hungteen.htlib.api.interfaces.IHTSimpleRegistry;
import hungteen.htlib.api.interfaces.IRangeNumber;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.htlib.common.registry.HTSimpleRegistry;
import hungteen.imm.ImmortalMod;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.util.Constants;
import hungteen.imm.util.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-13 22:13
 **/
public class PlayerRangeNumbers {

    private static final HTSimpleRegistry<IRangeNumber<Integer>> PLAYER_RANGE_NUMBERS = HTRegistryManager.create(Util.prefix("player_range_numbers"));
    private static final List<IRangeNumber<Integer>> TYPES = new ArrayList<>();

    public static IHTSimpleRegistry<IRangeNumber<Integer>> registry() {
        return PLAYER_RANGE_NUMBERS;
    }

    /* Just like vanilla experience, 玩家的修仙经验值 */
    public static final IRangeNumber<Integer> CULTIVATION = new PlayerData("cultivation", 0, 0, Integer.MAX_VALUE);
    public static final IRangeNumber<Integer> SPIRITUAL_MANA = new PlayerData("spiritual_mana", 0, 0, Integer.MAX_VALUE);
    /* additional mana limit, 额外的附加灵气值 */
    public static final IRangeNumber<Integer> MAX_SPIRITUAL_MANA = new PlayerData("max_spiritual_mana", 0, 0, Integer.MAX_VALUE);
    public static final IRangeNumber<Integer> CONSCIOUSNESS = new PlayerData("consciousness", 0, 0, Integer.MAX_VALUE);
    public static final IRangeNumber<Integer> OPPORTUNITY = new PlayerData("opportunity", 0, 0, Integer.MAX_VALUE);
    public static final IRangeNumber<Integer> PERSONALITY = new PlayerData("personality", 0, 0, Integer.MAX_VALUE);
    public static final IRangeNumber<Integer> IS_GHOST = new PlayerData("is_ghost", 0, 0, 1);
    /* 是否开启默认轮盘，0代表需要客户端配置文件更新选项，1表示默认，2表示滚轮 */
    public static final IRangeNumber<Integer> DEFAULT_SPELL_CIRCLE = new PlayerData("default_spell_circle", 0, 0, 2);
    /* 可用的常驻法术槽位 */
    public static final IRangeNumber<Integer> PASSIVE_SPELL_COUNT_LIMIT = new PlayerData("passive_spell_count_limit", 3, 0, Constants.SPELL_CIRCLE_SIZE);

    public record PlayerData(String name, int defaultValue, int minValue, int maxValue) implements IRangeNumber<Integer> {
        /**
         * {@link ImmortalMod#coreRegister()}
         */
        public static void register() {
            IMMAPI.get().integerDataRegistry().ifPresent(l -> l.register(TYPES));
        }

        public PlayerData(String name, int defaultValue, int minValue, int maxValue){
            this.name = name;
            this.defaultValue = defaultValue;
            this.minValue = minValue;
            this.maxValue = maxValue;
            TYPES.add(this);
        }
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
        public String getName() {
            return name;
        }

        @Override
        public String getModID() {
            return Util.id();
        }

        @Override
        public MutableComponent getComponent() {
            return Component.translatable("misc." + getModID() +".player_data." + getName());
        }

    }
}