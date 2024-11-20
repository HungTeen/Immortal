package hungteen.imm.common.item.artifacts;

import hungteen.imm.api.interfaces.IArtifactItem;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.util.TipUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-08 09:42
 **/
public abstract class ArtifactItem extends Item implements IArtifactItem {

    protected static final UUID ARTIFACT_ATTACK_DAMAGE_UUID = UUID.fromString("a0c2228c-6166-11ed-9b6a-0242ac120002");
    protected static final UUID ARTIFACT_ATTACK_SPEED_UUID = UUID.fromString("a0c22552-6166-11ed-9b6a-0242ac120002");
    protected static final UUID ARTIFACT_REACH_DISTANCE_UUID = UUID.fromString("a0c2296c-6166-11ed-9b6a-0242ac120002");
    private final IRealmType realmType;

    public ArtifactItem(IRealmType realmType) {
        this(new Properties().stacksTo(1), realmType);
    }

    public ArtifactItem(Properties properties, IRealmType realmType) {
        super(properties);
        this.realmType = realmType;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Item.TooltipContext level, List<Component> components, TooltipFlag tooltipFlag) {
        components.add(this.getDesc(itemStack));
    }

    protected MutableComponent getDesc(ItemStack stack){
        return TipUtil.desc(this).withStyle(ChatFormatting.GRAY);
    }

    @Override
    public IRealmType getArtifactRealm(ItemStack stack) {
        return this.realmType;
    }
}
