package hungteen.imm.common.item.blockitem;

import hungteen.imm.api.artifact.ArtifactCategory;
import hungteen.imm.api.artifact.ArtifactItem;
import hungteen.imm.api.artifact.ArtifactRank;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/5 22:44
 **/
public class ArtifactBlockItem extends BlockItem implements ArtifactItem {

    public ArtifactBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public ArtifactRank getArtifactRealm(ItemStack stack) {
        return null;
    }

    @Override
    public ArtifactCategory getArtifactCategory() {
        return null;
    }
}
