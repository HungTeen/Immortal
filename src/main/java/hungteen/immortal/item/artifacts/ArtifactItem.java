package hungteen.immortal.item.artifacts;

import hungteen.immortal.api.interfaces.IArtifact;
import hungteen.immortal.item.ItemTabs;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-08 09:42
 **/
public abstract class ArtifactItem extends Item implements IArtifact {

    public ArtifactItem() {
        super(new Properties().tab(ItemTabs.ARTIFACTS));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        if(isAncientArtifact()){
            components.add(new TranslatableComponent("misc.immortal.artifact.ancient").withStyle(ChatFormatting.BLACK));
        } else{
            components.add(new TranslatableComponent("misc.immortal.artifact.level_" + getArtifactLevel()).withStyle(
                    getArtifactLevel() <= 3 ? ChatFormatting.GREEN :
                            getArtifactLevel() <= 6 ? ChatFormatting.BLUE :
                                    ChatFormatting.DARK_PURPLE
            ));
        }
    }

}
