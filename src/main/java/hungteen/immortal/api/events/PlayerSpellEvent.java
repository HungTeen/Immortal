package hungteen.immortal.api.events;

import hungteen.immortal.api.registry.ISpellType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-02 15:06
 **/
public class PlayerSpellEvent extends PlayerEvent {

    private final ISpellType spell;
    private final int level;

    public PlayerSpellEvent(Player player, ISpellType spell, int level) {
        super(player);
        this.spell = spell;
        this.level = level;
    }

    public ISpellType getSpell() {
        return spell;
    }

    public int getLevel() {
        return level;
    }

    /**
     * trigger when spell is activated.
     */
    public static class ActivateSpellEvent extends PlayerSpellEvent {

        public ActivateSpellEvent(Player player, ISpellType spell, int level) {
            super(player, spell, level);
        }

        /**
         * hasRecipe can trigger or not.
         */
        @Cancelable
        public static final class Pre extends ActivateSpellEvent {

            public Pre(Player player, ISpellType spell, int level) {
                super(player, spell, level);
            }

        }

        /**
         * triggered already.
         */
        public static final class Post extends ActivateSpellEvent {

            public Post(Player player, ISpellType spell, int level) {
                super(player, spell, level);
            }

        }
    }

    /**
     * using spell after activated.
     * Server Side Only.
     */
    @Cancelable
    public static final class UsingSpellEvent extends PlayerSpellEvent {

        public UsingSpellEvent(Player player, ISpellType spell, int level) {
            super(player, spell, level);
        }
    }

}
