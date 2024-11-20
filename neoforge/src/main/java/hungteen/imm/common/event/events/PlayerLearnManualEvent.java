package hungteen.imm.common.event.events;

import hungteen.imm.common.impl.manuals.SecretManual;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/8/19 15:06
 */
public class PlayerLearnManualEvent extends PlayerEvent {

    private final ItemStack stack;
    private final SecretManual manual;

    public PlayerLearnManualEvent(Player player, ItemStack stack, SecretManual manual) {
        super(player);
        this.stack = stack;
        this.manual = manual;
    }

    public ItemStack getStack() {
        return stack;
    }

    public SecretManual getManual() {
        return manual;
    }
}
