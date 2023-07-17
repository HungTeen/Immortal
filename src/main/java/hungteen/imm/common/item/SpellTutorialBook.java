package hungteen.imm.common.item;

import hungteen.imm.api.registry.ISpellType;
import net.minecraft.world.item.Item;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-11 23:01
 **/
public class SpellTutorialBook extends Item {

    private final ISpellType spell;

    public SpellTutorialBook(ISpellType spell) {
        super(new Properties().stacksTo(1));
        this.spell = spell;
    }


}
