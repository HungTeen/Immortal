package hungteen.imm.client.event;

import hungteen.imm.ImmortalMod;
import hungteen.imm.client.event.handler.RenderEventHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-07-02 16:43
 **/
@Mod.EventBusSubscriber(modid = ImmortalMod.MOD_ID, value = Dist.CLIENT)
public class EntityEvents {

    @SubscribeEvent
    public static void onPostRenderOverlay(RenderNameTagEvent ev) {
        RenderEventHandler.renderEntityElements(ev.getEntity(), ev.getEntityRenderer(), ev.getPoseStack(), ev.getMultiBufferSource(), ev.getPackedLight());
    }

}
