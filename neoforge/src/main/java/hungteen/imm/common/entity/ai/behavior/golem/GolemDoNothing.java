package hungteen.imm.common.entity.ai.behavior.golem;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.item.ItemStack;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-04-09 18:32
 **/
public class GolemDoNothing extends GolemBehavior{
    public GolemDoNothing(ItemStack stack) {
        super(stack, ImmutableMap.of(), 30, 60);
    }
}
