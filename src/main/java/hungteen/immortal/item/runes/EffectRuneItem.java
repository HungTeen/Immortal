package hungteen.immortal.item.runes;

import hungteen.immortal.api.interfaces.IEffectRune;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-06 18:29
 **/
public class EffectRuneItem extends RuneItem{

    private final IEffectRune effectRune;

    public EffectRuneItem(IEffectRune effectRune) {
        this.effectRune = effectRune;
    }

    public IEffectRune getEffectRune() {
        return effectRune;
    }

}
