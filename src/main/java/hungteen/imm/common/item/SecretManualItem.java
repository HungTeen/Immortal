package hungteen.imm.common.item;

import com.mojang.datafixers.util.Pair;
import hungteen.htlib.util.helper.StringHelper;
import hungteen.imm.common.impl.manuals.SecretManual;
import hungteen.imm.common.impl.manuals.SecretManuals;
import hungteen.imm.util.TipUtil;
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
    public Component getName(ItemStack stack) {
        return getLocation(stack).map(key -> {
            return StringHelper.langKey("item", key.getNamespace(), "secret_manual." + key.getPath());
        }).map(Component::translatable).map(Component.class::cast).orElse(super.getName(stack));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if(level != null) {
            getSecretManualPair(level, stack).ifPresent(res -> {
                if(! res.getSecond().getContentInfo().isEmpty()){
                    components.add(TipUtil.tooltip(this, "contents"));
                    components.addAll(res.getSecond().getContentInfo());
                }
                if(! res.getSecond().getRequirementInfo().isEmpty()){
                    components.add(TipUtil.tooltip(this, "requirements"));
                    components.addAll(res.getSecond().getRequirementInfo());
                }
            });
        }
    }

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

    public static Optional<Pair<ResourceKey<SecretManual>, SecretManual>> getSecretManualPair(Level level, ItemStack stack) {
        return getResourceKey(stack).map(key -> Pair.of(key, SecretManuals.registry().getValue(level, key)));
    }

}
