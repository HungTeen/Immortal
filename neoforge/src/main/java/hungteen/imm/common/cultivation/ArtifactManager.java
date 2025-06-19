package hungteen.imm.common.cultivation;

import hungteen.imm.api.artifact.ArtifactCategory;
import hungteen.imm.api.artifact.ArtifactRank;
import hungteen.imm.common.cultivation.impl.ArtifactCategories;
import hungteen.imm.util.TipUtil;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2025/6/18 19:10
 **/
public class ArtifactManager {

    public static ArtifactRank getRank(ItemStack stack){
        return ArtifactRank.COMMON;
    }

    public static ArtifactCategory getCategory(ItemStack stack){
        return ArtifactCategories.DEFAULT;
    }

    public static MutableComponent getRankComponent(ItemStack stack){
        return TipUtil.misc("artifact." + getRank(stack).getSerializedName());
    }

    public static MutableComponent getArtifactComponent(ItemStack stack){
        return getRankComponent(stack).append(getCategory(stack).getComponent());
    }
}
