package hungteen.imm.common.event;

import hungteen.imm.ImmortalMod;
import hungteen.imm.common.ElementManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/1 15:46
 */
@Mod.EventBusSubscriber(modid = ImmortalMod.MOD_ID)
public class IMMLevelEvents {

    @SubscribeEvent
    public static void tick(TickEvent.LevelTickEvent event) {
        if(event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END) {
            if(event.level instanceof ServerLevel serverLevel){
                for (Entity entity : serverLevel.getEntities().getAll()) {
                    ElementManager.tickElements(entity);
                }
            }
        }
    }

}
