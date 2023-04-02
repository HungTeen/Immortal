package hungteen.imm.common.item.runes;

import hungteen.imm.common.rune.behavior.IBehaviorRune;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-02 22:36
 **/
public class BehaviorRuneItem extends RuneItem {

    private final IBehaviorRune behaviorRune;

    public BehaviorRuneItem(IBehaviorRune behaviorRune) {
        this.behaviorRune = behaviorRune;
    }

    public IBehaviorRune getBehaviorRune() {
        return behaviorRune;
    }
}
