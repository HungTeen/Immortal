package hungteen.immortal.client.event;

import com.mojang.datafixers.util.Either;
import hungteen.immortal.ImmortalMod;
import hungteen.immortal.client.ClientDatas;
import hungteen.immortal.common.ElixirManager;
import hungteen.immortal.common.menu.tooltip.ElementToolTip;
import hungteen.immortal.common.tag.ImmortalItemTags;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-10 11:09
 **/
@Mod.EventBusSubscriber(modid = ImmortalMod.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void tick(TickEvent.ClientTickEvent event){
    }

    @SubscribeEvent
    public static void gatherComponents(RenderTooltipEvent.GatherComponents event){
        if(ElixirManager.isElixirIngredient(event.getItemStack())){
            List<Either<FormattedText, TooltipComponent>> components = event.getTooltipElements();
            components.add(components.size(), Either.right(new ElementToolTip(ElixirManager.getElixirIngredient(event.getItemStack()))));
        }
    }

}
