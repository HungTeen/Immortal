package hungteen.imm.api.registry;

import com.mojang.serialization.Codec;
import hungteen.htlib.api.interfaces.ISimpleEntry;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/17 15:42
 */
public interface IRequirementType<P extends ILearnRequirement> extends ISimpleEntry {

    Codec<P> codec();
}
