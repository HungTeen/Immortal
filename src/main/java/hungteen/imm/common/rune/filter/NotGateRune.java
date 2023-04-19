package hungteen.imm.common.rune.filter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
    public <T> Predicate<T> getPredicate(Class<?> clazz, Predicate<T> predicate) {
        return t -> ! filter().getPredicate(clazz, predicate).test(t);
    }

    @Override
    public IFilterRuneType<?> getType() {
        return FilterRuneTypes.NOT;
    }

}
