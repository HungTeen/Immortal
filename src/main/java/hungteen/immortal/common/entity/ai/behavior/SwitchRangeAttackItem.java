package hungteen.immortal.common.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import hungteen.immortal.common.entity.ai.ImmortalMemories;
import hungteen.immortal.common.entity.human.HumanEntity;
import hungteen.immortal.utils.EntityUtil;
import hungteen.immortal.utils.ItemUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.item.ItemStack;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-12-06 21:46
 **/
public class SwitchRangeAttackItem extends Behavior<HumanEntity> {

    private final float refreshChance;

    public SwitchRangeAttackItem(float refreshChance) {
        super(ImmutableMap.of(ImmortalMemories.UNABLE_RANGE_ATTACK.get(), MemoryStatus.VALUE_ABSENT), 20, 40);
        this.refreshChance = refreshChance;
    }

    @Override
    protected boolean canStillUse(ServerLevel level, HumanEntity entity, long time) {
        return true;
    }

    /**
     * Switch weapon when there is no item in hand(avoid conflict) or refresh melee weapon.
     */
    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, HumanEntity entity) {
        return EntityUtil.isMainHolding(entity, ItemStack::isEmpty) || EntityUtil.isMainHolding(entity, ItemUtil::isRangeWeapon);
    }

    @Override
    protected void start(ServerLevel level, HumanEntity entity, long time) {
        if(! EntityUtil.isMainHolding(entity, ItemUtil::isRangeWeapon) || entity.getRandom().nextFloat() < refreshChance){
            entity.switchInventory(InteractionHand.MAIN_HAND, stack -> ItemUtil.isRangeWeapon(stack) && ! entity.getProjectile(stack).isEmpty());
        }
        // No range weapon in inventory at all.
        if(! EntityUtil.isMainHolding(entity, ItemUtil::isRangeWeapon)){
            entity.getBrain().setMemory(ImmortalMemories.UNABLE_RANGE_ATTACK.get(), true);
        }
    }

}