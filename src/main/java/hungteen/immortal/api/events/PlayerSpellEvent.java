package hungteen.immortal.api.events;

import hungteen.immortal.api.interfaces.ISpell;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-02 15:06
 **/
public class PlayerSpellEvent extends PlayerEvent {

    private final ISpell spell;
    private final int level;

    public PlayerSpellEvent(Player player, ISpell spell, int level) {
        super(player);
        this.spell = spell;
        this.level = level;
    }

    public ISpell getSpell() {
        return spell;
    }

    public int getLevel() {
        return level;
    }

    /**
     * trigger when spell is activated.
     * Server Side Only.
     */
    @Cancelable
    public static final class ActivateSpellEvent extends PlayerSpellEvent {

        public ActivateSpellEvent(Player player, ISpell spell, int level) {
            super(player, spell, level);
        }
    }

    /**
     * using spell after activated.
     * Server Side Only.
     */
    @Cancelable
    public static final class UsingSpellEvent extends PlayerSpellEvent {

        public UsingSpellEvent(Player player, ISpell spell, int level) {
            super(player, spell, level);
        }
    }

}
