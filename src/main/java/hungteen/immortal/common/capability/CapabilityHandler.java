package hungteen.immortal.common.capability;

import hungteen.immortal.Immortal;
import hungteen.immortal.common.capability.player.PlayerCapProvider;
import hungteen.immortal.common.capability.player.PlayerCapability;
import hungteen.immortal.utils.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-24 14:59
 **/
public class CapabilityHandler {

    public static Capability<PlayerCapability> PLAYER_CAP = CapabilityManager.get(new CapabilityToken<PlayerCapability>() {});

    /**
     * {@link Immortal#Immortal()}
     */
    public static void registerCapabilities(RegisterCapabilitiesEvent event){
        event.register(PlayerCapability.class);
    }

    /**
     * {@link Immortal#Immortal()}
     */
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event){
        if(event.getObject() instanceof Player){
            event.addCapability(Util.prefix("player_data"), new PlayerCapProvider((Player) event.getObject()));
        }
    }

    public static LazyOptional<PlayerCapability> getPlayerData(Player player){
        return player.getCapability(PLAYER_CAP);
    }
}
