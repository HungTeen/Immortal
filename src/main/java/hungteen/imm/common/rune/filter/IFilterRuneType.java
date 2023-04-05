package hungteen.imm.common.rune.filter;

import com.mojang.serialization.Codec;
import hungteen.htlib.api.interfaces.ISimpleEntry;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-04 21:37
 **/
public interface IFilterRuneType<P extends IFilterRune> extends ISimpleEntry {

    Codec<P> codec();


}
