package hungteen.imm.client.event;

import hungteen.imm.ImmortalMod;
import hungteen.imm.client.ClientHandler;
import hungteen.imm.client.ClientUtil;
import hungteen.imm.client.event.handler.SpellCircleHandler;
import hungteen.imm.client.gui.screen.meditation.MeditationScreen;
import hungteen.imm.client.render.level.ReactionRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-10 11:09
 **/
@Mod.EventBusSubscriber(modid = ImmortalMod.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void tick(TickEvent.ClientTickEvent event){
        if(event.phase == TickEvent.Phase.END && ClientUtil.player() != null){
            ClientHandler.onSmithing();
            SpellCircleHandler.tickSpellCircle();
            MeditationScreen.tickMeditation();
            ReactionRenderer.tick();
        }
    }

}
