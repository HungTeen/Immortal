package hungteen.imm.common.cultivation.rune.filter;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.util.TipUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-04-04 22:51
 **/
public class EqualGateRune extends BaseFilterRune {

    public static final MapCodec<EqualGateRune> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BaseFilterRune.Info.CODEC.fieldOf(INFO).forGetter(EqualGateRune::getInfo)
    ).apply(instance, EqualGateRune::new));

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
    public MutableComponent getFilterText() {
        return TipUtil.rune("equal_rune", getDataText());
    }

    @Override
    public IFilterRuneType<?> getType() {
        return FilterRuneTypes.EQUAL;
    }
}
