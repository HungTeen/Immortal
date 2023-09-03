package hungteen.imm.api.records;

import hungteen.imm.api.registry.ISpellType;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-09-03 19:36
 **/
public record Spell(ISpellType spell, int level) {

    public Spell(ISpellType spell){
        this(spell, 1);
    }

}
