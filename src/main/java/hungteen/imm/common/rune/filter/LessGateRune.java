package hungteen.imm.common.rune.filter;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.util.TipUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-04 22:51
 **/
public class LessGateRune extends BaseFilterRune {

    public static final MapCodec<LessGateRune> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Info.CODEC.fieldOf(INFO).forGetter(LessGateRune::getInfo)
    ).apply(instance, LessGateRune::new));

    public LessGateRune(Item item, CompoundTag tag) {
        this(new Info(item, tag));
    }

    public LessGateRune(Info info) {
        super(info);
    }

    @Override
    public boolean check(Object target, Object current) {
        if(target instanceof Float a && current instanceof Float b) {
            return Float.compare(a, b) == -1;
        } else if(target instanceof Integer a && current instanceof Integer b) {
            return Integer.compare(a, b) == -1;
        }
        return false;
    }

    @Override
    public MutableComponent getFilterText() {
        return TipUtil.rune("less_rune", getDataText());
    }

    @Override
    public IFilterRuneType<?> getType() {
        return FilterRuneTypes.EQUAL;
    }
}
