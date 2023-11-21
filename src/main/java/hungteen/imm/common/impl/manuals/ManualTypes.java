package hungteen.imm.common.impl.manuals;

import com.mojang.serialization.Codec;
import hungteen.htlib.api.interfaces.IHTSimpleRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.imm.api.registry.IManualContent;
import hungteen.imm.api.registry.IManualType;
import hungteen.imm.util.Util;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/17 15:19
 */
public interface ManualTypes {

    IHTSimpleRegistry<IManualType<?>> TYPES = HTRegistryManager.createSimple(Util.prefix("manual_type"));

    IManualType<LearnSpellManual> LEARN_SPELL = register(new ManualType<>("learn_spell", LearnSpellManual.CODEC));

    static IHTSimpleRegistry<IManualType<?>> registry(){
        return TYPES;
    }

    static <T extends IManualContent> IManualType<T> register(IManualType<T> type){
        return registry().register(type);
    }

    static Codec<IManualContent> getManualCodec() {
        return ManualTypes.registry().byNameCodec().dispatch(IManualContent::getType, IManualType::codec);
    }

    record ManualType<P extends IManualContent>(String name, Codec<P> codec) implements IManualType<P> {

        @Override
        public String getName() {
            return name();
        }

        @Override
        public String getModID() {
            return Util.id();
        }
    }

}
