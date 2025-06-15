package hungteen.imm.api.spell;

import com.mojang.serialization.MapCodec;
import hungteen.htlib.api.registry.SimpleEntry;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/7/17 15:20
 */
public interface IScrollType<P extends IScrollContent> extends SimpleEntry {

    MapCodec<P> codec();
}
