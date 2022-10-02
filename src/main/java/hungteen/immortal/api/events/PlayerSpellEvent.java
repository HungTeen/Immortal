package hungteen.immortal.api.events;

import hungteen.immortal.api.interfaces.ISpell;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-02 15:06
 **/
public class PlayerSpellEvent extends PlayerEvent {

    private final ISpell spell;

    public PlayerSpellEvent(Player player, ISpell spell) {
        super(player);
        this.spell = spell;
    }

    /**
     * trigger when spell is activated.
     */
    public static final class ActivateSpellEvent extends PlayerSpellEvent {

        public ActivateSpellEvent(Player player, ISpell spell) {
            super(player, spell);
        }
    }

}
