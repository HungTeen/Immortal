package hungteen.immortal.common.capability.player;

import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.interfaces.ISpiritualRoot;
import hungteen.immortal.common.event.handler.PlayerEventHandler;
import hungteen.immortal.utils.PlayerUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

import java.util.HashSet;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-24 15:02
 **/
public class PlayerDataManager {

    private final Player player;
    private final HashSet<ISpiritualRoot> spiritualRoots = new HashSet<>();

    public PlayerDataManager(Player player){
        this.player = player;
        PlayerUtil.showPlayerSpiritualRoots(player);
    }

    public CompoundTag saveToNBT() {
        CompoundTag tag = new CompoundTag();
        {
            CompoundTag nbt = new CompoundTag();
            for (ISpiritualRoot spiritualRoot : ImmortalAPI.get().getSpiritualRoots()){
                nbt.putBoolean(spiritualRoot.getName() + "_root", spiritualRoots.contains(spiritualRoot));
            }
            tag.put("SpiritualRoots", nbt);
        }

        return tag;
    }

    public void loadFromNBT(CompoundTag tag) {
        {
            CompoundTag nbt = tag.getCompound("SpiritualRoots");
            for (ISpiritualRoot spiritualRoot : ImmortalAPI.get().getSpiritualRoots()){
                if(nbt.getBoolean(spiritualRoot.getName() + "_root")){
                    spiritualRoots.add(spiritualRoot);
                }
            }
        }
    }

    /**
     * copy player data when clone event happen.
     */
    public void cloneFromExistingPlayerData(PlayerDataManager data, boolean died) {
        this.loadFromNBT(data.saveToNBT());
    }

    /**
     * run when player join world.
     * {@link PlayerEventHandler#onPlayerLogin(Player)}
     */
    public void init() {
        this.syncBounds();
        this.syncToClient();
    }

    /**
     * avoid render out of bound.
     * {@link #init()}
     */
    private void syncBounds() {
//        {// player resources.
//            for(Resources res : Resources.values()) {
//                this.addResource(res, 0);
//            }
//        }
    }

    /**
     * sync data to client side.
     * {@link #init()}
     */
    public void syncToClient() {
//        {// player resources.
//            for(Resources res : Resources.values()) {
//                this.sendResourcePacket(player, res);
//            }
//        }
    }

    /* SpiritualRoots */

    public void addSpiritualRoot(ISpiritualRoot spiritualRoot) {
        this.spiritualRoots.add(spiritualRoot);
    }

    public void removeSpiritualRoot(ISpiritualRoot spiritualRoot) {
        this.spiritualRoots.remove(spiritualRoot);
    }

    public void clearSpiritualRoot() {
        this.spiritualRoots.clear();
    }

    public boolean hasSpiritualRoot(ISpiritualRoot spiritualRoot) {
        return this.spiritualRoots.contains(spiritualRoot);
    }

    public int getSpiritualRootCount() {
        return this.spiritualRoots.size();
    }

    public Player getPlayer(){
        return player;
    }

}
