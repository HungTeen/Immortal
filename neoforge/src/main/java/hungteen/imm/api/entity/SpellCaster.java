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
        final Map<SpellUsageCategory, Integer> priorityMap = getCategoryPriority();
        return getNonPassiveSpells().filter(this::canUseSpell).sorted((spell1, spell2) -> {
            final int result = priorityMap.getOrDefault(spell1.getCategory(), 0) - priorityMap.getOrDefault(spell2.getCategory(), 0);
            if(result != 0) return result;
            return prefer(spell1, spell2);
        }).map(type -> new Spell(type, getSpellLevel(type))).toList();
    }

    /**
     * 同优先级下更倾向于使用哪个。
     */
    default int prefer(SpellType spell1, SpellType spell2){
        return 0;
    }

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
