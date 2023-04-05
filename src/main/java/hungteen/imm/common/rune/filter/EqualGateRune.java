package hungteen.imm.common.rune.filter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-04 22:51
 **/
public class EqualGateRune extends BaseGateRune {

    public static final Codec<EqualGateRune> CODEC = RecordCodecBuilder.<EqualGateRune>mapCodec(instance -> instance.group(
            BaseGateRune.Info.CODEC.fieldOf("info").forGetter(EqualGateRune::getInfo)
    ).apply(instance, EqualGateRune::new)).codec();

    public EqualGateRune(Item item, CompoundTag tag) {
        this(new Info(item, tag));
    }

    public EqualGateRune(Info info) {
        super(info);
    }

    @Override
    public boolean check(Object target, Object current) {
        return target.equals(current);
    }

    @Override
    public IFilterRuneType<?> getType() {
        return FilterRuneTypes.EQUAL;
    }
}
