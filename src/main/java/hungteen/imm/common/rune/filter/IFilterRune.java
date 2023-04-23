package hungteen.imm.common.rune.filter;

import hungteen.imm.common.item.runes.info.FilterRuneItem;
import hungteen.imm.util.TipUtil;
import net.minecraft.network.chat.MutableComponent;

import java.util.function.Predicate;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-04 21:38
 **/
public interface IFilterRune {

    MutableComponent UNKNOWN_COMPONENT = TipUtil.rune("unknown_rune_data");

    MutableComponent getFilterText();

    <T> Predicate<T> getPredicate(FilterRuneItem<?> item, Predicate<T> predicate);

    IFilterRuneType<?> getType();
}
