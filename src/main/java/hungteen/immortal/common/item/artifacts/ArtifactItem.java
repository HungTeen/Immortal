package hungteen.immortal.common.item.artifacts;

import hungteen.immortal.api.interfaces.IArtifactItem;
import hungteen.immortal.api.registry.IArtifactType;
import hungteen.immortal.common.item.ImmortalItemTabs;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
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
    private final IArtifactType artifactType;

    public ArtifactItem(IArtifactType artifactType) {
        this(new Properties().stacksTo(1), artifactType);
    }

    public ArtifactItem(Properties properties, IArtifactType artifactType) {
        super(properties);
        this.artifactType = artifactType;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        components.add(getArtifactType(itemStack).getComponent());
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false; // Not allowed to enchant.
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 0; // Not allowed to enchant.
    }

    @Override
    public IArtifactType getArtifactType(ItemStack stack) {
        return this.artifactType;
    }
}
