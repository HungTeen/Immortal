package hungteen.imm.api.events;

import hungteen.imm.api.spell.SpellType;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-02 15:06
 **/
public class PlayerSpellEvent extends PlayerEvent {

    private final SpellType spell;
    private final int level;

    public PlayerSpellEvent(Player player, SpellType spell, int level) {
        super(player);
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
     * trigger when spell is activated.
     */
    public static class ActivateSpellEvent extends PlayerSpellEvent {

        public ActivateSpellEvent(Player player, SpellType spell, int level) {
            super(player, spell, level);
        }

        /**
         * hasRecipe can trigger or not.
         */
        public static final class Pre extends ActivateSpellEvent implements ICancellableEvent {

            public Pre(Player player, SpellType spell, int level) {
                super(player, spell, level);
            }

        }

        /**
         * triggered already.
         */
        public static final class Post extends ActivateSpellEvent {


            public Post(Player player, SpellType spell, int level) {
                super(player, spell, level);
            }

        }
    }

}
