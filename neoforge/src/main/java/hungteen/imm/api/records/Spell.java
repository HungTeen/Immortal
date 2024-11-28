package hungteen.imm.api.records;

import hungteen.imm.api.spell.SpellType;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-09-03 19:36
 **/
public record Spell(SpellType spell, int level) {

    public static Spell create(SpellType spell){
        return create(spell, 1);
    }

    public static Spell create(SpellType spell, int level){
        return new Spell(spell, level);
    }

}
