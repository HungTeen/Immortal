package hungteen.immortal.common.item.artifacts;

import hungteen.immortal.api.interfaces.IArtifact;
import hungteen.immortal.common.item.ItemTabs;
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

    private final int artifactLevel;
    private final boolean isAncientArtifact;

    public ArtifactItem(int artifactLevel) {
        this(artifactLevel, false);
    }

    public ArtifactItem(int artifactLevel, boolean isAncientArtifact) {
        super(new Properties().tab(ItemTabs.ARTIFACTS));
        this.artifactLevel = artifactLevel;
        this.isAncientArtifact = isAncientArtifact;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        if(isAncientArtifact()){
            components.add(new TranslatableComponent("misc.immortal.artifact.ancient").withStyle(ChatFormatting.BLACK));
        } else{
            final int artifactLevel = getItemArtifactLevel(itemStack);
            components.add(new TranslatableComponent("misc.immortal.artifact.level_" + artifactLevel).withStyle(
                    artifactLevel <= 3 ? ChatFormatting.GREEN :
                            artifactLevel <= 6 ? ChatFormatting.BLUE :
                                    ChatFormatting.DARK_PURPLE
            ));
        }
    }

    @Override
    public int getArtifactLevel() {
        return artifactLevel;
    }

    public int getItemArtifactLevel(ItemStack stack) {
        return this.getArtifactLevel();
    }

    @Override
    public boolean isAncientArtifact() {
        return isAncientArtifact;
    }
}
