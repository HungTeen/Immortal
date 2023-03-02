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
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/2 20:10
 */
public class EatFood extends Behavior<HumanEntity> {

    private int usingTick = 0;

    public EatFood() {
        super(ImmutableMap.of(), 120, 240);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, HumanEntity entity, long time) {
        return EntityUtil.isMainHolding(entity, ItemUtil::isFood);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, HumanEntity entity) {
        return (! entity.isUsingItem() || entity.getHealth() < entity.getMaxHealth() * 0.6F) && EntityUtil.isOffHolding(entity, ItemStack::isEmpty) && entity.hasItemStack(ItemUtil::isFood);
    }

    @Override
    protected void start(ServerLevel level, HumanEntity entity, long time) {
        entity.switchInventory(InteractionHand.OFF_HAND, ItemUtil::isFood);
        entity.startUsingItem(InteractionHand.OFF_HAND);
    }

    @Override
    protected void tick(ServerLevel serverLevel, HumanEntity entity, long time) {
        if(++ usingTick >= entity.getOffhandItem().getUseDuration()){
            usingTick = 0;
            entity.getOffhandItem().finishUsingItem(serverLevel, entity);
        }
    }

    @Override
    protected void stop(ServerLevel serverLevel, HumanEntity entity, long time) {
        if(entity.isUsingItem()){
            entity.stopUsingItem();
        }
    }
}
