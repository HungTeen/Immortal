package hungteen.imm.common.event;

import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.api.event.ActivateSpellEvent;
import hungteen.imm.common.cultivation.*;
import hungteen.imm.common.cultivation.reaction.GenerationReaction;
import hungteen.imm.common.cultivation.reaction.GildingReaction;
import hungteen.imm.common.cultivation.reaction.InhibitionReaction;
import hungteen.imm.common.cultivation.spell.basic.ElementalMasterySpell;
import hungteen.imm.common.cultivation.spell.metal.CriticalHitSpell;
import hungteen.imm.common.cultivation.spell.metal.SharpnessSpell;
import hungteen.imm.common.entity.effect.IMMEffects;
import hungteen.imm.common.entity.misc.ThrowingItemEntity;
import hungteen.imm.common.event.handler.LivingEventHandler;
import hungteen.imm.util.DamageUtil;
import hungteen.imm.util.EntityUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.*;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-09-25 16:32
 **/
@EventBusSubscriber(modid = IMMAPI.MOD_ID)
public class IMMLivingEvents {

    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
        InhibitionReaction.blockSolidificationJump(event.getEntity());
    }

    /**
     * 被攻击（先触发）。
     */
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void firstIncomeDamage(LivingIncomingDamageEvent event){
        if(event.getEntity().level() instanceof ServerLevel level) {
            LivingEventHandler.attachElementWhenImpact(event.getEntity(), event.getSource());
        }
    }

    @SubscribeEvent
    public static void incomeDamage(LivingIncomingDamageEvent event){
        if(event.getEntity().level() instanceof ServerLevel level) {
            // 被攻击的生物。
            LivingEntity hurtEntity = event.getEntity();
            SpellManager.activateSpell(hurtEntity, TriggerConditions.HURT);

            // 攻击者。
            Entity attacker = event.getSource().getEntity();
            SharpnessSpell.checkSharpening(attacker, event);
            if(DamageUtil.isMeleeDamage(event.getSource()) && attacker != null){
                if(attacker instanceof LivingEntity livingAttacker){
                    SpellManager.activateSpell(livingAttacker, TriggerConditions.ATTACK, context -> {
                        context.setEvent(event);
                    });
                    CriticalHitSpell.checkCriticalHit(livingAttacker, event);
                    ElementalMasterySpell.checkActivateMetal(livingAttacker, event.getEntity());
                }
                GildingReaction.ifGlidingActive(attacker, event.getEntity());
                GenerationReaction.quenchBlade(event, attacker);
            }
        }
    }

    /**
     * 被攻击（晚触发）。
     */
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void lastIncomeDamage(LivingIncomingDamageEvent event){
        InhibitionReaction.breakSolidification(event);
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

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void preLivingDamageHighly(LivingDamageEvent.Pre event){
        if(event.getEntity().level() instanceof ServerLevel level) {
            RealmManager.realmAttackGap(event);
        }
    }

    @SubscribeEvent
    public static void preLivingDamage(LivingDamageEvent.Pre event){
        if(event.getEntity().level() instanceof ServerLevel level) {
            InhibitionReaction.cut(event);
        }
    }

    @SubscribeEvent
    public static void postLivingDamage(LivingDamageEvent.Post event){
        if(event.getEntity().level() instanceof ServerLevel level) {
            ElementManager.attachDamageElement(level, event.getEntity(), event.getSource());
            GenerationReaction.markDamage(event.getEntity(), event.getNewDamage());
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

    @SubscribeEvent
    public static void onEffectExpired(MobEffectEvent.Expired event) {
        // 超时时随机扣除突破进度。
        if(event.getEffectInstance().is(IMMEffects.BREAK_THROUGH.holder())) {
            if(event.getEntity() instanceof ServerPlayer serverPlayer){
                float random = serverPlayer.getRandom().nextFloat();
                CultivationManager.addBreakThroughProgress(serverPlayer, - random);
            }
        }
    }

    @SubscribeEvent
    public static void onSpellCast(ActivateSpellEvent.Post event) {
        if(!event.getEntity().level().isClientSide()) {
            SpellManager.activateSpell(event.getEntity(), TriggerConditions.CAST, context -> {
                context.setEvent(event);
            });
        }
    }


}
