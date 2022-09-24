package hungteen.immortal.common.capability.player;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-24 15:02
 **/
public class PlayerDataManager {

    private final Player player;
//    private final HashSet<>

    public PlayerDataManager(Player player){
        this.player = player;
    }

    public Player getPlayer(){
        return player;
    }

    public CompoundTag saveToNBT() {
        return new CompoundTag();
    }

    public void loadFromNBT(CompoundTag nbt) {

    }

}
