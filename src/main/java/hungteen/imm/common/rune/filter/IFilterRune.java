package hungteen.imm.common.rune.filter;

import hungteen.htlib.util.helper.CodecHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.function.Predicate;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-04 21:38
 **/
public interface IFilterRune {

    default MutableComponent getFilterText(){
        return Component.literal(CodecHelper.encodeNbt(FilterRuneTypes.getCodec(), this)
                .result().orElseThrow().getAsString());
    }

    <T> Predicate<T> getPredicate(Class<?> clazz, Predicate<T> predicate);

    IFilterRuneType<?> getType();
}
