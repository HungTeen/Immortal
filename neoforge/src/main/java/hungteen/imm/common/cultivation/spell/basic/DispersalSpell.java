package hungteen.imm.common.cultivation.spell.basic;

import hungteen.imm.api.spell.SpellCastContext;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/8/18 14:25
 */
public class DispersalSpell extends SpellTypeImpl {

    public DispersalSpell() {
        super("dispersal", property().maxLevel(1).mana(5).cd(100));
    }

    @Override
    public boolean checkActivate(SpellCastContext context) {
        // TODO 神识外放。
        return false;
    }

}
