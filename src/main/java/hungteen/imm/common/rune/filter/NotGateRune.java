package hungteen.imm.common.rune.filter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.common.item.runes.filter.FilterRuneItem;
import hungteen.imm.util.TipUtil;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Predicate;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-19 23:28
 **/
public record NotGateRune(Item item, IFilterRune filter) implements IFilterRune{

    public static final Codec<NotGateRune> CODEC = RecordCodecBuilder.<NotGateRune>mapCodec(instance -> instance.group(
            ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(NotGateRune::item),
            FilterRuneTypes.getCodec().fieldOf("filters").forGetter(NotGateRune::filter)
    ).apply(instance, NotGateRune::new)).codec();

    @Override
    public MutableComponent getFilterText() {
        return TipUtil.rune("not_rune", filter().getFilterText());
    }

    @Override
    public <T> Predicate<T> getPredicate(FilterRuneItem<?> item, Predicate<T> predicate) {
        return t -> ! filter().getPredicate(item, predicate).test(t);
    }

    @Override
    public IFilterRuneType<?> getType() {
        return FilterRuneTypes.NOT;
    }

}
