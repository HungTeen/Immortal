package hungteen.imm.common.spell.spells;

import hungteen.imm.api.enums.Elements;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.common.ElementManager;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

import java.util.EnumMap;
import java.util.Map;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-08-17 22:12
 **/
public class ElementalMasterySpell extends SpellType{

    private static final Map<Elements, ISpellType> MASTERY_MAP = new EnumMap<>(Elements.class);
    private final Elements element;

    public ElementalMasterySpell(Elements element) {
        super(element.name().toLowerCase() + "_mastery", properties().maxLevel(5).notTrigger());
        this.element = element;
        MASTERY_MAP.put(element, this);
    }

    @Override
    public MutableComponent getComponent() {
        return TipUtil.spell("elemental_mastery", ElementManager.name(element).getString());
    }

    @Override
    public MutableComponent getSpellDesc(int level) {
        return TipUtil.spell("elemental_mastery_" + level, ElementManager.name(element).getString());
    }

    public static ISpellType getSpell(Elements element){
        return MASTERY_MAP.get(element);
    }

    public static int requireEMP(Player player, int level){
        final int cnt = (int) MASTERY_MAP.entrySet().stream().filter(entry -> {
            return PlayerUtil.hasLearnedSpell(player, entry.getValue(), level);
        }).count();
        return cnt == 0 ? (1 << level - 1) : cnt * getLevelStart(level);
    }

    private static int getLevelStart(int level){
        return level == 1 ? 4 : level == 2 ? 6 : level == 3 ? 10 : level == 4 ? 16 : 25;
    }

}
