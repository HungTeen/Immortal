package hungteen.imm.common.rune.filter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.util.TipUtil;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.function.Predicate;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-18 23:13
 **/
public class AndGateRune extends ListGateRune{

    public static final Codec<AndGateRune> CODEC = RecordCodecBuilder.<AndGateRune>mapCodec(instance -> instance.group(
            ListGateRune.Info.CODEC.fieldOf("info").forGetter(AndGateRune::getInfo)
    ).apply(instance, AndGateRune::new)).codec();

    public AndGateRune(Item item, List<IFilterRune> filters) {
        this(new Info(item, filters));
    }

    public AndGateRune(Info info) {
        super(info);
    }

    @Override
    public MutableComponent getFilterText() {
        return TipUtil.rune("and_rune", getDataText());
    }

    @Override
    public <T> Predicate<T> getPredicate(Class<?> clazz, Predicate<T> predicate) {
        return t -> getInfo().filters().stream().allMatch(l -> l.getPredicate(clazz, predicate).test(t));
    }

    @Override
    public IFilterRuneType<?> getType() {
        return FilterRuneTypes.AND;
    }
}
