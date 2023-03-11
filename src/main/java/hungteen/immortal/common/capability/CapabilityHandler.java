package hungteen.immortal.common.capability;

import hungteen.htlib.common.capability.PlayerCapabilityManager;
import hungteen.immortal.ImmortalMod;
import hungteen.immortal.common.capability.chunk.ChunkCapProvider;
import hungteen.immortal.common.capability.chunk.ChunkCapability;
import hungteen.immortal.common.capability.entity.EntityCapProvider;
import hungteen.immortal.common.capability.entity.EntitySpiritualCapability;
import hungteen.immortal.common.capability.player.PlayerCapProvider;
import hungteen.immortal.common.capability.player.PlayerCapability;
import hungteen.immortal.utils.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-24 14:59
 **/
public class CapabilityHandler {

    public static Capability<PlayerCapability> PLAYER_CAP = CapabilityManager.get(new CapabilityToken<>() {});
    public static Capability<EntitySpiritualCapability> ENTITY_CAP = CapabilityManager.get(new CapabilityToken<>() {});
    public static Capability<ChunkCapability> CHUNK_CAP = CapabilityManager.get(new CapabilityToken<>() {});

    /**
     * {@link ImmortalMod#ImmortalMod()}
     */
    public static void registerCapabilities(RegisterCapabilitiesEvent event){
        event.register(PlayerCapability.class);
    }

    /**
     * {@link ImmortalMod#ImmortalMod()}
     */
    public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event){
        if(event.getObject() instanceof Player){
            event.addCapability(Util.prefix("player_data"), new PlayerCapProvider((Player) event.getObject()));
        }
        event.addCapability(Util.prefix("spiritual_data"), new EntityCapProvider(event.getObject()));
    }

    /**
     * {@link ImmortalMod#ImmortalMod()}
     */
    public static void attachChunkCapabilities(AttachCapabilitiesEvent<LevelChunk> event){
        event.addCapability(Util.prefix("spiritual_data"), new ChunkCapProvider(event.getObject()));
    }

    /**
     * {@link ImmortalMod#setUp(FMLCommonSetupEvent)}
     */
    public static void init(){
        PlayerCapabilityManager.register(PLAYER_CAP);
    }

}
