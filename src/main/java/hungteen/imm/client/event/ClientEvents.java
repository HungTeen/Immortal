package hungteen.imm.client.event;

import hungteen.imm.ImmortalMod;
import hungteen.imm.client.ClientHandler;
import hungteen.imm.client.gui.screen.meditation.MeditationScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
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
        if(event.phase == TickEvent.Phase.END){
            ClientHandler.onSmithing();
            ClientHandler.tickSpellCircle();
            MeditationScreen.tickMeditation();
        }
    }

    @SubscribeEvent
    public static void gatherComponents(RenderTooltipEvent.GatherComponents event){
//        if(ElixirManager.isElixirIngredient(event.getItemStack())){
//            List<Either<FormattedText, TooltipComponent>> components = event.getTooltipElements();
//            components.add(components.size(), Either.right(new ElementToolTip(ElixirManager.getElixirIngredient(event.getItemStack()))));
//        }
    }

    @SubscribeEvent
    public static void gatherComponents(RenderLevelStageEvent event){
    }

}
