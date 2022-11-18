package hungteen.immortal.common.item;

import hungteen.immortal.common.datapack.SpellBookManager;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-18 19:11
 **/
public class SpellBookItem extends Item {

    public static final String SPELL_BOOK = "spell_book";

    public SpellBookItem() {
        super(new Properties().tab(ItemTabs.SPELL_BOOKS).stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        getResource(stack).ifPresent(res -> {
            components.add(Component.translatable("item." + res.getNamespace() + ".spell_book." + res.getPath()));
        });
    }

    @Override
    public void fillItemCategory(CreativeModeTab creativeModeTab, NonNullList<ItemStack> itemStacks) {
        if(this.allowedIn(creativeModeTab)){
            SpellBookManager.getBooks().forEach(res -> {
                ItemStack stack = new ItemStack(this);
                setSpellBook(stack, res);
                itemStacks.add(stack);
            });
        }
    }

    public static void setSpellBook(ItemStack stack, ResourceLocation spellBook) {
        stack.getOrCreateTag().putString(SPELL_BOOK, spellBook.toString());
    }

    public static Optional<ResourceLocation> getResource(ItemStack stack) {
        return stack.getOrCreateTag().contains(SPELL_BOOK) ? Optional.of(new ResourceLocation(stack.getOrCreateTag().getString(SPELL_BOOK))) : Optional.empty();
    }

    public static Optional<SpellBookManager.SpellBook> getSpellBook(ItemStack stack) {
        return getResource(stack).flatMap(SpellBookManager::get);
    }

}
