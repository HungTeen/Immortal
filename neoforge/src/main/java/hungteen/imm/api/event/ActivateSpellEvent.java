package hungteen.imm.api.event;

import hungteen.imm.api.spell.SpellType;
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

    private final SpellType spell;
    private final int level;

    public ActivateSpellEvent(LivingEntity entity, SpellType spell, int level) {
        super(entity);
        this.spell = spell;
        this.level = level;
    }

    public SpellType getSpell() {
        return spell;
    }

    public int getLevel() {
        return level;
    }

    /**
     * hasRecipe can trigger or not.
     */
    public static final class Pre extends ActivateSpellEvent implements ICancellableEvent {

        public Pre(LivingEntity living, SpellType spell, int level) {
            super(living, spell, level);
        }

    }

    /**
     * triggered already.
     */
    public static final class Post extends ActivateSpellEvent {


        public Post(LivingEntity living, SpellType spell, int level) {
            super(living, spell, level);
        }

    }

}
