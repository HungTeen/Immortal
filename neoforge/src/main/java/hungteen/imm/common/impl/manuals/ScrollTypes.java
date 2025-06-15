package hungteen.imm.common.impl.manuals;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import hungteen.htlib.api.registry.HTCustomRegistry;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.imm.api.spell.IScrollContent;
import hungteen.imm.api.spell.IScrollType;
import hungteen.imm.util.Util;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/7/17 15:19
 */
public interface ScrollTypes {

    HTCustomRegistry<IScrollType<?>> TYPES = HTRegistryManager.custom(Util.prefix("scroll_type"));

    IScrollType<LearnSpellScroll> LEARN_SPELL = register(new ManualType<>("learn_spell", LearnSpellScroll.CODEC));

    static HTCustomRegistry<IScrollType<?>> registry(){
        return TYPES;
    }

    static <T extends IScrollContent> IScrollType<T> register(IScrollType<T> type){
        return registry().register(type.getLocation(), type);
    }

    static Codec<IScrollContent> getManualCodec() {
        return ScrollTypes.registry().byNameCodec().dispatch(IScrollContent::getType, IScrollType::codec);
    }

    record ManualType<P extends IScrollContent>(String name, MapCodec<P> codec) implements IScrollType<P> {

        @Override
        public String getModID() {
            return Util.id();
        }
    }

}
