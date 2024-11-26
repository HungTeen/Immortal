package hungteen.imm.common.block.artifacts;

import hungteen.htlib.common.block.entityblock.HTEntityBlock;
import hungteen.imm.api.interfaces.IArtifactBlock;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.util.TipUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/2/28 20:41
 */
public abstract class ArtifactEntityBlock extends HTEntityBlock implements IArtifactBlock {

    private final RealmType realmType;

    protected ArtifactEntityBlock(BlockBehaviour.Properties properties, RealmType realmType) {
        super(properties);
        this.realmType = realmType;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Item.TooltipContext getter, List<Component> components, TooltipFlag flag) {
        components.add(this.getDesc(itemStack));
    }


    protected MutableComponent getDesc(ItemStack stack){
        return TipUtil.desc(this).withStyle(ChatFormatting.GRAY);
    }

    @Override
    public RealmType getRealm(BlockState state) {
        return this.realmType;
    }

    @Override
    public RealmType getArtifactRealm(ItemStack stack) {
        return this.realmType;
    }

    public RealmType getRealmType() {
        return realmType;
    }
}
