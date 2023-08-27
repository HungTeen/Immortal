package hungteen.imm.common.spell.spells;

import com.mojang.datafixers.util.Pair;
import hungteen.htlib.util.WeightedList;
import hungteen.imm.api.enums.Elements;
import hungteen.imm.common.ElementManager;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.*;
import java.util.function.Consumer;

/**
 * Lvl 1: Attach target weakly.
 * Lvl 2: Attach self weakly.
 * Lvl 3: Attach target weakly with more amount.
 * Lvl 4: Attach self strongly.
 * Lvl 5: Attach target strongly.
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-08-17 22:12
 **/
public class ElementalMasterySpell extends SpellType{

    private static final int ELEMENTAL_MASTERY_MAX_LEVEL = 5;
    private static final Map<Elements, ElementalMasterySpell> MASTERY_MAP = new EnumMap<>(Elements.class);
    private final Elements element;

    public ElementalMasterySpell(Elements element) {
        super(element.name().toLowerCase() + "_mastery", properties().maxLevel(ELEMENTAL_MASTERY_MAX_LEVEL).notTrigger());
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

    public static List<Pair<ElementalMasterySpell, Integer>> getMasteryElements(Player player){
        return Arrays.stream(Elements.values())
                .map(ElementalMasterySpell::getSpell)
                .map(s -> Pair.of(s, PlayerUtil.getSpellLevel(player, s)))
                .filter(p -> p.getSecond() > 0).toList();
    }

    /**
     * 未指定元素，则完全随机。
     */
    public static void addWeakElement(Entity entity, boolean self, boolean must, float amount){
        addWeakElement(entity, Arrays.stream(Elements.values()).toList(), self, must, amount);
    }

    /**
     * 指定一个元素。
     */
    public static void addWeakElement(Entity entity, Elements element, boolean self, boolean must, float amount){
        addWeakElement(entity, List.of(element), self, must, amount);
    }

    public static void addWeakElement(Entity entity, List<Elements> elements, boolean self, boolean must, float amount){
        getElementEntry(entity, elements, self, must, false, true).ifPresent(e -> e.addElement(entity, amount));
    }

    /**
     * 未指定元素，则完全随机。
     */
    public static void addRobustElement(Entity entity, boolean self, boolean must, float amount){
        addRobustElement(entity, Arrays.stream(Elements.values()).toList(), self, must, amount);
    }

    /**
     * 指定一个元素。
     */
    public static void addRobustElement(Entity entity, Elements element, boolean self, boolean must, float amount){
        addRobustElement(entity, List.of(element), self, must, amount);
    }

    public static void addRobustElement(Entity entity, List<Elements> elements, boolean self, boolean must, float amount){
        getElementEntry(entity, elements, self, must, true, false).ifPresent(e -> e.addElement(entity, amount));
    }

    /**
     * 未指定元素，则完全随机。
     */
    public static void addElement(Entity entity, boolean self, boolean must, float amount){
        addElement(entity, Arrays.stream(Elements.values()).toList(), self, must, amount);
    }

    /**
     * 指定一个元素。
     */
    public static void addElement(Entity entity, Elements element, boolean self, boolean must, float amount){
        addElement(entity, List.of(element), self, must, amount);
    }

    public static void addElement(Entity entity, List<Elements> elements, boolean self, boolean must, float amount){
        getElementEntry(entity, elements, self, must, true, true).ifPresent(e -> e.addElement(entity, amount));
    }

    /**
     * 获取此次效果的附着结果。
     * @param entity 发起者。
     * @param elements 指定附着的元素有哪些。
     * @param self 对自身附着，还是对目标。
     * @param must 一定会选中。
     * @param robust 有强元素。
     * @param weak 有弱元素。
     * @return 随机结果。
     */
    public static Optional<ElementEntry> getElementEntry(Entity entity, List<Elements> elements, boolean self, boolean must, boolean robust, boolean weak){
        final WeightedList.Builder<ElementEntry> builder = new WeightedList.Builder<>();
        elements.forEach(element -> {
            applyElementEntry(element, EntityUtil.getSpellLevel(entity, getSpell(element)), self, robust, weak).accept(builder);
        });
        if(! must){
            builder.weight(1000);
        }
        return builder.build().getRandomItem(entity.level().getRandom());
    }

    private static Consumer<WeightedList.Builder<ElementEntry>> applyElementEntry(Elements element, int level, boolean self, boolean robust, boolean weak){
        return builder -> {
            if (self) {
                if (level >= 2) {
                    builder.add(new ElementEntry(element, false, (int) (50 * element.getAttachFactor())));
                }
                if (robust && level >= 3) {
                    builder.add(new ElementEntry(element, true, (int) ((level >= 4 ? 25 : 5) * element.getAttachFactor())));
                }
            } else {
                if(weak && level >= 1){
                    builder.add(new ElementEntry(element, false, (int) (50 * element.getAttachFactor())));
                }
                if (robust && level >= 3) {
                    builder.add(new ElementEntry(element, true, (int) ((level >= 5 ? 15 : 3) * element.getAttachFactor())));
                }
            }
        };
    }

    public static ElementalMasterySpell getSpell(Elements element){
        return MASTERY_MAP.get(element);
    }

    public static int requireEMP(Player player, int level){
        return requireEMP(level, (int) MASTERY_MAP.entrySet().stream().filter(entry -> {
            return PlayerUtil.hasLearnedSpell(player, entry.getValue(), level);
        }).count());
    }

    /**
     * 计算当前需要多少元素精通点。
     * @param level 对应精通等级。
     * @param cnt 已经精通了多少同级元素。
     * @return EMP。
     */
    private static int requireEMP(int level, int cnt){
        return cnt == 0 ? (1 << level - 1) : cnt * getLevelStart(level);
    }

    /**
     * 玩家已经使用了多少EMP，根据精通反推。
     */
    public static int calculateUsedEMP(Player player){
        int emp = 0;
        List<Pair<ElementalMasterySpell, Integer>> spells = getMasteryElements(player);
        for(int i = 1; i <= ELEMENTAL_MASTERY_MAX_LEVEL; ++ i){
            int now = 0;
            for (Pair<ElementalMasterySpell, Integer> pair : spells) {
                if(pair.getSecond() >= i){
                    emp += requireEMP(i, now ++);
                }
            }
        }
        return emp;
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
