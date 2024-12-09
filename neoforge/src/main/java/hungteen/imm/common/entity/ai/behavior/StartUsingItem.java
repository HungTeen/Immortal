package hungteen.imm.common.entity.ai.behavior;

import hungteen.imm.common.entity.ai.IMMMemories;
import hungteen.imm.common.entity.human.HumanLikeEntity;
import hungteen.imm.common.item.talisman.TalismanItem;
import hungteen.imm.util.ItemUtil;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.behavior.OneShot;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/9 21:27
 **/
public class StartUsingItem {

    /**
     * 就因为这玩意，必须搞成 BiPredicate。
     */
    public static OneShot<HumanLikeEntity> eatFood(){
        return forOffHand((human, stack) -> ItemUtil.isFood(stack, human), entity -> {
            float percent = 1 - entity.getHealth() / entity.getMaxHealth();
            // 血量少于 20% 或者随机概率。
            return percent > 0.8 || entity.getRandom().nextFloat() < percent;
        }, UniformInt.of(20, 40));
    }

    public static OneShot<HumanLikeEntity> useTalisman(IntProvider provider){
        return forMainHand((human, stack) -> stack.getItem() instanceof TalismanItem, entity -> {
            return true;
        }, provider);
    }

    public static OneShot<HumanLikeEntity> forMainHand(BiPredicate<HumanLikeEntity, ItemStack> itemPredicate, Predicate<HumanLikeEntity> entityPredicate, IntProvider cdProvider) {
        return create(itemPredicate, entityPredicate, net.minecraft.world.InteractionHand.MAIN_HAND, cdProvider);
    }

    public static OneShot<HumanLikeEntity> forOffHand(BiPredicate<HumanLikeEntity, ItemStack> itemPredicate, Predicate<HumanLikeEntity> entityPredicate, IntProvider cdProvider) {
        return createWithNoTarget(itemPredicate, entityPredicate, net.minecraft.world.InteractionHand.OFF_HAND, cdProvider);
    }

    public static OneShot<HumanLikeEntity> create(BiPredicate<HumanLikeEntity, ItemStack> itemPredicate, Predicate<HumanLikeEntity> entityPredicate, InteractionHand hand, IntProvider cdProvider) {
        return BehaviorBuilder.create(instance -> instance.group(
                instance.present(MemoryModuleType.ATTACK_TARGET),
                instance.absent(IMMMemories.USING_ITEM_COOLING_DOWN.get())
        ).apply(instance, (attackTarget, coolDown) -> trigger(
                itemPredicate, entityPredicate, hand, cdProvider
        )));
    }

    public static OneShot<HumanLikeEntity> createWithNoTarget(BiPredicate<HumanLikeEntity, ItemStack> itemPredicate, Predicate<HumanLikeEntity> entityPredicate, InteractionHand hand, IntProvider cdProvider) {
        return BehaviorBuilder.create(instance -> instance.group(
                instance.absent(IMMMemories.USING_ITEM_COOLING_DOWN.get())
        ).apply(instance, (coolDown) -> trigger(
                itemPredicate, entityPredicate, hand, cdProvider
        )));
    }

    public static Trigger<HumanLikeEntity> trigger(BiPredicate<HumanLikeEntity, ItemStack> itemPredicate, Predicate<HumanLikeEntity> entityPredicate, InteractionHand hand, IntProvider cdProvider) {
        return (level, mob, time) -> {
            // 检查是否正在使用物品，以及是否满足额外条件（比如吃东西需要血量不足）。
            if (!mob.isUsingItem()){
                if(entityPredicate.test(mob)) {
                    mob.switchInventory(hand, stack -> itemPredicate.test(mob, stack));
                    mob.startUsingItem(hand);
                }
                mob.getBrain().setMemoryWithExpiry(IMMMemories.USING_ITEM_COOLING_DOWN.get(), true, cdProvider.sample(level.random));
                return true;
            }
            return false;
        };
    }
}


