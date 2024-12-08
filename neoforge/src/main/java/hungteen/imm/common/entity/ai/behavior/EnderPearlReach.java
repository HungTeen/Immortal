package hungteen.imm.common.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import hungteen.imm.common.entity.human.HumanLikeEntity;
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
import java.util.function.Predicate;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-12-07 16:36
 **/
public class EnderPearlReach extends Behavior<HumanLikeEntity> {

    private final float chance;
    private final int duration;
    private final Predicate<LivingEntity> predicate;

    public EnderPearlReach(float chance, int duration){
        this(chance, duration, l -> true);
    }

    public EnderPearlReach(float chance, int duration, Predicate<LivingEntity> predicate) {
        super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), duration);
        this.chance = chance;
        this.duration = Math.max(20, duration);
        this.predicate = predicate;
    }

    @Override
    protected boolean canStillUse(ServerLevel level, HumanLikeEntity entity, long time) {
        return this.getAttackTarget(entity).isPresent();
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, HumanLikeEntity entity) {
        return this.getAttackTarget(entity).isPresent() && entity.distanceToSqr(this.getAttackTarget(entity).get()) >= 100 && ! entity.filterFromInventory(stack -> stack.is(Items.ENDER_PEARL)).isEmpty() && predicate.test(this.getAttackTarget(entity).get());
    }

    @Override
    protected void start(ServerLevel level, HumanLikeEntity entity, long time) {
        if(entity.getRandom().nextFloat() < this.chance){
            entity.switchInventory(InteractionHand.OFF_HAND, stack -> stack.is(Items.ENDER_PEARL));
            final LivingEntity target = this.getAttackTarget(entity).get();
            ThrownEnderpearl pearl = new ThrownEnderpearl(level, entity);
            pearl.setItem(entity.getOffhandItem());
            final double d0 = target.getEyeY() - (double)1.1F;
            final double d1 = target.getX() - entity.getX();
            final double d2 = d0 - pearl.getY();
            final double d3 = target.getZ() - entity.getZ();
            final double d4 = Math.sqrt(d1 * d1 + d3 * d3) * (double)0.2F;
            pearl.shoot(d1, d2 + d4, d3, 1.2F, 6.0F);
            entity.playSound(SoundEvents.ENDER_PEARL_THROW, 0.5F, 0.4F / (entity.getRandom().nextFloat() * 0.4F + 0.8F));
            level.addFreshEntity(pearl);
            entity.getOffhandItem().shrink(1);
        }
    }

    @Override
    protected void tick(ServerLevel level, HumanLikeEntity entity, long time) {
    }

    @Override
    protected void stop(ServerLevel level, HumanLikeEntity entity, long time) {
        if(entity.getOffhandItem().is(Items.ENDER_PEARL)){
            entity.switchInventory(InteractionHand.OFF_HAND, ItemStack::isEmpty);
        }
    }

    private Optional<LivingEntity> getAttackTarget(Mob mob) {
        return mob.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
    }

}
