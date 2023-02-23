package hungteen.immortal.common.impl;

import hungteen.htlib.api.interfaces.IRangeNumber;
import hungteen.htlib.util.interfaces.IRangeNumber;
import hungteen.immortal.ImmortalMod;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.utils.Util;
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

    private static final List<IRangeNumber<Integer>> TYPES = new ArrayList<>();

    /**
     * Just like vanilla experience, 玩家的修仙经验值。
     */
    public static final IRangeNumber<Integer> CULTIVATION = new PlayerData("cultivation", 0, 0, Integer.MAX_VALUE);

    /**
     * Tell which stage of realm is the player currently stays, 辨别玩家当前处在什么境界。
     */
    public static final IRangeNumber<Integer> CULTIVATION_POS = new PlayerData("cultivation_pos", 0, 0, Integer.MAX_VALUE);
    public static final IRangeNumber<Integer> SPIRITUAL_MANA = new PlayerData("spiritual_mana", 0, 0, Integer.MAX_VALUE);
    public static final IRangeNumber<Integer> MAX_SPIRITUAL_MANA = new PlayerData("max_spiritual_mana", 0, 0, Integer.MAX_VALUE);
    public static final IRangeNumber<Integer> CONSCIOUSNESS = new PlayerData("consciousness", 0, 0, Integer.MAX_VALUE);
    public static final IRangeNumber<Integer> OPPORTUNITY = new PlayerData("opportunity", 0, 0, Integer.MAX_VALUE);
    public static final IRangeNumber<Integer> PERSONALITY = new PlayerData("personality", 0, 0, Integer.MAX_VALUE);
    public static final IRangeNumber<Integer> IS_GHOST = new PlayerData("is_ghost", 0, 0, 1);

    public record PlayerData(String name, int defaultValue, int minValue, int maxValue) implements IRangeNumber<Integer> {
        /**
         * {@link ImmortalMod#coreRegister()}
         */
        public static void register() {
            ImmortalAPI.get().integerDataRegistry().ifPresent(l -> l.register(TYPES));
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
