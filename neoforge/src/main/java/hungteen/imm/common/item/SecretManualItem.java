package hungteen.imm.common.item;

import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.common.event.events.PlayerLearnManualEvent;
import hungteen.imm.common.impl.manuals.SecretManual;
import hungteen.imm.common.impl.manuals.SecretManuals;
import hungteen.imm.common.impl.manuals.SecretScroll;
import hungteen.imm.common.menu.tooltip.ManualToolTip;
import hungteen.imm.util.EventUtil;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * 既可以当做卷轴使用，也可以当做秘籍使用的物品。
 * @program Immortal
 * @author HungTeen
 * @create 2022-11-18 19:11
 **/
public class SecretManualItem extends Item {

    public SecretManualItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        final ItemStack stack = player.getItemInHand(hand);
        final Optional<SecretManual> manual = getSecretManual(player.level(), stack);
        manual.ifPresent(secretManual -> Util.getProxy().openManualScreen(player, secretManual, getManualPage(stack), hand));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    public static void changePage(ServerPlayer player, InteractionHand hand, int page) {
        ItemStack item = player.getItemInHand(hand);
        if(item.is(IMMItems.SECRET_MANUAL.get())){
            setManualPage(item, page);
        }
    }

    public static void learnManual(ServerPlayer player, InteractionHand hand, int page){
        ItemStack item = player.getItemInHand(hand);
        if(item.is(IMMItems.SECRET_MANUAL.get())){
            Optional<SecretManual> secretManual = getSecretManual(player.serverLevel(), item);
            if(secretManual.isPresent() && page >= 0 && page < secretManual.get().scrolls().size()){
                SecretScroll scroll = secretManual.get().scrolls().get(page);
                if(scroll.canLearn(player.serverLevel(), player)) {
                    scroll.learn(player.serverLevel(), player);
                    EventUtil.post(new PlayerLearnManualEvent(player, item, scroll));
                }
            }
        }
    }

    @Override
    public Component getName(ItemStack stack) {
        Optional<Player> player = PlayerHelper.getClientPlayer();
        if(player.isPresent()){
            Optional<Component> component = getSecretManual(player.get().level(), stack).map(SecretManual::title);
            if(component.isPresent()){
                return component.get().copy().withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD);
            }
        }
        return super.getName(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Item.TooltipContext context, List<Component> components, TooltipFlag flag) {
        if(context != null && context.level() != null){
            Optional<SecretManual> secretManual = getSecretManual(context.level(), stack);
            Optional<SecretScroll> secretScroll = getSecretScroll(context.level(), stack);
            if(secretManual.isPresent()){
                secretManual.get().desc().ifPresent(c -> {
                    components.add(c.copy().withStyle(ChatFormatting.GRAY));
                });
            }
            if(secretScroll.isPresent()){
                Optional<Player> opt = PlayerHelper.getClientPlayer();
                if(opt.isPresent()) {
                    if (!secretScroll.get().getRequirementInfo(opt.get()).isEmpty()) {
                        if (Util.getProxy().isShiftKeyDown()) {
                            components.add(TipUtil.tooltip(this, "requirements").withStyle(ChatFormatting.RED));
                            components.addAll(secretScroll.get().getRequirementInfo(opt.get()));
                        } else {
                            components.add(TipUtil.rune("shift_to_see_details").withStyle(ChatFormatting.DARK_RED, ChatFormatting.ITALIC));
                        }
                    }
                }
            }
        }
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        Optional<Player> player = PlayerHelper.getClientPlayer();
        if(player.isPresent()){
            Optional<SecretManual> secretManual = getSecretManual(player.get().level(), stack);
            Optional<SecretScroll> secretScroll = getSecretScroll(player.get().level(), stack);
            if(secretScroll.isPresent() && secretManual.isPresent()){
                return Optional.of(new ManualToolTip(secretManual.get(), secretScroll.get()));
            }
        }
        return Optional.empty();
    }

    public static ItemStack create(ResourceKey<SecretManual> key) {
        final ItemStack stack = new ItemStack(IMMItems.SECRET_MANUAL.get());
        setSecretManual(stack, key);
        return stack;
    }

    /**
     * @return 获取当前正在看的秘籍。
     */
    public static Optional<SecretManual> getSecretManual(Level level, ItemStack stack) {
        return getSecretManualKey(stack).flatMap(key -> SecretManuals.registry().getOptValue(level, key));
    }

    /**
     * @return 获取当前正在看的卷轴。
     */
    public static Optional<SecretScroll> getSecretScroll(Level level, ItemStack stack) {
        return getSecretManual(level, stack).map(manual -> {
            return manual.scrolls().get(Math.clamp(getManualPage(stack), 0, manual.scrolls().size() - 1));
        });
    }

    public static void setSecretManual(ItemStack stack, ResourceKey<SecretManual> manual) {
        stack.set(IMMDataComponents.SECRET_MANUAL, manual);
    }

    /**
     * @return 获取秘籍的注册名。
     */
    public static Optional<ResourceKey<SecretManual>> getSecretManualKey(ItemStack stack) {
        return Optional.ofNullable(stack.get(IMMDataComponents.SECRET_MANUAL));
    }

    /**
     * @return 获取当前正在看的页码，默认为 0。
     */
    public static Integer getManualPage(ItemStack stack) {
        return stack.getOrDefault(IMMDataComponents.MANUAL_PAGE, 0);
    }

    /**
     * 设置当前正在看的页码。
     */
    public static void setManualPage(ItemStack stack, int page) {
        stack.set(IMMDataComponents.MANUAL_PAGE, page);
    }

}
