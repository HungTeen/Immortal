package hungteen.immortal.common.capability;

import hungteen.htlib.common.capability.PlayerCapabilityManager;
import hungteen.immortal.ImmortalMod;
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
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-24 14:59
 **/
public class CapabilityHandler {

    public static Capability<PlayerCapability> PLAYER_CAP = CapabilityManager.get(new CapabilityToken<PlayerCapability>() {});

    /**
     * {@link ImmortalMod#ImmortalMod()}
     */
    public static void registerCapabilities(RegisterCapabilitiesEvent event){
        event.register(PlayerCapability.class);
    }

    /**
     * {@link ImmortalMod#ImmortalMod()}
     */
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event){
        if(event.getObject() instanceof Player){
            event.addCapability(Util.prefix("player_data"), new PlayerCapProvider((Player) event.getObject()));
        }
    }

    /**
     * {@link ImmortalMod#setUp(FMLCommonSetupEvent)}
     */
    public static void init(){
        PlayerCapabilityManager.register(PLAYER_CAP);
    }

}
