package hungteen.imm.common.event;

import hungteen.imm.api.IMMAPI;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.common.RealmManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/28 17:12
 */
@EventBusSubscriber(modid = IMMAPI.MOD_ID)
public class IMMItemEvents {

    @SubscribeEvent
    public static void gatherComponents(ItemTooltipEvent event){
        // 添加法器标识。
        final IRealmType type = RealmManager.getRealm(event.getItemStack());
        if(RealmManager.notCommon(type)){
            event.getToolTip().add(1, type.getComponent());
        }
    }

}
