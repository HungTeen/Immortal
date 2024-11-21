package hungteen.imm.common.event;

import hungteen.imm.common.ElementManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/7/1 15:46
 */
//@EventBusSubscriber(modid = IMMAPI.MOD_ID)
public class IMMLevelEvents {

    @SubscribeEvent
    public static void tick(LevelTickEvent.Post event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            for (Entity entity : serverLevel.getEntities().getAll()) {
                ElementManager.tickElements(entity);
            }
        }
    }

}
