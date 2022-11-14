package hungteen.immortal.common.item;

import com.google.common.collect.Sets;
import net.minecraftforge.common.ToolAction;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-19 22:52
 **/
public class ImmortalToolActions {

    public static final ToolAction ARTIFACT_SMITHING = ToolAction.get("artifact_smithing");

    public static final Set<ToolAction> DEFAULT_HAMMER_ACTIONS = of(ARTIFACT_SMITHING);

    private static Set<ToolAction> of(ToolAction... actions) {
        return Stream.of(actions).collect(Collectors.toCollection(Sets::newIdentityHashSet));
    }
}
