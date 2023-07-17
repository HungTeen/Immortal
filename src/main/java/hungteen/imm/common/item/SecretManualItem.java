package hungteen.imm.common.item;

import hungteen.imm.common.codec.SecretManual;
import hungteen.imm.common.impl.manuals.SecretManuals;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
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
public class SecretManualItem extends Item {

    public static final String SECRET_MANUAL = "SecretManual";

    public SecretManualItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if(! level.isClientSide){

        }
        return super.use(level, player, interactionHand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
//        get(stack).ifPresent(res -> {
//            components.add(Component.translatable("item." + res.getNamespace() + ".spell_book." + res.getPath()));
//        });
    }

//    @Override
//    public void fillItemCategory(CreativeModeTab creativeModeTab, NonNullList<ItemStack> itemStacks) {
//        if(this.allowedIn(creativeModeTab)){
//            SpellBookManager.getBooks().forEach(res -> {
//                ItemStack stack = new ItemStack(this);
//                setSpellBook(stack, res);
//                itemStacks.add(stack);
//            });
//        }
//    }

    public static void setSpellBook(ItemStack stack, ResourceLocation spellBook) {
        stack.getOrCreateTag().putString(SECRET_MANUAL, spellBook.toString());
    }

    public static Optional<ResourceLocation> getLocation(ItemStack stack) {
        return stack.getOrCreateTag().contains(SECRET_MANUAL) ? Optional.of(new ResourceLocation(stack.getOrCreateTag().getString(SECRET_MANUAL))) : Optional.empty();
    }

    public static Optional<ResourceKey<SecretManual>> getResourceKey(ItemStack stack) {
        return getLocation(stack).map(l -> SecretManuals.registry().createKey(l));
    }

    public static Optional<SecretManual> getSecretManual(Level level, ItemStack stack) {
        return getResourceKey(stack).map(key -> SecretManuals.registry().getValue(level, key));
    }

}
