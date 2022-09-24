package hungteen.immortal.common.capability.player;

import net.minecraft.world.entity.player.Player;

public interface IPlayerCapability {

    void init(Player player);

    PlayerDataManager get();
}
