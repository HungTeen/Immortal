package hungteen.imm.common.impl.manuals;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import hungteen.htlib.api.registry.HTCustomRegistry;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.imm.api.registry.IManualContent;
import hungteen.imm.api.registry.IManualType;
import hungteen.imm.util.Util;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/17 15:19
 */
public interface ManualTypes {

    HTCustomRegistry<IManualType<?>> TYPES = HTRegistryManager.custom(Util.prefix("manual_type"));

    IManualType<LearnSpellManual> LEARN_SPELL = register(new ManualType<>("learn_spell", LearnSpellManual.CODEC));

    static HTCustomRegistry<IManualType<?>> registry(){
        return TYPES;
    }

    static <T extends IManualContent> IManualType<T> register(IManualType<T> type){
        return registry().register(type.getLocation(), type);
    }

    static Codec<IManualContent> getManualCodec() {
        return ManualTypes.registry().byNameCodec().dispatch(IManualContent::getType, IManualType::codec);
    }

    record ManualType<P extends IManualContent>(String name, MapCodec<P> codec) implements IManualType<P> {

        @Override
        public String getModID() {
            return Util.id();
        }
    }

}
