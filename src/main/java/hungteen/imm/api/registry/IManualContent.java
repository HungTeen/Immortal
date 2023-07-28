package hungteen.imm.api.registry;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/17 15:26
 */
public interface IManualContent {

    void learn(Player player);

    List<Component> getInfo();

    IManualType<?> getType();
}
