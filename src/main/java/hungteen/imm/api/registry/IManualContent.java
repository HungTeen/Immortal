package hungteen.imm.api.registry;

import net.minecraft.world.entity.player.Player;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/17 15:26
 */
public interface IManualContent {

    void learn(Player player);

    IManualType<?> getType();
}
