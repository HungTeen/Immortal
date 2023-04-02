package hungteen.imm.common.item.runes;

import hungteen.imm.common.rune.memory.IMemoryRune;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-02 22:34
 **/
public class MemoryRuneItem extends RuneItem{

    private final IMemoryRune memoryRune;

    public MemoryRuneItem(IMemoryRune memoryRune) {
        this.memoryRune = memoryRune;
    }

    public IMemoryRune getMemoryRune() {
        return memoryRune;
    }
}
