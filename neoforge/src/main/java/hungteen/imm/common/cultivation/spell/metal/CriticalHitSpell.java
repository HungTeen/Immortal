package hungteen.imm.common.cultivation.spell.metal;

import hungteen.htlib.util.helper.impl.ParticleHelper;
import hungteen.imm.api.spell.SpellUsageCategory;
import hungteen.imm.common.cultivation.SpellManager;
import hungteen.imm.common.cultivation.SpellTypes;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;

/**
 * @program Immortal
 * @author PangTeen
 * @create 2023/10/7 20:20
 **/
public class CriticalHitSpell extends SpellTypeImpl {

    public CriticalHitSpell() {
        super("critical_hit", properties(SpellUsageCategory.TRIGGERED_PASSIVE).maxLevel(1).mana(20).cd(40));
    }

    public static void checkCriticalHit(Player owner, CriticalHitEvent event) {
        SpellManager.activateSpell(owner, SpellTypes.CRITICAL_HIT, (p, result, spell, level) -> {
            if(! event.isVanillaCritical()){
                event.setDamageMultiplier(1.5F);
                spawnCriticalParticles(event.getTarget());
                playSoundAround(event.getTarget(), SoundEvents.PLAYER_ATTACK_CRIT);
                return true;
            }
            return false;
        });
    }

    /**
     * @param attacker 攻击者。
     * @param event 受伤事件。
     */
    public static void checkCriticalHit(Entity attacker, LivingIncomingDamageEvent event) {
        // 玩家不走此方法。
        if(attacker instanceof LivingEntity owner && !(attacker instanceof Player)){
            SpellManager.activateSpell(owner, SpellTypes.CRITICAL_HIT, (p, result, spell, level) -> {
                event.setAmount(event.getOriginalAmount() * 1.5F);
                spawnCriticalParticles(event.getEntity());
                playSoundAround(event.getEntity(), SoundEvents.PLAYER_ATTACK_CRIT);
                return true;
            });
        }
    }

    private static void spawnCriticalParticles(Entity entity) {
        if(entity.level() instanceof ServerLevel serverLevel){
            ParticleHelper.sendParticles(serverLevel, ParticleTypes.CRIT, entity.getX(), entity.getEyeY(), entity.getZ(), 10, 0.25, 0.25, 0.25, 0.1);
        }
    }

}
