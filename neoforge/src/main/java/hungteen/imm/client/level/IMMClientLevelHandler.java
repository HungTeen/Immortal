package hungteen.imm.client.level;

import hungteen.imm.common.world.levelgen.IMMDimensionTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/22 16:47
 **/
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class IMMClientLevelHandler {

    @SubscribeEvent
    public static void registerDimensionSpecialEffect(RegisterDimensionSpecialEffectsEvent event){
        event.register(IMMDimensionTypes.SPIRIT_WORLD_EFFECTS, new SpiritWorldEffects());
    }
}
