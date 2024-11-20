package hungteen.imm.common.item.runes.filter;

import com.mojang.serialization.Codec;
import hungteen.htlib.util.helper.CodecHelper;
import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.common.item.runes.RuneItem;
import hungteen.imm.common.rune.ICraftableRune;
import hungteen.imm.common.rune.filter.*;
import hungteen.imm.util.TipUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-04 21:54
 **/
public abstract class FilterRuneItem<T> extends RuneItem {

    private static final MutableComponent ALREADY_BIND = TipUtil.info("rune_already_bind").withStyle(ChatFormatting.RED);
    private static final String FILTER = "Filter";

    public abstract Codec<T> getCodec();

    /**
     * 检查保存的信息类型是否支持门操作。
     */
    public boolean fitFilterRune(IFilterRuneType<?> type){
        return isNumberData() || !type.isNumberOperation();
    }

    protected abstract boolean isNumberData();

    @Override
    protected void addDisplayComponents(ItemStack stack, List<Component> components) {
        components.add(TipUtil.tooltip(this).withStyle(ChatFormatting.GREEN));
    }

    @Override
    protected void addHideComponents(ItemStack stack, List<Component> components) {
        getGateRune(stack).ifPresentOrElse(
                rune -> components.add(rune.getFilterText().withStyle(ChatFormatting.LIGHT_PURPLE)),
                () -> components.add(TipUtil.rune("no_filter_target").withStyle(ChatFormatting.RED))
        );
    }

    @Override
    protected boolean hasHideInfo(ItemStack stack) {
        return true;
    }

    public void bind(Player player, ItemStack stack, T obj) {
        if (!this.alreadyBind(stack)) {
            this.setInfo(stack, obj);
        } else {
            this.alertBind(player);
        }
        PlayerHelper.setCooldown(player, stack.getItem(), 10);
    }

    public void setGateRune(ItemStack stack, IFilterRune gateRune) {
        CodecHelper.encodeNbt(FilterRuneTypes.getCodec(), gateRune)
                .result().ifPresent(runeNbt -> {
                    putFilterRaw(stack, runeNbt);
                });
    }

    private void putFilterRaw(ItemStack stack, Tag tag) {
//        stack.getOrCreateTag().put(FILTER, tag);
    }

    public Optional<IFilterRune> getGateRune(ItemStack stack) {
//        if (stack.getOrCreateTag().contains(FILTER)) {
//            final CompoundTag nbt = getFilterRaw(stack);
//            return CodecHelper.parse(FilterRuneTypes.getCodec(), nbt).result();
//        }
        return Optional.empty();
    }

    public Optional<T> getData(ItemStack stack) {
        return alreadyBind(stack) || isEmpty(stack) ? Optional.empty() : BaseFilterRune.getData(getFilterRaw(stack), getCodec());
    }

    private CompoundTag getFilterRaw(ItemStack stack) {
//        return stack.getOrCreateTag().getCompound(FILTER);
        return new CompoundTag();
    }

    public void setInfo(ItemStack stack, T info) {
        CodecHelper.encodeNbt(getCodec(), info)
                .result().ifPresent(tag -> {
                    if (isEmpty(stack)) {
                        // Default filter is equal rune.
                        final IFilterRune defaultRune = new EqualGateRune(FilterRuneItem.this, BaseFilterRune.warp(tag));
                        this.setGateRune(stack, defaultRune);
                    } else {
                        // Replace info.
                        putFilterRaw(stack, BaseFilterRune.replace(getFilterRaw(stack), tag));
                    }
                });
    }

    public boolean isEmpty(ItemStack stack) {
        return getGateRune(stack).isEmpty();
    }

    protected boolean alreadyBind(ItemStack stack) {
        return ! isEmpty(stack) && getGateRune(stack).filter(BaseFilterRune.class::isInstance).isEmpty();
    }

    protected void alertBind(Player player) {
        PlayerHelper.sendTipTo(player, ALREADY_BIND);
    }

    protected record FilterCraftableRune(int cost) implements ICraftableRune {

        @Override
        public boolean costAmethyst() {
            return false;
        }

        @Override
        public int requireMaterial() {
            return cost();
        }

        @Override
        public int requireRedStone() {
            return cost();
        }
    }

}
