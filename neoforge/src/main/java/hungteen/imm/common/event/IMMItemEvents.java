package hungteen.imm.common.event;

import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.common.cultivation.CultivationManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/3/28 17:12
 */
//@EventBusSubscriber(modid = IMMAPI.MOD_ID)
public class IMMItemEvents {

    @SubscribeEvent
    public static void gatherComponents(ItemTooltipEvent event){
        // 添加法器标识。
        final RealmType type = CultivationManager.getRealm(event.getItemStack());
        if(CultivationManager.notCommon(type)){
            event.getToolTip().add(1, type.getComponent());
        }
    }

}
