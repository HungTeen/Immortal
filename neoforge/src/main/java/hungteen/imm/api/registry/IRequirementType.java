package hungteen.imm.api.registry;

import com.mojang.serialization.MapCodec;
import hungteen.htlib.api.registry.SimpleEntry;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/7/17 15:42
 */
public interface IRequirementType<P extends ILearnRequirement> extends SimpleEntry {

    MapCodec<P> codec();
}
