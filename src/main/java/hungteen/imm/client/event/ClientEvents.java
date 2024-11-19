package hungteen.imm.client.event;

import hungteen.imm.api.IMMAPI;
import hungteen.imm.client.ClientHandler;
import hungteen.imm.client.ClientUtil;
import hungteen.imm.client.event.handler.SpellHandler;
import hungteen.imm.client.gui.screen.meditation.MeditationScreen;
import hungteen.imm.client.render.level.ReactionRenderer;
import hungteen.imm.common.ElementManager;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-10 11:09
 **/
@EventBusSubscriber(modid = IMMAPI.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void tick(ClientTickEvent.Post event) {
        if (ClientUtil.player() != null) {
            ClientHandler.onSmithing();
            SpellHandler.tick(ClientUtil.player());
            MeditationScreen.tickMeditation();
            ReactionRenderer.tick();
        }
        Optional.ofNullable(ClientUtil.level()).ifPresent(clientLevel -> {
            if (ClientUtil.mc().isPresent() && !ClientUtil.mc().get().isPaused()) {
                for (Entity entity : clientLevel.entitiesForRendering()) {
                    ElementManager.clientTickElements(clientLevel, entity);
                }
            }
        });
    }

}
