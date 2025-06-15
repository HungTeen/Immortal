package hungteen.imm.common.event.events;

import hungteen.imm.common.impl.manuals.SecretScroll;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/8/19 15:06
 */
public class PlayerLearnManualEvent extends PlayerEvent {

    private final ItemStack stack;
    private final SecretScroll scroll;

    public PlayerLearnManualEvent(Player player, ItemStack stack, SecretScroll scroll) {
        super(player);
        this.stack = stack;
        this.scroll = scroll;
    }

    public ItemStack getStack() {
        return stack;
    }

    public SecretScroll getScroll() {
        return scroll;
    }
}
