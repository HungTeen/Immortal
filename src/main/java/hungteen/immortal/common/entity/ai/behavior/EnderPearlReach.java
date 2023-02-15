package hungteen.immortal.common.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import hungteen.immortal.common.entity.human.HumanEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-12-07 16:36
 **/
public class EnderPearlReach extends Behavior<HumanEntity> {

    private final float chance;
    private final int duration;

    public EnderPearlReach(float chance, int duration) {
        super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), duration);
        this.chance = chance;
        this.duration = Math.max(20, duration);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, HumanEntity entity, long time) {
        return this.getAttackTarget(entity).isPresent();
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, HumanEntity entity) {
        return this.getAttackTarget(entity).isPresent() && entity.distanceToSqr(this.getAttackTarget(entity).get()) >= 100 && ! entity.filterFromInventory(stack -> stack.is(Items.ENDER_PEARL)).isEmpty();
    }

    @Override
    protected void start(ServerLevel level, HumanEntity entity, long time) {
        if(entity.getRandom().nextFloat() < this.chance){
            entity.switchInventory(stack -> stack.is(Items.ENDER_PEARL), InteractionHand.OFF_HAND);
            final LivingEntity target = this.getAttackTarget(entity).get();
            ThrownEnderpearl enderpearl = new ThrownEnderpearl(level, entity);
            enderpearl.setItem(entity.getOffhandItem());
            final double d0 = target.getEyeY() - (double)1.1F;
            final double d1 = target.getX() - entity.getX();
            final double d2 = d0 - enderpearl.getY();
            final double d3 = target.getZ() - entity.getZ();
            final double d4 = Math.sqrt(d1 * d1 + d3 * d3) * (double)0.2F;
            enderpearl.shoot(d1, d2 + d4, d3, 1.2F, 6.0F);
            entity.playSound(SoundEvents.ENDER_PEARL_THROW, 0.5F, 0.4F / (entity.getRandom().nextFloat() * 0.4F + 0.8F));
            level.addFreshEntity(enderpearl);
            entity.getOffhandItem().shrink(1);
        }
    }

    @Override
    protected void tick(ServerLevel level, HumanEntity entity, long time) {
    }

    @Override
    protected void stop(ServerLevel level, HumanEntity entity, long time) {
        if(entity.getOffhandItem().is(Items.ENDER_PEARL)){
            entity.switchInventory(ItemStack::isEmpty, InteractionHand.OFF_HAND);
        }
    }

    private Optional<LivingEntity> getAttackTarget(Mob mob) {
        return mob.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
    }

}
