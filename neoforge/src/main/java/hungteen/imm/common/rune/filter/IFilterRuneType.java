package hungteen.imm.common.rune.filter;

import com.mojang.serialization.MapCodec;
import hungteen.htlib.api.registry.SimpleEntry;
import hungteen.imm.util.TipUtil;
import net.minecraft.network.chat.MutableComponent;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-04 21:37
 **/
public interface IFilterRuneType<P extends IFilterRune> extends SimpleEntry {

    boolean isBaseType();

    boolean isNumberOperation();

    int requireMinEntry();

    int requireMaxEntry();

    @Override
    default MutableComponent getComponent() {
        return TipUtil.rune(this.getName() + "_filter");
    }

    default MutableComponent getDesc() {
        return TipUtil.rune(this.getName() + "_filter.desc");
    }

    MapCodec<P> codec();

}
