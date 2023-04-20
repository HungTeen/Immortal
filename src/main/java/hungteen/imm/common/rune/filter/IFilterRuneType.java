package hungteen.imm.common.rune.filter;

import com.mojang.serialization.Codec;
import hungteen.htlib.api.interfaces.ISimpleEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-04 21:37
 **/
public interface IFilterRuneType<P extends IFilterRune> extends ISimpleEntry {

    boolean isBaseType();

    int requireMinEntry();

    int requireMaxEntry();

    @Override
    default MutableComponent getComponent() {
        return Component.translatable("rune." + this.getModID() + ".filter." + this.getName());
    }

    default MutableComponent getDesc() {
        return Component.translatable("rune." + this.getModID() + ".filter." + this.getName() + ".desc");
    }

    Codec<P> codec();

}
