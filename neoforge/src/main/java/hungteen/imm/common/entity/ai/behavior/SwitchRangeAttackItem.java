package hungteen.imm.common.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import hungteen.imm.common.entity.ai.IMMMemories;
import hungteen.imm.common.entity.human.HumanLikeEntity;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.ItemUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-12-06 21:46
 **/
public class SwitchRangeAttackItem extends Behavior<HumanLikeEntity> {

    private final float refreshChance;

    public SwitchRangeAttackItem(float refreshChance) {
        super(ImmutableMap.of(IMMMemories.UNABLE_RANGE_ATTACK.get(), MemoryStatus.VALUE_ABSENT), 20, 40);
        this.refreshChance = refreshChance;
    }

    @Override
    protected boolean canStillUse(ServerLevel level, HumanLikeEntity entity, long time) {
        return true;
    }

    /**
     * Switch weapon when there is no item in hand(avoid conflict) or refresh melee weapon.
     */
    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, HumanLikeEntity entity) {
        return true;
    }

    @Override
    protected void start(ServerLevel level, HumanLikeEntity entity, long time) {
        if(! EntityUtil.isMainHolding(entity, ItemUtil::isRangeWeapon) || entity.getRandom().nextFloat() < refreshChance){
            entity.switchInventory(InteractionHand.MAIN_HAND, stack -> ItemUtil.isRangeWeapon(stack) && ! entity.getProjectile(stack).isEmpty());
        }
        // No range weapon in inventory at all.
        if(! EntityUtil.isMainHolding(entity, ItemUtil::isRangeWeapon)){
            entity.getBrain().setMemory(IMMMemories.UNABLE_RANGE_ATTACK.get(), true);
        }
    }

}
