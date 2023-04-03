package hungteen.imm.common.entity.ai.behavior.golem;

import hungteen.imm.common.entity.golem.GolemEntity;
import hungteen.imm.common.item.runes.BehaviorRuneItem;
import hungteen.imm.common.rune.behavior.IBehaviorRune;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-03 20:11
 **/
public class GolemBehavior extends Behavior<GolemEntity> {

    private final Map<Integer, Object> runeMap = new HashMap<>();

    public GolemBehavior(CompoundTag nbt, Map<MemoryModuleType<?>, MemoryStatus> statusMap, int minDuration, int maxDuration) {
        super(statusMap, minDuration, maxDuration);
        final ItemStack stack = ItemStack.of(nbt);
        if(stack.getItem() instanceof BehaviorRuneItem item){
            final List<ItemStack> stacks = item.getRuneItems(stack);
            final IBehaviorRune rune = item.getBehaviorRune();
            for(int i = 0; i < stacks.size(); i++) {
                final int id = i;
                rune.parse(i, stacks.get(i).serializeNBT()).ifPresent(l -> {
                    runeMap.put(id, l);
                });
            }
        }
    }

    protected <T> T get(int id, Class<T> clazz, T defaultValue) {
        if(runeMap.containsKey(id)){
            final Object obj = runeMap.get(id);
            if(clazz.isInstance(obj)){
                return clazz.cast(obj);
            }
        }
        return defaultValue;
    }

}
