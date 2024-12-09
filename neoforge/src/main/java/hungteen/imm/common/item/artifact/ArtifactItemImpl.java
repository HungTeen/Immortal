package hungteen.imm.common.item.artifact;

import hungteen.imm.api.artifact.ArtifactItem;
import hungteen.imm.api.artifact.ArtifactRank;
import hungteen.imm.common.item.IMMComponents;
import hungteen.imm.util.ItemUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-08 09:42
 **/
public abstract class ArtifactItemImpl extends Item implements ArtifactItem {

    protected static final UUID ARTIFACT_ATTACK_DAMAGE_UUID = UUID.fromString("a0c2228c-6166-11ed-9b6a-0242ac120002");
    protected static final UUID ARTIFACT_ATTACK_SPEED_UUID = UUID.fromString("a0c22552-6166-11ed-9b6a-0242ac120002");
    protected static final UUID ARTIFACT_REACH_DISTANCE_UUID = UUID.fromString("a0c2296c-6166-11ed-9b6a-0242ac120002");

    public ArtifactItemImpl(ArtifactRank realmType) {
        this(new Properties().stacksTo(1), realmType);
    }

    public ArtifactItemImpl(Properties properties, ArtifactRank realmType) {
        super(properties.component(IMMComponents.ARTIFACT_RANK, realmType));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Item.TooltipContext level, List<Component> components, TooltipFlag tooltipFlag) {
        components.add(ItemUtil.desc(itemStack));
    }

    @Override
    public ArtifactRank getArtifactRealm(ItemStack stack) {
        return this.components().getOrDefault(IMMComponents.ARTIFACT_RANK.get(), ArtifactRank.COMMON);
    }

}