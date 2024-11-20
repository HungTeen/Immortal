package hungteen.imm.api.registry;

import com.mojang.serialization.MapCodec;
import hungteen.htlib.api.registry.SimpleEntry;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/17 15:20
 */
public interface IManualType<P extends IManualContent> extends SimpleEntry {

    MapCodec<P> codec();
}
