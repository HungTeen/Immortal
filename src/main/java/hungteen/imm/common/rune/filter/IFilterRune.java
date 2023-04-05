package hungteen.imm.common.rune.filter;

import java.util.function.Predicate;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-04 21:38
 **/
public interface IFilterRune {

    <T> Predicate<T> getPredicate(Class<?> clazz, Predicate<T> predicate);

    IFilterRuneType<?> getType();
}
