package hungteen.immortal.common.item.runes;

import hungteen.immortal.api.registry.IGetterRune;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-06 16:29
 **/
public class GetterRuneItem extends RuneItem {

    private final IGetterRune getterRune;

    public GetterRuneItem(final IGetterRune getterRune) {
        this.getterRune = getterRune;
    }

    public IGetterRune getGetterRune() {
        return getterRune;
    }
}
