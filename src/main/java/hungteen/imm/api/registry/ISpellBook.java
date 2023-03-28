package hungteen.imm.api.registry;

import hungteen.htlib.api.interfaces.ISimpleEntry;

import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-16 23:35
 **/
public interface ISpellBook extends ISimpleEntry {

    /**
     * Get Spells in this book ordered by spell level.
     * @return the list of spells.
     */
    List<ISpellType> getSpells();

    /**
     * How many spells in this book.
     * @return the number of spells.
     */
    default int getTotalSpellCount(){
        return getSpells().size();
    }
}
