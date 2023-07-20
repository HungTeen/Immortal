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

    private static final HTSimpleRegistry<IRangeNumber<Float>> NUMBERS = HTRegistryManager.createSimple(Util.prefix("player_range_float"));

    public static final IRangeNumber<Float> SPIRITUAL_MANA = register(new PlayerData("spiritual_mana", 0, 0, Float.MAX_VALUE));
    public static final IRangeNumber<Float> MAX_SPIRITUAL_MANA = register(new PlayerData("max_spiritual_mana", 0, 0, Float.MAX_VALUE));

    public static IHTSimpleRegistry<IRangeNumber<Float>> registry() {
        return NUMBERS;
    }

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
