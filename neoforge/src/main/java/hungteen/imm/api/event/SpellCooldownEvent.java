package hungteen.imm.api.event;

import hungteen.imm.api.spell.SpellType;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2025/6/18 16:58
 **/
public class SpellCooldownEvent extends PlayerEvent {

    private final SpellType spell;

    public SpellCooldownEvent(Player player, SpellType spell) {
        super(player);
        this.spell = spell;
    }

    public SpellType getSpell() {
        return spell;
    }
}
