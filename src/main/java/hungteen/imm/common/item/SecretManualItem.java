package hungteen.imm.common.item;

import com.mojang.datafixers.util.Pair;
import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.common.impl.manuals.SecretManual;
import hungteen.imm.common.impl.manuals.SecretManuals;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
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
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        final ItemStack stack = player.getItemInHand(hand);
        final Optional<SecretManual> manual = getSecretManual(level, stack);
        if(manual.isPresent()){
            if(manual.get().canLearn(level, player)) {
                player.startUsingItem(hand);
                return InteractionResultHolder.consume(stack);
            }
            return InteractionResultHolder.fail(stack);
        }
        return super.use(level, player, hand);
    }

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack stack, int tick) {
        if(! level.isClientSide() && tick % 20 == 0){
            EntityUtil.playSound(level, living, SoundEvents.BOOK_PAGE_TURN);
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity living) {
        final Optional<SecretManual> manual = getSecretManual(level, stack);
        if(! level.isClientSide() && manual.isPresent() && living instanceof Player player){
            manual.get().learn(level, player);
            if(! PlayerUtil.notConsume(player)){
                stack.shrink(1);
            }
            return stack;
        }
        return super.finishUsingItem(stack, level, living);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 60;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if(level != null) {
            getSecretManualPair(level, stack).ifPresent(res -> {
                components.add(res.getSecond().getManualTitle().withStyle(ChatFormatting.YELLOW).withStyle(ChatFormatting.BOLD));
                components.add(res.getSecond().getContentInfo().withStyle(ChatFormatting.GREEN));
                PlayerHelper.getClientPlayer().ifPresent(p -> {
                    if(! res.getSecond().getRequirementInfo(p).isEmpty()){
                        components.add(TipUtil.tooltip(this, "requirements").withStyle(ChatFormatting.RED));
                        components.addAll(res.getSecond().getRequirementInfo(p));
                    }
                });
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
        final Optional<ResourceKey<SecretManual>> opt = getResourceKey(stack);
        if(opt.isPresent()){
            return SecretManuals.registry().getOptValue(level, opt.get());
        }
        return Optional.empty();
    }

    public static Optional<Pair<ResourceKey<SecretManual>, SecretManual>> getSecretManualPair(Level level, ItemStack stack) {
        final Optional<ResourceKey<SecretManual>> opt = getResourceKey(stack);
        return opt.flatMap(secretManualResourceKey -> SecretManuals.registry().getOptValue(level, secretManualResourceKey).map(r -> Pair.of(secretManualResourceKey, r)));
    }

    @Override
    public Component getDescription() {
        return super.getDescription();
    }
}
