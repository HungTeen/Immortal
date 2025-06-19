package hungteen.imm.common.cultivation.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import hungteen.htlib.api.registry.HTCustomRegistry;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.imm.api.spell.ScrollContent;
import hungteen.imm.api.spell.ScrollType;
import hungteen.imm.common.cultivation.manual.LearnSpellScroll;
import hungteen.imm.util.Util;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/7/17 15:19
 */
public interface ScrollTypes {

    HTCustomRegistry<ScrollType<?>> TYPES = HTRegistryManager.custom(Util.prefix("scroll_type"));

    ScrollType<LearnSpellScroll> LEARN_SPELL = register(new ScrollTypeImpl<>("learn_spell", LearnSpellScroll.CODEC));

    static HTCustomRegistry<ScrollType<?>> registry(){
        return TYPES;
    }

    static <T extends ScrollContent> ScrollType<T> register(ScrollType<T> type){
        return registry().register(type.getLocation(), type);
    }

    static Codec<ScrollContent> getManualCodec() {
        return ScrollTypes.registry().byNameCodec().dispatch(ScrollContent::getType, ScrollType::codec);
    }

    record ScrollTypeImpl<P extends ScrollContent>(String name, MapCodec<P> codec) implements ScrollType<P> {

        @Override
        public String getModID() {
            return Util.id();
        }
    }

}
