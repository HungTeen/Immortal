package hungteen.imm.api.interfaces;

import hungteen.imm.api.HTHitResult;
import hungteen.imm.api.enums.SpellCategories;
import hungteen.imm.api.records.Spell;
import hungteen.imm.api.registry.ISpellType;
import net.minecraft.world.entity.Mob;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/8/19 15:49
 */
public interface IHasSpell extends IHasMana {

    boolean isOnCoolDown();

    void trigger(@Nullable Spell spell);

    int getSpellLevel(ISpellType spell);

    /**
     * 获取当前法术使用优先级。
     */
    default Map<SpellCategories, Integer> getCategoryPriority(){
        return Map.of();
    }

    /**
     * 获取优先级排序后的可用法术。
     */
    default List<Spell> getSortedSpells(){
        final Map<SpellCategories, Integer> priorityMap = getCategoryPriority();
        return getLearnedSpellTypes().stream().filter(this::canUseSpell).sorted((spell1, spell2) -> {
            final int result = priorityMap.getOrDefault(spell1.getCategory(), 0) - priorityMap.getOrDefault(spell2.getCategory(), 0);
            if(result != 0) return result;
            return prefer(spell1, spell2);
        }).map(type -> new Spell(type, getSpellLevel(type))).toList();
    }

    /**
     * 同优先级下更倾向于使用哪个。
     */
    default int prefer(ISpellType spell1, ISpellType spell2){
        return 0;
    }

    default boolean canUseSpell(ISpellType spell){
        return getMana() >= spell.getConsumeMana();
    }

    default boolean hasLearnedSpell(ISpellType spell, int level){
        return getSpellLevel(spell) >= level;
    }

    Set<ISpellType> getLearnedSpellTypes();

    HTHitResult createHitResult();

    Mob self();

}
