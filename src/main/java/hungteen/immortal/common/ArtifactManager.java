package hungteen.immortal.common;

import hungteen.immortal.api.interfaces.IArtifact;
import net.minecraft.world.item.ItemStack;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-16 14:25
 **/
public class ArtifactManager {

    public static int getArtifactLevel(ItemStack stack){
        return stack.getItem() instanceof IArtifact ? ((IArtifact) stack.getItem()).getArtifactLevel() : 0;
    }
}
