package hungteen.immortal.common.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import hungteen.immortal.common.entity.human.HumanEntity;
import hungteen.immortal.utils.EntityUtil;
import hungteen.immortal.utils.ItemUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.item.ItemStack;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/2 20:10
 */
public class EatFood extends Behavior<HumanEntity> {

    private long finishUsingTick = 0;

    public EatFood() {
        super(ImmutableMap.of(), 120, 240);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, HumanEntity entity, long time) {
        return time > finishUsingTick || EntityUtil.isOffHolding(entity, ItemUtil::isFood);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, HumanEntity entity) {
        return entity.getHealth() < entity.getMaxHealth() && (! entity.isUsingItem() || entity.getHealth() < entity.getMaxHealth() * 0.6F) && EntityUtil.isOffHolding(entity, ItemStack::isEmpty) && entity.hasItemStack(ItemUtil::isFood);
    }

    @Override
    protected void start(ServerLevel level, HumanEntity entity, long time) {
        entity.switchInventory(InteractionHand.OFF_HAND, ItemUtil::isFood);
        entity.startUsingItem(InteractionHand.OFF_HAND);
        this.finishUsingTick = time + entity.getOffhandItem().getUseDuration();
    }

    @Override
    protected void tick(ServerLevel serverLevel, HumanEntity entity, long time) {
        if(time == this.finishUsingTick || timedOut(time + 1)){
            entity.getOffhandItem().finishUsingItem(serverLevel, entity);
        }
    }

    @Override
    protected void stop(ServerLevel serverLevel, HumanEntity entity, long time) {
        if(entity.isUsingItem()){
            entity.stopUsingItem();
        }
        entity.switchInventory(EquipmentSlot.OFFHAND, ItemStack::isEmpty);
    }
}
