package hungteen.imm.api.registry;

import hungteen.htlib.api.registry.SimpleEntry;

import java.util.List;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-11-16 23:35
 **/
public interface ISpellBook extends SimpleEntry {

    /**
     * Get Spells in this book ordered by spell level.
     * @return the list of entries.
     */
    List<ISpellType> getSpells();

    /**
     * How many entries in this book.
     * @return the number of entries.
     */
    default int getTotalSpellCount(){
        return getSpells().size();
    }
}
