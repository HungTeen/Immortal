package hungteen.imm.common.event;

import hungteen.imm.ImmortalMod;
import hungteen.imm.api.registry.IArtifactType;
import hungteen.imm.common.ArtifactManager;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/28 17:12
 */
@Mod.EventBusSubscriber(modid = ImmortalMod.MOD_ID)
public class IMMItemEvents {

    @SubscribeEvent
    public static void gatherComponents(ItemTooltipEvent event){
        // 添加法器标识。
        final IArtifactType type = ArtifactManager.getArtifactType(event.getItemStack());
        if(ArtifactManager.notCommon(type)){
            event.getToolTip().add(1, type.getComponent());
        }
    }

}
