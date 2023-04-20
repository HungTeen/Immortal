package hungteen.imm.common.item.runes.info;

import com.mojang.serialization.Codec;
import hungteen.htlib.util.helper.CodecHelper;
import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.common.item.runes.RuneItem;
import hungteen.imm.common.rune.filter.BaseFilterRune;
import hungteen.imm.common.rune.filter.EqualGateRune;
import hungteen.imm.common.rune.filter.FilterRuneTypes;
import hungteen.imm.common.rune.filter.IFilterRune;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
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

    public void bind(Player player, ItemStack stack, T obj){
        if(this.getGateRune(stack).isEmpty()){
            this.setInfo(stack, obj);
        } else {
            this.alreadyBind(player);
        }
        PlayerHelper.setCooldown(player, stack.getItem(), 10);
    }

    public void setGateRune(ItemStack stack, IFilterRune gateRune){
        CodecHelper.encodeNbt(FilterRuneTypes.getCodec(), gateRune)
                .result().ifPresent(runeNbt -> {
                    stack.getOrCreateTag().put(FILTER, runeNbt);
                });
    }

    public Optional<IFilterRune> getGateRune(ItemStack stack){
        if(stack.getOrCreateTag().contains(FILTER)){
            final CompoundTag nbt = stack.getOrCreateTag().getCompound(FILTER);
            return CodecHelper.parse(FilterRuneTypes.getCodec(), nbt).result();
        }
        return Optional.empty();
    }

    /**
     * Can only bind at the first time.
     */
    public void setInfo(ItemStack stack, T info){
        if(isEmpty(stack)){
            CodecHelper.encodeNbt(getCodec(), info)
                    .resultOrPartial(Util::error).ifPresent(tag -> {
                        final IFilterRune defaultRune = new EqualGateRune(FilterRuneItem.this, BaseFilterRune.warp(tag));
                        this.setGateRune(stack, defaultRune);
                    });
        }
    }

    public boolean isEmpty(ItemStack stack){
        return getGateRune(stack).isEmpty();
    }

    protected void alreadyBind(Player player){
        PlayerHelper.sendTipTo(player, ALREADY_BIND);
    }
}
