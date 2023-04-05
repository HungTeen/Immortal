package hungteen.imm.common.item.runes.info;

import com.mojang.serialization.Codec;
import hungteen.htlib.util.helper.CodecHelper;
import hungteen.imm.common.item.runes.RuneItem;
import hungteen.imm.common.rune.filter.EqualGateRune;
import hungteen.imm.common.rune.filter.FilterRuneTypes;
import hungteen.imm.common.rune.filter.IFilterRune;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-04 21:54
 **/
public abstract class FilterRuneItem<T> extends RuneItem {

    private static final String FILTER = "Filter";

    public abstract Codec<T> getCodec();

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
                    .result().filter(CompoundTag.class::isInstance)
                    .map(CompoundTag.class::cast).ifPresent(tag -> {
                        final IFilterRune defaultRune = new EqualGateRune(FilterRuneItem.this, tag);
                        this.setGateRune(stack, defaultRune);
                    });
        }
    }

    public boolean isEmpty(ItemStack stack){
        return getGateRune(stack).isPresent();
    }

}
