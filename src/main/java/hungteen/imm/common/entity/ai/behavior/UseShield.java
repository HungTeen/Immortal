package hungteen.imm.common.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.imm.common.entity.ai.ImmortalMemories;
import hungteen.imm.common.entity.human.HumanEntity;
import hungteen.imm.util.BehaviorUtil;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.ItemUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-03-12 16:52
 **/
public class UseShield extends Behavior<HumanEntity> {

    public UseShield(int minInterval, int maxInterval) {
        super(ImmutableMap.of(
                MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED,
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.REGISTERED,
                MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_PRESENT,
                ImmortalMemories.NEAREST_PROJECTILE.get(), MemoryStatus.REGISTERED
        ), minInterval, maxInterval);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, HumanEntity entity, long time) {
        return EntityUtil.isOffHolding(entity, ItemUtil::isShield);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, HumanEntity entity) {
        return ! entity.isUsingItem() && (EntityHelper.isOffHolding(entity, ItemStack::isEmpty) && entity.hasItemStack(ItemUtil::isShield)) && (BehaviorUtil.getAttackTarget(entity).isPresent() && entity.getRandom().nextFloat() < 0.35F || getProjectile(entity).isPresent());
    }

    @Override
    protected void start(ServerLevel level, HumanEntity entity, long time) {
        entity.switchInventory(InteractionHand.OFF_HAND, ItemUtil::isShield);
        entity.startUsingItem(InteractionHand.OFF_HAND);
    }

    @Override
    protected void tick(ServerLevel serverLevel, HumanEntity entity, long time) {
        //TODO 破盾
        getLookTarget(entity).ifPresent(l -> BehaviorUtil.lookAtEntity(entity, l));
    }

    @Override
    protected void stop(ServerLevel serverLevel, HumanEntity entity, long time) {
        if(entity.isUsingItem()){
            entity.stopUsingItem();
        }
        entity.switchInventory(InteractionHand.OFF_HAND, ItemStack::isEmpty);
    }

    private Optional<Projectile> getProjectile(HumanEntity entity){
        return entity.getBrain().getMemory(ImmortalMemories.NEAREST_PROJECTILE.get());
    }

    private Optional<Entity> getLookTarget(HumanEntity entity) {
        return Optional.ofNullable(getProjectile(entity).map(Entity.class::cast).orElse(BehaviorUtil.getAttackTarget(entity).orElse(null)));
    }
}
