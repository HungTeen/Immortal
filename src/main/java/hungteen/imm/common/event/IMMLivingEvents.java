package hungteen.imm.common.event;

import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.htlib.util.helper.registry.ParticleHelper;
import hungteen.imm.ImmortalMod;
import hungteen.imm.api.interfaces.IHasMana;
import hungteen.imm.common.ElementManager;
import hungteen.imm.common.RealmManager;
import hungteen.imm.common.effect.IMMEffects;
import hungteen.imm.common.impl.registry.ElementReactions;
import hungteen.imm.common.world.entity.trial.BreakThroughTrial;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.LevelUtil;
import hungteen.imm.util.ParticleUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
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
            if(event.getEntity() instanceof IHasMana entity && EntityUtil.canManaIncrease(event.getEntity())) {
                if(! entity.isManaFull()){
                    entity.addMana(LevelUtil.getSpiritualRate(event.getEntity().level(), event.getEntity().blockPosition()));
                }
            }
            ElementManager.ifActiveReaction(event.getEntity(), ElementReactions.PARASITISM, scale -> {
                if(EntityUtil.hasMana(event.getEntity())){
                    EntityUtil.addMana(event.getEntity(), - scale * 5);
                } else {
                }
            });
        }
    }

    @SubscribeEvent
    public static void tick(LivingEvent.LivingJumpEvent event) {
        if(event.getEntity().hasEffect(IMMEffects.AGGLOMERATION.get())){
            final Vec3 speed = event.getEntity().getDeltaMovement();
            event.getEntity().setDeltaMovement(speed.x(), Math.min(speed.y(), 0), speed.z());
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event){
        if(event.getEntity().level() instanceof ServerLevel level && event.getEntity().hasEffect(IMMEffects.AGGLOMERATION.get())){
            if(event.getAmount() >= event.getEntity().getMaxHealth() * 0.1F){
                event.getEntity().removeEffect(IMMEffects.AGGLOMERATION.get());
                event.setAmount(event.getAmount() * 1.1F);
                ParticleHelper.spawnParticles(level, ParticleUtil.block(Blocks.DIRT.defaultBlockState()), event.getEntity().getEyePosition(), 20, 0, 0.15);
                event.getEntity().playSound(SoundEvents.GRAVEL_BREAK);
            } else {
                ParticleHelper.spawnParticles(level, ParticleUtil.block(Blocks.DIRT.defaultBlockState()), event.getEntity().getEyePosition(), 10, 0, 0.15);
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
