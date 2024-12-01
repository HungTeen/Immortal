package hungteen.imm.common.entity.ai.goal;

import hungteen.imm.api.HTHitResult;
import hungteen.imm.api.entity.SpellCaster;
import hungteen.imm.api.spell.Spell;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-09-03 20:48
 **/
public class UseSpellGoal extends Goal {

    private final SpellCaster spellEntity;

    public UseSpellGoal(SpellCaster spellEntity) {
        this.spellEntity = spellEntity;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if(!this.spellEntity.isOnCoolDown()){
            Spell usingSpell = null;
            final HTHitResult result = this.spellEntity.createHitResult();
            for (Spell spell : this.spellEntity.getSortedSpells()) {
                if(spell.spell().checkActivate(this.spellEntity.self(), result, spell.level())){
                    usingSpell = spell;
                    break;
                }
            }
            if(usingSpell == null){
                this.spellEntity.setCoolDown(20);
            } else {
                this.spellEntity.trigger(usingSpell);
            }
        }
        return false;
    }

}
