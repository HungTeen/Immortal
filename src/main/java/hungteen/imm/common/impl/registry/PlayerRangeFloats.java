package hungteen.imm.common.impl.registry;

import hungteen.htlib.api.interfaces.IHTSimpleRegistry;
import hungteen.htlib.api.interfaces.IRangeNumber;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.htlib.common.registry.HTSimpleRegistry;
import hungteen.imm.util.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-13 22:13
 **/
public class PlayerRangeFloats {

    private static final HTSimpleRegistry<IRangeNumber<Float>> PLAYER_RANGE_NUMBERS = HTRegistryManager.createSimple(Util.prefix("player_range_numbers"));

    public static IHTSimpleRegistry<IRangeNumber<Float>> registry() {
        return PLAYER_RANGE_NUMBERS;
    }

    /* Just like vanilla experience, 玩家的修仙经验值 */
    public static final IRangeNumber<Float> CULTIVATION = register(new PlayerData("cultivation", 0, 0, Float.MAX_VALUE));
    public static final IRangeNumber<Float> SPIRITUAL_MANA = register(new PlayerData("spiritual_mana", 0, 0, Float.MAX_VALUE));
    /* additional mana limit, 额外的附加灵气值 */
    public static final IRangeNumber<Float> MAX_SPIRITUAL_MANA = register(new PlayerData("max_spiritual_mana", 0, 0, Float.MAX_VALUE));

    public static IRangeNumber<Float> register(IRangeNumber<Float> number){
        return registry().register(number);
    }

    public record PlayerData(String name, float defaultValue, float minValue, float maxValue) implements IRangeNumber<Float> {

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
