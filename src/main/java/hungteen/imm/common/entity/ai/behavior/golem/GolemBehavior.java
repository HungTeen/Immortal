package hungteen.imm.common.entity.ai.behavior.golem;

import hungteen.imm.common.entity.golem.GolemEntity;
import hungteen.imm.common.item.runes.BehaviorRuneItem;
import hungteen.imm.common.rune.behavior.BehaviorRunes;
import hungteen.imm.common.rune.behavior.IBehaviorRune;
import hungteen.imm.common.rune.filter.IFilterRune;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-03 20:11
 **/
public class GolemBehavior extends Behavior<GolemEntity> {

    private final Map<Integer, IFilterRune> filterMap;
    private final IBehaviorRune behaviorRune;

    public GolemBehavior(ItemStack stack, Map<MemoryModuleType<?>, MemoryStatus> statusMap, int minDuration, int maxDuration) {
        super(statusMap, minDuration, maxDuration);
        if(stack.getItem() instanceof BehaviorRuneItem item){
            this.filterMap = item.getFilterMap(stack);
            this.behaviorRune = item.getBehaviorRune();
        } else {
            // Go here to access to the field of {memory status map}.
            this.filterMap = new HashMap<>();
            this.behaviorRune = BehaviorRunes.LOOK_AT_TARGET;
        }
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, GolemEntity golem) {
        return super.checkExtraStartConditions(level, golem);
    }

    public <T> Predicate<T> get(int id, Predicate<T> defaultValue) {
        if(filterMap.containsKey(id)){
            final IFilterRune filterRune = filterMap.get(id);
            return filterRune.getPredicate(this.behaviorRune.getPredicateClasses().get(id), defaultValue);
        }
        return defaultValue;
    }

}
