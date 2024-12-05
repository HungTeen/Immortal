package hungteen.imm.api.entity;

import hungteen.imm.api.HTHitResult;
import hungteen.imm.api.spell.Spell;
import hungteen.imm.api.spell.SpellType;
import hungteen.imm.api.spell.SpellUsageCategory;
import hungteen.imm.common.cultivation.SpellManager;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * 使用法术的生物。
 * @author PangTeen
 * @program Immortal
 * @create 2023/8/19 15:49
 */
public interface SpellCaster extends HasQi {

    int VERY_HIGH = 100;
    int HIGH = 50;
    int LITTLE_HIGH = 10;
    int DEFAULT = 0;
    int LITTLE_LOW = -10;
    int LOW = -50;
    int VERY_LOW = -100;

    /**
     * @return 法术是否在冷却之中。
     */
    boolean isOnCoolDown();

    /**
     * @param coolDown 法术冷却时间。
     */
    void setCoolDown(int coolDown);

    /**
     * @param spell 使用的法术。
     */
    void trigger(@NotNull Spell spell);

    /**
     * @return 法术的学习等级。
     */
    int getSpellLevel(SpellType spell);

    /**
     * 获取当前法术使用优先级。
     */
    default Map<SpellUsageCategory, Integer> getCategoryPriority(){
        return Map.of();
    }

    /**
     * 获取优先级排序后的可用法术。
     */
    default List<Spell> getSortedSpells(){
        return getNonPassiveSpells().filter(this::canUseSpell).sorted((spell1, spell2) -> {
            return getSpellUsePriority(spell2) - getSpellUsePriority(spell1);
        }).map(type -> new Spell(type, getSpellLevel(type))).toList();
    }

    /**
     * 优先级大的在前。
     */
    int getSpellUsePriority(SpellType spell);

    /**
     * @return 是否有足够的灵气使用法术。
     */
    default boolean canUseSpell(SpellType spell){
        return getQiAmount() >= spell.getConsumeMana();
    }

    /**
     * @return 是否学习过指定法术。
     */
    default boolean hasLearnedSpell(SpellType spell, int level){
        return getSpellLevel(spell) >= level;
    }

    /**
     * @return 已学习的法术类型。
     */
    Set<SpellType> getLearnedSpellTypes();

    /**
     * @return 获取能够主动触发的法术。
     */
    default Stream<SpellType> getNonPassiveSpells(){
        return getLearnedSpellTypes().stream().filter(SpellManager::canMobTrigger);
    }

    /**
     * @return 获取法术释放所需的环境信息。
     */
    HTHitResult createHitResult();

    Mob self();

}
