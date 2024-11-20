package hungteen.imm.common.entity.ai.behavior;

import hungteen.imm.api.HTHitResult;
import hungteen.imm.api.interfaces.IHasSpell;
import hungteen.imm.api.records.Spell;
import hungteen.imm.common.entity.IMMMob;
import hungteen.imm.common.entity.ai.IMMMemories;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/10/14 21:39
 **/
public class UseSpell {

    public static <E extends IMMMob> BehaviorControl<E> create() {
        return create(ConstantInt.of(0));
    }

    public static <E extends IMMMob> BehaviorControl<E> create(IntProvider cdProvider) {
        return create((mob) -> {
            return true;
        }, cdProvider);
    }

    public static <E extends IMMMob> BehaviorControl<E> create(Predicate<E> predicate, IntProvider cdProvider) {
        return BehaviorBuilder.create(instance -> instance.group(
                instance.absent(IMMMemories.SPELL_COOLING_DOWN.get())
        ).apply(instance, (cd) -> (level, mob, time) -> {
            if (!predicate.test(mob)) {
                // 自身不能触发法术。
                return false;
            } else {
                Spell usingSpell = null;
                final HTHitResult result = mob.createHitResult();
                for (Spell spell : mob.getSortedSpells()) {
                    if(spell.spell().checkActivate(mob, result, spell.level())){
                        usingSpell = spell;
                        break;
                    }
                }
                if(usingSpell != null){
                    mob.trigger(usingSpell);
                    mob.getBrain().setMemoryWithExpiry(IMMMemories.SPELL_COOLING_DOWN.get(), true, usingSpell.spell().getCooldown() + cdProvider.sample(mob.getRandom()));
                    return true;
                }
                mob.getBrain().setMemoryWithExpiry(IMMMemories.SPELL_COOLING_DOWN.get(), true, 20);
                return false;
            }
        }));
    }

}
