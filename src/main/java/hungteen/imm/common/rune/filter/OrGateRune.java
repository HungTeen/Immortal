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
public class OrGateRune extends ListGateRune{

    public static final Codec<OrGateRune> CODEC = RecordCodecBuilder.<OrGateRune>mapCodec(instance -> instance.group(
            Info.CODEC.fieldOf("info").forGetter(OrGateRune::getInfo)
    ).apply(instance, OrGateRune::new)).codec();

    public OrGateRune(Item item, List<IFilterRune> filters) {
        this(new Info(item, filters));
    }

    public OrGateRune(Info info) {
        super(info);
    }

    @Override
    public MutableComponent getFilterText() {
        return TipUtil.rune("or_rune", getDataText());
    }

    @Override
    public <T> Predicate<T> getPredicate(Class<?> clazz, Predicate<T> predicate) {
        return t -> getInfo().filters().stream().anyMatch(l -> l.getPredicate(clazz, predicate).test(t));
    }

    @Override
    public IFilterRuneType<?> getType() {
        return FilterRuneTypes.OR;
    }
}
