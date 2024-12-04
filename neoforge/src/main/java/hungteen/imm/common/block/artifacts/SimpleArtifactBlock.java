package hungteen.imm.common.block.artifacts;

import hungteen.htlib.common.block.HTBlock;
import hungteen.imm.api.artifact.ArtifactBlock;
import hungteen.imm.api.artifact.ArtifactRank;
import hungteen.imm.util.TipUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/2/28 20:41
 */
public abstract class SimpleArtifactBlock extends HTBlock implements ArtifactBlock {

    private final ArtifactRank rank;

    protected SimpleArtifactBlock(Properties properties, ArtifactRank rank) {
        super(properties);
        this.rank = rank;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Item.TooltipContext getter, List<Component> components, TooltipFlag flag) {
        components.add(this.getDesc(itemStack));
    }

    protected MutableComponent getDesc(ItemStack stack){
        return TipUtil.desc(this).withStyle(ChatFormatting.GRAY);
    }

    @Override
    public ArtifactRank getRealm(BlockState state) {
        return this.rank;
    }

}
