package hungteen.imm.client.event;

import hungteen.imm.ImmortalMod;
import hungteen.imm.client.*;
import hungteen.imm.client.event.handler.SpellHandler;
import hungteen.imm.client.gui.screen.meditation.MeditationScreen;
import hungteen.imm.client.render.level.ReactionRenderer;
import hungteen.imm.common.ElementManager;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-10 11:09
 **/
@Mod.EventBusSubscriber(modid = ImmortalMod.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void tick(TickEvent.ClientTickEvent event){
        if(event.phase == TickEvent.Phase.END){
            if(ClientUtil.player() != null){
                ClientHandler.onSmithing();
                SpellHandler.tick(ClientUtil.player());
                MeditationScreen.tickMeditation();
                ReactionRenderer.tick();
            }
            Optional.ofNullable(ClientUtil.level()).ifPresent(clientLevel -> {
                if(ClientUtil.mc().isPresent() && ! ClientUtil.mc().get().isPaused()){
                    for (Entity entity : clientLevel.entitiesForRendering()) {
                        ElementManager.clientTickElements(clientLevel, entity);
                    }
                }
            });
        }
    }

}
