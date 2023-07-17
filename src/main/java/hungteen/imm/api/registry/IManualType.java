package hungteen.imm.api.registry;

import com.mojang.serialization.Codec;
import hungteen.htlib.api.interfaces.ISimpleEntry;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/17 15:20
 */
public interface IManualType<P extends IManualContent> extends ISimpleEntry {

    Codec<P> codec();
}
