package hungteen.imm.common.rune.filter;

import com.mojang.serialization.Codec;
import hungteen.htlib.api.interfaces.ISimpleEntry;
import hungteen.imm.util.TipUtil;
import net.minecraft.network.chat.MutableComponent;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-04 21:37
 **/
public interface IFilterRuneType<P extends IFilterRune> extends ISimpleEntry {

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

    Codec<P> codec();

}
