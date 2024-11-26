package hungteen.imm.common.event;

import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.cultivation.IHasMana;
import hungteen.imm.common.ElementManager;
import hungteen.imm.common.cultivation.CultivationManager;
import hungteen.imm.common.entity.misc.ThrowingItemEntity;
import hungteen.imm.common.impl.registry.ElementReactions;
import hungteen.imm.common.misc.damage.IMMDamageSources;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.LevelUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-09-25 16:32
 **/
//@EventBusSubscriber(modid = IMMAPI.MOD_ID)
public class IMMLivingEvents {

    @SubscribeEvent
    public static void tick(EntityTickEvent.Post event) {
        if(EntityHelper.isServer(event.getEntity()) && event.getEntity() instanceof LivingEntity living){
            // 灵气自然增长。
            if(event.getEntity() instanceof IHasMana entity && EntityUtil.canManaIncrease(event.getEntity())) {
                if(! entity.isManaFull()){
                    entity.addMana(LevelUtil.getSpiritualRate(event.getEntity().level(), event.getEntity().blockPosition()));
                }
            }
            // 附加元素。
            ElementManager.attachElement(living);
            // 元素反应：寄生。
            ElementManager.ifActiveReaction(event.getEntity(), ElementReactions.PARASITISM, (reaction, scale) -> {
                if(EntityUtil.hasMana(event.getEntity())){
                    EntityUtil.addMana(event.getEntity(), - scale * 2.5F);
                } else {
                    if(event.getEntity().getRandom().nextFloat() < 0.2F){
                        event.getEntity().hurt(IMMDamageSources.elementReaction(event.getEntity()), scale * 2F);
                    }
                }
            });
        }
    }

//    @SubscribeEvent
//    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
//        if(event.getEntity().hasEffect(IMMEffects.SOLIDIFICATION.get())){
//            final Vec3 speed = event.getEntity().getDeltaMovement();
//            event.getEntity().setDeltaMovement(speed.x(), Math.min(speed.y(), 0), speed.z());
//        }
//    }

    @SubscribeEvent
    public static void onLivingAttackedBy(LivingDamageEvent.Post event){
        // TODO New Damage.
        if(event.getEntity().level() instanceof ServerLevel level) {
            ElementManager.attachDamageElement(level, event.getEntity(), event.getSource());
            // 发生淬刃的被攻击者，会记录最大的受伤害值。
            ElementManager.ifActiveReaction(event.getEntity(), ElementReactions.QUENCH_BLADE, (reaction, scale) -> {
                ElementManager.setQuenchBladeDamage(event.getEntity(), event.getNewDamage(), true);
            }, () -> {
                ElementManager.setQuenchBladeDamage(event.getEntity(), 0, true);
            });
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingDamageEvent.Pre event){
//        if(event.getEntity().level() instanceof ServerLevel level) {
//            SharpnessSpell.checkSharpening(event.getSource().getEntity(), event);
//            if(event.getSource().is(DamageTypes.MOB_ATTACK) || event.getSource().is(DamageTypes.PLAYER_ATTACK)){
//                ElementReactions.GildingReaction.ifGlidingActive(event.getSource().getEntity(), event.getEntity());
//                ElementManager.ifActiveReaction(event.getSource().getEntity(), ElementReactions.QUENCH_BLADE, (reaction, scale) -> {
//                    final float damage = event.getNewDamage() + ElementManager.getQuenchBladeDamage(event.getSource().getEntity());
//                    event.setNewDamage(damage);
//                    ElementManager.addElementAmount(event.getEntity(), Elements.WATER, false, scale * 5);
//                });
//            }
//        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void postLivingHurt(LivingDamageEvent.Pre event){
//        if(event.getEntity().level() instanceof ServerLevel level) {
//            if (event.getEntity().hasEffect(IMMEffects.SOLIDIFICATION.holder())) {
//                if (event.getNewDamage() >= event.getEntity().getMaxHealth() * 0.1F) {
//                    event.getEntity().removeEffect(IMMEffects.SOLIDIFICATION.holder());
//                    event.setNewDamage(event.getNewDamage() * 1.1F);
//                    ParticleHelper.spawnParticles(level, ParticleUtil.block(Blocks.DIRT.defaultBlockState()), event.getEntity().getEyePosition(), 20, 0, 0.15);
//                    event.getEntity().playSound(SoundEvents.GRAVEL_BREAK);
//                } else {
//                    ParticleHelper.spawnParticles(level, ParticleUtil.block(Blocks.DIRT.defaultBlockState()), event.getEntity().getEyePosition(), 10, 0, 0.15);
//                }
//            }
//        }
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Pre event){
//        if(EntityHelper.isServer(event.getEntity()) && event.getEntity().level() instanceof ServerLevel serverLevel){
//            ElementManager.ifActiveReaction(event.getEntity(), ElementReactions.CUTTING, (reaction, scale) -> {
//                if(ElementManager.hasElement(event.getEntity(), Elements.WATER, false) && reaction instanceof ElementReactions.CuttingReaction cuttingReaction){
//                    float amount = Math.min(ElementManager.getAmount(event.getEntity(), Elements.WATER, false), cuttingReaction.getWaterAmount() * scale);
//                    event.setAmount(event.getAmount() + (scale - amount / cuttingReaction.getWaterAmount()) * 0.8F);
//                } else {
//                    event.setAmount(event.getAmount() + scale * 0.8F);
//                }
//                ParticleUtil.spawnParticles(serverLevel, IMMParticles.METAL_DAMAGE.get(), event.getEntity().position(), 1, 0.1, 0);
//            });
//        }
    }

    /**
     * 优先级最低，确保此时没有事件取消格挡（也就是盾一定会格挡）。
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onShieldBlock(LivingShieldBlockEvent event){
        if(EntityHelper.isServer(event.getEntity())){
            if(event.getDamageSource().getDirectEntity() instanceof ThrowingItemEntity throwingItem && throwingItem.getOwner() instanceof LivingEntity attacker){
                if(throwingItem.getItem().canDisableShield(event.getEntity().getUseItem(), event.getEntity(), attacker)){
                    EntityUtil.disableShield(throwingItem.level(), event.getEntity());
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onLivingDeath(LivingDeathEvent ev) {
        if(ev.getEntity() instanceof ServerPlayer player){
//            if(BreakThroughTrial.checkTrialFail(player)){
//                ev.setCanceled(true);
//                return;
//            }
        }
        // Cause by player.
        if(ev.getSource().getEntity() instanceof ServerPlayer player) {
            CultivationManager.onPlayerKillLiving(player, ev.getEntity());
        }
    }

}
