package hungteen.immortal.common;

import hungteen.immortal.api.interfaces.IArtifact;
import net.minecraft.world.item.ItemStack;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-16 14:25
 **/
public class ArtifactManager {

    public static final int COMMON_ITEM = 0;
    public static final int COMMON_ARTIFACT = 1; // 低阶法器
    public static final int MODERATE_ARTIFACT = 2; // 中阶法器
    public static final int ADVANCED_ARTIFACT = 3; // 高阶法器

    public static int getArtifactLevel(ItemStack stack){
        return stack.getItem() instanceof IArtifact ? ((IArtifact) stack.getItem()).getArtifactLevel() : 0;
    }
}
