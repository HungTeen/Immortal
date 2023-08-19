package hungteen.imm.api.interfaces;

import hungteen.imm.api.registry.ISpellType;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/8/19 15:49
 */
public interface IHasSpell {

    int getSpellLevel(ISpellType spell);

    default boolean hasLearnedSpell(ISpellType spell, int level){
        return getSpellLevel(spell) >= level;
    }

}
