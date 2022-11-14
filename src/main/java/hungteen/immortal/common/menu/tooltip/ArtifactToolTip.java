package hungteen.immortal.common.menu.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-13 18:40
 **/
public class ArtifactToolTip implements TooltipComponent {

    private static final int HEIGHT = 18;
    private static final int WIDTH = 18;
    private final ItemStack artifact;

    public ArtifactToolTip(ItemStack artifact) {
        this.artifact = artifact.copy();
    }

    public ItemStack getArtifact() {
        return artifact;
    }

    public int getHeight(){
        return HEIGHT;
    }

    public int getWidth(){
        return WIDTH;
    }

}
