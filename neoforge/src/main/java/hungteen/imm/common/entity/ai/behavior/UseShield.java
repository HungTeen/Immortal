package hungteen.imm.common.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.common.entity.ai.IMMMemories;
import hungteen.imm.util.BehaviorUtil;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.ItemUtil;
import hungteen.imm.util.interfaces.InventorySwitcher;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-03-12 16:52
 **/
public class UseShield extends Behavior<Mob> {

    public UseShield(int minInterval, int maxInterval) {
        super(ImmutableMap.of(
                MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED,
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.REGISTERED,
                MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_PRESENT,
                IMMMemories.NEAREST_PROJECTILE.get(), MemoryStatus.REGISTERED
        ), minInterval, maxInterval);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, Mob entity, long time) {
        return EntityUtil.isOffHolding(entity, ItemUtil::isShield);
    }

    /**
     * 没在使用物品 & 副手是空的 & 有盾牌 & （有攻击目标 & 有35%概率 或 有投掷物）
     */
    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, Mob entity) {
        if(entity instanceof InventorySwitcher switcher){
            return ! entity.isUsingItem() && (EntityHelper.isOffHolding(entity, ItemStack::isEmpty) && switcher.hasItemStack(ItemUtil::isShield)) && (BehaviorUtil.getAttackTarget(entity).isPresent() && entity.getRandom().nextFloat() < 0.35F || getProjectile(entity).isPresent());
        }
        return false;
    }

    @Override
    protected void start(ServerLevel level, Mob entity, long time) {
        if(entity instanceof InventorySwitcher switcher){
            switcher.switchInventory(InteractionHand.OFF_HAND, ItemUtil::isShield);
            entity.startUsingItem(InteractionHand.OFF_HAND);
        }
    }

    @Override
    protected void tick(ServerLevel serverLevel, Mob entity, long time) {
        //TODO 破盾
        getLookTarget(entity).ifPresent(l -> BehaviorUtil.lookAtEntity(entity, l));
    }

    @Override
    protected void stop(ServerLevel serverLevel, Mob entity, long time) {
        if(entity instanceof InventorySwitcher switcher) {
            if (entity.isUsingItem()) {
                entity.stopUsingItem();
            }
            switcher.switchInventory(InteractionHand.OFF_HAND, ItemStack::isEmpty);
        }
    }

    private Optional<Projectile> getProjectile(Mob entity){
        return entity.getBrain().getMemory(IMMMemories.NEAREST_PROJECTILE.get());
    }

    private Optional<Entity> getLookTarget(Mob entity) {
        return Optional.ofNullable(getProjectile(entity).map(Entity.class::cast).orElse(BehaviorUtil.getAttackTarget(entity).orElse(null)));
    }
}
