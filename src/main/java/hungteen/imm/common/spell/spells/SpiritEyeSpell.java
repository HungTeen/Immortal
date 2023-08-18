package hungteen.imm.common.spell.spells;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/8/18 15:26
 */
public class SpiritEyeSpell extends SpellType {

    public SpiritEyeSpell() {
        super("spirit_eyes", properties().maxLevel(2).notTrigger());
    }

}
