package hungteen.imm.api.entity;

import com.mojang.datafixers.util.Pair;
import hungteen.imm.api.spell.Spell;
import hungteen.imm.api.spell.SpellCastContext;
import hungteen.imm.api.spell.SpellType;
import hungteen.imm.common.cultivation.SpellManager;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

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
     * 获取能够主动触发的法术，并根据实体的具体情况为其设置优先级，优先级小于 0 的法术不会被触发。<br>
     * 对于可以触发的法术，会根据优先级进行一个随机，优先级为100的法术必触发，优先级为 0 的法术有 10% 的概率触发。<br>
     */
    static Spell randomChooseWithPriority(SpellCaster caster){
        List<Pair<Spell, Integer>> candidateSpells = caster.getLearnedSpellTypes().stream()
                .filter(SpellManager::canMobTrigger)
                .filter(caster::canUseSpell)
                .map(type -> Pair.of(new Spell(type, caster.getSpellLevel(type)), caster.getSpellUsePriority(type)))
                .sorted((a, b) -> b.getSecond() - a.getSecond())
                .filter(pair -> pair.getSecond() >= 0)
                .toList();
        for (var pair : candidateSpells) {
            Spell spell = pair.getFirst();
            int priority = pair.getSecond();
            SpellCastContext context = new SpellCastContext(caster.self(), spell.level());
            caster.fillSpellContext(context);
            if(caster.self().getRandom().nextInt(10) < Math.clamp(priority, 1, priority / 10) && spell.spell().checkActivate(context)){
                return spell;
            }
        }
        return null;
    }

    /**
     * 优先级大的在前。
     */
    default int getSpellUsePriority(SpellType spell){
        return spell.getCastingPriority(self());
    }

    /**
     * @return 是否有足够的灵气使用法术。
     */
    default boolean canUseSpell(SpellType spell){
        return getQiAmount() >= spell.getConsumeQi();
    }

    /**
     * 学习法术。
     */
    void learnSpell(SpellType spell, int level);

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
     * 补充法术释放所需的上下文信息。
     */
    void fillSpellContext(SpellCastContext context);

    /**
     * @return 获取实体自身。
     */
    Mob self();

}
