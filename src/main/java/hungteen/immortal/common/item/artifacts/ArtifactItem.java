package hungteen.immortal.common.item.artifacts;

import hungteen.immortal.api.interfaces.IArtifact;
import hungteen.immortal.common.item.ItemTabs;
import net.minecraft.ChatFormatting;
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
public abstract class ArtifactItem extends Item implements IArtifact {

    protected static final UUID ARTIFACT_ATTACK_DAMAGE_UUID = UUID.fromString("a0c2228c-6166-11ed-9b6a-0242ac120002");
    protected static final UUID ARTIFACT_ATTACK_SPEED_UUID = UUID.fromString("a0c22552-6166-11ed-9b6a-0242ac120002");
    protected static final UUID ARTIFACT_REACH_DISTANCE_UUID = UUID.fromString("a0c2296c-6166-11ed-9b6a-0242ac120002");
    private final int artifactLevel;
    private final boolean isAncientArtifact;

    public ArtifactItem(int artifactLevel) {
        this(artifactLevel, false);
    }

    public ArtifactItem(int artifactLevel, boolean isAncientArtifact) {
        super(new Properties().tab(ItemTabs.ARTIFACTS).stacksTo(1));
        this.artifactLevel = artifactLevel;
        this.isAncientArtifact = isAncientArtifact;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        if(isAncientArtifact()){
            components.add(Component.translatable("misc.immortal.artifact.ancient").withStyle(ChatFormatting.BLACK));
        } else{
            final int artifactLevel = getItemArtifactLevel(itemStack);
            components.add(Component.translatable("misc.immortal.artifact.level_" + artifactLevel).withStyle(
                    artifactLevel <= 3 ? ChatFormatting.BLUE :
                            artifactLevel <= 6 ? ChatFormatting.DARK_PURPLE :
                                    ChatFormatting.GOLD
            ));
        }
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 0;
    }

    @Override
    public int getArtifactLevel() {
        return artifactLevel;
    }

    public int getItemArtifactLevel(ItemStack stack) {
        return this.getArtifactLevel();
    }

    @Override
    public boolean isAncientArtifact() {
        return isAncientArtifact;
    }
}
