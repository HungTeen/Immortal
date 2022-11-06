package hungteen.immortal.common.item.artifacts;

import hungteen.htlib.util.ItemUtil;
import hungteen.immortal.client.render.item.RawArtifactItemRender;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.function.Consumer;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-06 12:07
 **/
public class RawArtifact extends ArtifactItem {

    private static final String ARTIFACT_ITEM = "ArtifactItem";
    private static final String SPIRITUAL_VALUE = "SpiritualValue";
    private static final String SPIRITUAL_VALUE_REQUIRED = "SpiritualValueRequired";

    public RawArtifact() {
        super(1);
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> stacks) {
        if(tab == CreativeModeTab.TAB_SEARCH){
            ItemUtil.getFilterItems(ArtifactItem.class::isInstance).forEach(item -> {
                ItemStack stack = new ItemStack(this);
                setArtifactItem(stack, new ItemStack(item));
                stacks.add(stack);
            });
        }
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
    public int getItemArtifactLevel(ItemStack stack) {
        return super.getItemArtifactLevel(stack);
    }

    @Override
    public Component getDescription() {
        return super.getDescription();
    }

    @Override
    public Component getName(ItemStack stack) {
        final ItemStack itemStack = getArtifactItem(stack);
        return itemStack.getItem().getName(itemStack);
    }

    //    @Override
//    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
//        consumer.accept(RawArtifactRender.INSTANCE);
//    }
//
//    private static class RawArtifactRender implements IItemRenderProperties{
//
//        private static final RawArtifactRender INSTANCE = new RawArtifactRender();
//
//        @Override
//        public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
//            return new RawArtifactItemRender();
//        }
//    }
}
