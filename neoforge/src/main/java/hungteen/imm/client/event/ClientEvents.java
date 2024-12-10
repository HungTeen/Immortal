package hungteen.imm.client.event;

import hungteen.imm.api.IMMAPI;
import hungteen.imm.client.ClientHandler;
import hungteen.imm.client.data.SpellClientData;
import hungteen.imm.client.gui.screen.meditation.MeditationScreen;
import hungteen.imm.client.render.level.ReactionRenderer;
import hungteen.imm.client.util.ClientUtil;
import hungteen.imm.common.cultivation.ElementManager;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.Optional;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-10 11:09
 **/
@EventBusSubscriber(modid = IMMAPI.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void tick(ClientTickEvent.Post event) {
        ClientUtil.playerOpt().ifPresent(player -> {
//            ClientHandler.onSmithing();
            SpellClientData.tick(player);
            MeditationScreen.tickMeditation(player);
            ReactionRenderer.tick();
        });
        Optional.ofNullable(ClientUtil.level()).ifPresent(clientLevel -> {
            if (!ClientUtil.mc().isPaused()) {
                for (Entity entity : clientLevel.entitiesForRendering()) {
                    ElementManager.clientTickElements(clientLevel, entity);
                }
            }
        });
    }

    @SubscribeEvent
    public static void playerLogin(PlayerEvent.PlayerLoggedInEvent event){
        ClientHandler.registerCultivatorTypes();
    }

}
