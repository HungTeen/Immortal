package hungteen.imm.common.block.artifacts;

import hungteen.htlib.common.block.entityblock.HTEntityBlock;
import hungteen.imm.api.interfaces.IArtifactBlock;
import hungteen.imm.api.registry.IArtifactType;
import hungteen.imm.util.TipUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/2/28 20:41
 */
public abstract class ArtifactEntityBlock extends HTEntityBlock implements IArtifactBlock {

    private final IArtifactType artifactType;

    protected ArtifactEntityBlock(BlockBehaviour.Properties properties, IArtifactType artifactType) {
        super(properties);
        this.artifactType = artifactType;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable BlockGetter getter, List<Component> components, TooltipFlag flag) {
        components.add(this.getDesc(itemStack));
    }

    protected MutableComponent getDesc(ItemStack stack){
        return TipUtil.desc(this).withStyle(ChatFormatting.GREEN);
    }

    @Override
    public IArtifactType getArtifactType(BlockState state) {
        return this.artifactType;
    }

    @Override
    public IArtifactType getArtifactType(ItemStack stack) {
        return this.artifactType;
    }
}
