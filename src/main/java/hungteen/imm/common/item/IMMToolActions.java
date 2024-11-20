package hungteen.imm.common.item;

import com.google.common.collect.Sets;
import net.neoforged.neoforge.common.ItemAbility;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-19 22:52
 **/
public class IMMToolActions {

    public static final ItemAbility ARTIFACT_SMITHING = ItemAbility.get("artifact_smithing");

    public static final Set<ItemAbility> DEFAULT_HAMMER_ACTIONS = of(ARTIFACT_SMITHING);

    private static Set<ItemAbility> of(ItemAbility... actions) {
        return Stream.of(actions).collect(Collectors.toCollection(Sets::newIdentityHashSet));
    }
}
