package hungteen.immortal.common.item.artifacts;

import hungteen.htlib.client.RenderHelper;
import hungteen.htlib.util.helper.registry.ItemHelper;
import hungteen.immortal.api.registry.IArtifactType;
import hungteen.immortal.common.impl.ArtifactTypes;
import hungteen.immortal.common.menu.tooltip.ArtifactToolTip;
import hungteen.immortal.utils.Colors;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-06 12:07
 **/
public class RawArtifactBox extends ArtifactItem {

    private static final String ARTIFACT_ITEM = "ArtifactItem";
    private static final String SPIRITUAL_VALUE = "SpiritualValue";
    private static final String SPIRITUAL_VALUE_REQUIRED = "SpiritualValueRequired";

    public RawArtifactBox() {
        super(ArtifactTypes.EMPTY);
    }

//    @Override
//    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> stacks) {
//        if(tab == CreativeModeTab.TAB_SEARCH){
//            ItemHelper.get().getFilterEntries(ArtifactItem.class::isInstance).forEach(item -> {
//                ItemStack stack = new ItemStack(this);
//                setArtifactItem(stack, new ItemStack(item));
//                stacks.add(stack);
//            });
//        }
//    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {

    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        final ItemStack artifact = getArtifactItem(stack);
        return artifact.isEmpty() ? Optional.empty() : Optional.of(new ArtifactToolTip(artifact));
    }

    public static void setArtifactItem(ItemStack stack, ItemStack artifactItem){
        stack.getOrCreateTag().put(ARTIFACT_ITEM, artifactItem.serializeNBT());
    }

    public static ItemStack getArtifactItem(ItemStack stack){
        return ItemStack.of(stack.getOrCreateTag().getCompound(ARTIFACT_ITEM));
    }

    public static void addSpiritualValue(ItemStack stack, int value){
        final int currentValue = getSpiritualValue(stack);
        final int maxValue = getSpiritualValueRequired(stack);
        stack.getOrCreateTag().putInt(SPIRITUAL_VALUE, Mth.clamp(currentValue + value, 0, maxValue));
    }

    public static int getSpiritualValue(ItemStack stack){
        return stack.getOrCreateTag().getInt(SPIRITUAL_VALUE);
    }

    public static void setSpiritualValueRequired(ItemStack stack, int value){
        stack.getOrCreateTag().putInt(SPIRITUAL_VALUE_REQUIRED, value);
    }

    public static int getSpiritualValueRequired(ItemStack stack){
        return stack.getOrCreateTag().getInt(SPIRITUAL_VALUE_REQUIRED);
    }

    @Override
    public IArtifactType getArtifactType(ItemStack stack) {
        return super.getArtifactType(stack);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return ! getArtifactItem(stack).isEmpty();
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        final int value = getSpiritualValueRequired(stack);
        return value == 0 ? 0 : getSpiritualValue(stack) * RenderHelper.ITEM_BAR_LEN / value;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return Colors.SPIRITUAL_MANA;
    }

    @Override
    public Component getDescription() {
        return super.getDescription();
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        final ItemStack artifact = getArtifactItem(stack);
        return stack.isEmpty() ? Rarity.COMMON : artifact.getRarity();
    }

    @Override
    public Component getName(ItemStack stack) {
        final ItemStack itemStack = getArtifactItem(stack);
        return itemStack.getItem().getName(itemStack);
    }

}
