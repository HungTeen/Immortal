package hungteen.imm.common.event;

import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.htlib.util.helper.registry.ParticleHelper;
import hungteen.imm.ImmortalMod;
import hungteen.imm.api.enums.Elements;
import hungteen.imm.api.interfaces.IHasMana;
import hungteen.imm.common.ElementManager;
import hungteen.imm.common.RealmManager;
import hungteen.imm.common.effect.IMMEffects;
import hungteen.imm.common.entity.misc.ThrowingItemEntity;
import hungteen.imm.common.impl.registry.ElementReactions;
import hungteen.imm.common.misc.damage.IMMDamageSources;
import hungteen.imm.common.spell.spells.metal.SharpnessSpell;
import hungteen.imm.common.world.entity.trial.BreakThroughTrial;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.LevelUtil;
import hungteen.imm.util.ParticleUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-25 16:32
 **/
@Mod.EventBusSubscriber(modid = ImmortalMod.MOD_ID)
public class IMMLivingEvents {

    @SubscribeEvent
    public static void tick(LivingEvent.LivingTickEvent event) {
        if(EntityHelper.isServer(event.getEntity())){
            // 灵气自然增长。
            if(event.getEntity() instanceof IHasMana entity && EntityUtil.canManaIncrease(event.getEntity())) {
                if(! entity.isManaFull()){
                    entity.addMana(LevelUtil.getSpiritualRate(event.getEntity().level(), event.getEntity().blockPosition()));
                }
            }
            // 附加元素。
            ElementManager.attachElement(event.getEntity());
            // 元素反应：寄生。
            ElementManager.ifActiveReaction(event.getEntity(), ElementReactions.PARASITISM, scale -> {
                if(EntityUtil.hasMana(event.getEntity())){
                    EntityUtil.addMana(event.getEntity(), - scale * 2.5F);
                } else {
                    if(event.getEntity().getRandom().nextFloat() < 0.2F){
                        event.getEntity().hurt(IMMDamageSources.elementReaction(null), scale * 2F);
                    }
                }
            });
            RealmManager.limitEnchantments(event.getEntity());
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
    public static void onLivingAttackedBy(LivingAttackEvent event){
        if(event.getEntity().level() instanceof ServerLevel level) {
            ElementManager.attachDamageElement(level, event.getEntity(), event.getSource());
            // 发生淬刃的被攻击者，会记录最大的受伤害值。
            ElementManager.ifActiveReaction(event.getEntity(), ElementReactions.QUENCH_BLADE, scale -> {
                ElementManager.setQuenchBladeDamage(event.getEntity(), event.getAmount(), true);
            }, () -> {
                ElementManager.setQuenchBladeDamage(event.getEntity(), 0, true);
            });
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event){
        if(event.getEntity().level() instanceof ServerLevel level) {
            SharpnessSpell.checkSharpening(event.getSource().getEntity(), event);
            if(event.getSource().is(DamageTypes.MOB_ATTACK) || event.getSource().is(DamageTypes.PLAYER_ATTACK)){
                ElementManager.ifActiveReaction(event.getSource().getEntity(), ElementReactions.QUENCH_BLADE, scale -> {
                    final float damage = event.getAmount() + ElementManager.getQuenchBladeDamage(event.getSource().getEntity());
                    event.setAmount(damage);
                    ElementManager.addElementAmount(event.getEntity(), Elements.WATER, false, scale * 5);
                });
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void postLivingHurt(LivingHurtEvent event){
        if(event.getEntity().level() instanceof ServerLevel level) {
            if (event.getEntity().hasEffect(IMMEffects.SOLIDIFICATION.get())) {
                if (event.getAmount() >= event.getEntity().getMaxHealth() * 0.1F) {
                    event.getEntity().removeEffect(IMMEffects.SOLIDIFICATION.get());
                    event.setAmount(event.getAmount() * 1.1F);
                    ParticleHelper.spawnParticles(level, ParticleUtil.block(Blocks.DIRT.defaultBlockState()), event.getEntity().getEyePosition(), 20, 0, 0.15);
                    event.getEntity().playSound(SoundEvents.GRAVEL_BREAK);
                } else {
                    ParticleHelper.spawnParticles(level, ParticleUtil.block(Blocks.DIRT.defaultBlockState()), event.getEntity().getEyePosition(), 10, 0, 0.15);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event){
        if(EntityHelper.isServer(event.getEntity())){
            ElementManager.ifActiveReaction(event.getEntity(), ElementReactions.CUTTING, scale -> {
                event.setAmount(event.getAmount() + scale * 0.8F);
            });
        }
    }

    /**
     * 优先级最低，确保此时没有事件取消格挡（也就是盾一定会格挡）。
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onShieldBlock(ShieldBlockEvent event){
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
            if(BreakThroughTrial.checkTrialFail(player)){
                ev.setCanceled(true);
                return;
            }
        }
        // Cause by player.
        if(ev.getSource().getEntity() instanceof ServerPlayer player) {
            RealmManager.onPlayerKillLiving(player, ev.getEntity());
        }
    }

}
