package hungteen.imm.common.item.artifact;

import hungteen.htlib.client.util.RenderHelper;
import hungteen.imm.api.artifact.ArtifactRank;
import hungteen.imm.util.Colors;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-11-06 12:07
 **/
@Deprecated
public class RawArtifactBox extends ArtifactItemImpl {

    private static final String ARTIFACT_ITEM = "ArtifactItem";
    private static final String SPIRITUAL_VALUE = "SpiritualValue";
    private static final String SPIRITUAL_VALUE_REQUIRED = "SpiritualValueRequired";

    public RawArtifactBox() {
        super(ArtifactRank.UNKNOWN);
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
    public void appendHoverText(ItemStack itemStack, @Nullable Item.TooltipContext level, List<Component> components, TooltipFlag tooltipFlag) {

    }

//    @Override
//    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
//        final ItemStack artifact = getArtifactItem(stack);
//        return artifact.isEmpty() ? Optional.empty() : Optional.of(new ArtifactToolTip(artifact));
//    }

    public static void setArtifactItem(ItemStack stack, ItemStack artifactItem){
//        stack.getOrCreateTag().put(ARTIFACT_ITEM, artifactItem.serializeNBT());
    }

    public static ItemStack getArtifactItem(ItemStack stack){
//        return ItemStack.of(stack.getOrCreateTag().getCompound(ARTIFACT_ITEM));
        return ItemStack.EMPTY;
    }

    public static void addSpiritualValue(ItemStack stack, int value){
        final int currentValue = getSpiritualValue(stack);
        final int maxValue = getSpiritualValueRequired(stack);
//        stack.getOrCreateTag().putInt(SPIRITUAL_VALUE, Mth.clamp(currentValue + value, 0, maxValue));
    }

    public static int getSpiritualValue(ItemStack stack){
//        return stack.getOrCreateTag().getInt(SPIRITUAL_VALUE);
        return 0;
    }

    public static void setSpiritualValueRequired(ItemStack stack, int value){
//        stack.getOrCreateTag().putInt(SPIRITUAL_VALUE_REQUIRED, value);
    }

    public static int getSpiritualValueRequired(ItemStack stack){
//        return stack.getOrCreateTag().getInt(SPIRITUAL_VALUE_REQUIRED);
        return 0;
    }

    @Override
    public ArtifactRank getArtifactRealm(ItemStack stack) {
        return super.getArtifactRealm(stack);
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

//    @Override
//    public Rarity getRarity(ItemStack stack) {
//        final ItemStack artifact = getArtifactItem(stack);
//        return stack.isEmpty() ? Rarity.COMMON : artifact.getRarity();
//    }

    @Override
    public Component getName(ItemStack stack) {
        final ItemStack itemStack = getArtifactItem(stack);
        return itemStack.getItem().getName(itemStack);
    }

}
