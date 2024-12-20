package hungteen.imm.common.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import hungteen.imm.common.entity.human.HumanLikeEntity;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.ItemUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/3/2 20:10
 */
@Deprecated
public class WearArmor extends Behavior<HumanLikeEntity> {

    private EquipmentSlot slot;
    private static final int CD = 20;
    private long finishUsingTick = 0;

    public WearArmor() {
        super(ImmutableMap.of(), 30, 40);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, HumanLikeEntity entity, long time) {
        return time > this.finishUsingTick || entity.getItemBySlot(this.slot).isEmpty() && EntityUtil.isOffHolding(entity, ItemUtil::isArmor);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, HumanLikeEntity entity) {
        if(! entity.getOffhandItem().isEmpty() || entity.getRandom().nextFloat() < 0.1F) return false;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if(slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) continue;
            if(entity.getItemBySlot(slot).isEmpty() && entity.hasItemStack(stack -> stack.getItem() instanceof ArmorItem item && item.getEquipmentSlot() == slot)){
                this.slot = slot;
                return true;
            }
        }
        return false;
    }

    @Override
    protected void start(ServerLevel level, HumanLikeEntity entity, long time) {
        entity.switchInventory(InteractionHand.OFF_HAND, s -> ItemUtil.isArmor(s, this.slot));
        this.finishUsingTick = time + CD;
    }

    @Override
    protected void tick(ServerLevel serverLevel, HumanLikeEntity entity, long time) {
        if(time == this.finishUsingTick){
            entity.setItemSlot(slot, entity.getOffhandItem().copy());
            entity.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
        }
    }

    @Override
    protected void stop(ServerLevel serverLevel, HumanLikeEntity entity, long time) {
    }
}
