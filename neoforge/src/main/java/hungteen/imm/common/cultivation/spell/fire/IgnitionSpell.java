package hungteen.imm.common.cultivation.spell.fire;

import hungteen.htlib.util.helper.impl.ParticleHelper;
import hungteen.imm.api.HTHitResult;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.spell.SpellUsageCategory;
import hungteen.imm.client.particle.IMMParticles;
import hungteen.imm.common.cultivation.ElementManager;
import hungteen.imm.common.cultivation.SpellManager;
import hungteen.imm.common.cultivation.SpellTypes;
import hungteen.imm.common.cultivation.spell.SpellResult;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.HitResult;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-08-27 19:30
 **/
public class IgnitionSpell extends SpellTypeImpl {

    public IgnitionSpell() {
        super("ignition", properties(SpellUsageCategory.DEBUFF_TARGET).mana(5).cd(60).maxLevel(1));
    }

    /**
     * 投掷物碰撞瞬间判断燃烧。
     * @param projectile
     * @param hitResult
     */
    public static void checkIgnitionArrow(Projectile projectile, HitResult hitResult){
        if(projectile instanceof AbstractArrow arrow && arrow.getOwner() instanceof Player player){
            SpellManager.activateSpell(player, SpellTypes.IGNITION, (p, result, spell, level) -> {
                ElementManager.addElementAmount(result.getEntity(), Element.FIRE, false, 10);
                arrow.setRemainingFireTicks(100);
                return SpellResult.success();
            });
        }
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        if(result.hasEntity() && result.getEntity() != null){
            ElementManager.addElementAmount(result.getEntity(), Element.FIRE, false, 10);
            result.getEntity().setRemainingFireTicks(60);
            ParticleHelper.spawnLineMovingParticle(owner.level(), IMMParticles.QI.get(), owner.getEyePosition(), result.getEntity().getEyePosition(), 1, 0.1, 0.1);
            return true;
        }
        return false;
    }

}
