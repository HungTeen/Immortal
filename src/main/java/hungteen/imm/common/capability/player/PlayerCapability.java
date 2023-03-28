package hungteen.imm.common.capability.player;

import hungteen.htlib.common.capability.player.HTPlayerCapability;
import net.minecraft.world.entity.player.Player;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-24 15:03
 **/
public class PlayerCapability extends HTPlayerCapability<PlayerDataManager> {

    @Override
    public void init(Player player) {
        if(this.dataManager == null){
            this.dataManager = new PlayerDataManager(player);
        }
    }

}
