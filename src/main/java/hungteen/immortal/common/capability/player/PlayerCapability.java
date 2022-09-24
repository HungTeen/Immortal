package hungteen.immortal.common.capability.player;

import net.minecraft.world.entity.player.Player;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-24 15:03
 **/
public class PlayerCapability implements IPlayerCapability{

    private PlayerDataManager playerDataManager = null;

    @Override
    public void init(Player player) {
        if(playerDataManager == null){
            playerDataManager = new PlayerDataManager(player);
        }
    }

    @Override
    public PlayerDataManager get() {
        return playerDataManager;
    }
}
