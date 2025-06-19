package hungteen.imm.api.event;

import hungteen.imm.api.spell.Spell;
import hungteen.imm.api.spell.SpellCastContext;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

/**
 * 触发法术事件。
 * @author HungTeen
 * @program Immortal
 * @create 2022-10-02 15:06
 **/
public class ActivateSpellEvent extends LivingEvent {

    private final Spell spell;
    private final SpellCastContext context;

    public ActivateSpellEvent(LivingEntity entity, Spell spell, SpellCastContext context) {
        super(entity);
        this.spell = spell;
        this.context = context;
    }

    public Spell getSpell() {
        return spell;
    }

    /**
     * hasRecipe can trigger or not.
     */
    public static final class Pre extends ActivateSpellEvent implements ICancellableEvent {

        public Pre(LivingEntity living, Spell spell, SpellCastContext context) {
            super(living, spell, context);
        }

    }

    /**
     * triggered already.
     */
    public static final class Post extends ActivateSpellEvent {


        public Post(LivingEntity living, Spell spell, SpellCastContext context) {
            super(living, spell, context);
        }

    }

}
