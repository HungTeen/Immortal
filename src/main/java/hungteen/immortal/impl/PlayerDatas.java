package hungteen.immortal.impl;

import hungteen.htlib.interfaces.IRangeData;
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
public class PlayerDatas {

    private static final List<IRangeData<Integer>> TYPES = new ArrayList<>();

    public static final IRangeData<Integer> CULTIVATION = new PlayerData("cultivation", 0, 0, Integer.MAX_VALUE);
    public static final IRangeData<Integer> SPIRITUAL_MANA = new PlayerData("spiritual_mana", 0, 0, Integer.MAX_VALUE);
    public static final IRangeData<Integer> MAX_SPIRITUAL_MANA = new PlayerData("max_spiritual_mana", 0, 0, Integer.MAX_VALUE);
    public static final IRangeData<Integer> CONSCIOUSNESS = new PlayerData("consciousness", 0, 0, Integer.MAX_VALUE);
    public static final IRangeData<Integer> OPPORTUNITY = new PlayerData("opportunity", 0, 0, Integer.MAX_VALUE);
    public static final IRangeData<Integer> PERSONALITY = new PlayerData("personality", 0, 0, Integer.MAX_VALUE);
    public static final IRangeData<Integer> IS_GHOST = new PlayerData("is_ghost", 0, 0, 1);

    public record PlayerData(String name, int defaultValue, int minValue, int maxValue) implements IRangeData<Integer> {
        /**
         * {@link ImmortalMod#coreRegister()}
         */
        public static void register() {
            TYPES.forEach(type -> ImmortalAPI.get().registerIntegerData(type));
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
