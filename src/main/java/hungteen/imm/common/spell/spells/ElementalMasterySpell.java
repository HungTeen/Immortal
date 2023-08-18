package hungteen.imm.common.spell.spells;

import com.mojang.datafixers.util.Pair;
import hungteen.htlib.util.WeightedList;
import hungteen.imm.api.enums.Elements;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.common.ElementManager;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.*;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-08-17 22:12
 **/
public class ElementalMasterySpell extends SpellType{

    private static final Map<Elements, ElementalMasterySpell> MASTERY_MAP = new EnumMap<>(Elements.class);
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

    public Elements getElement() {
        return element;
    }

    public static void randomAddElement(Player player, float amount){
        getElementEntry(player, true).ifPresent(e -> e.addElement(player, amount));
    }

    public static void randomAddElement(Player player, Entity target, float amount){
        getElementEntry(player, false).ifPresent(e -> e.addElement(target, amount));
    }

    public static List<Pair<ElementalMasterySpell, Integer>> getMasteryElements(Player player){
        return Arrays.stream(Elements.values())
                .map(ElementalMasterySpell::getSpell)
                .map(s -> Pair.of(s, PlayerUtil.getSpellLevel(player, s)))
                .filter(p -> p.getSecond() > 0).toList();
    }

    public static Optional<ElementEntry> getElementEntry(Player player, boolean self){
        final WeightedList.Builder<ElementEntry> builder = new WeightedList.Builder<>();
        for (Pair<ElementalMasterySpell, Integer> pair : getMasteryElements(player)) {
            if(self){
                if(pair.getSecond() >= 1){
                    builder.add(new ElementEntry(pair.getFirst().getElement(), false, 8));
                }
                if(pair.getSecond() >= 3){
                    builder.add(new ElementEntry(pair.getFirst().getElement(), true, 4));
                }
            } else {
                if(pair.getSecond() >= 4){
                    builder.add(new ElementEntry(pair.getFirst().getElement(), false, 10));
                } else if(pair.getSecond() >= 2){
                    builder.add(new ElementEntry(pair.getFirst().getElement(), false, 5));
                }
                if(pair.getSecond() >= 5){
                    builder.add(new ElementEntry(pair.getFirst().getElement(), true, 5));
                }
            }
        }
        return builder.weight(100).build().getRandomItem(player.getRandom());
    }

    public static ElementalMasterySpell getSpell(Elements element){
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

    record ElementEntry(Elements element, boolean robust, int weight) implements WeightedEntry {

        public void addElement(Entity entity, float amount){
            ElementManager.addElementAmount(entity, element(), robust(), amount);
        }

        @Override
        public Weight getWeight() {
            return Weight.of(weight());
        }
    }

}
