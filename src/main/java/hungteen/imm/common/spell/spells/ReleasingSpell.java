package hungteen.imm.common.spell.spells;

import hungteen.htlib.util.helper.registry.ParticleHelper;
import hungteen.imm.api.HTHitResult;
import hungteen.imm.client.particle.IMMParticles;
import hungteen.imm.common.misc.damage.IMMDamageSources;
import hungteen.imm.util.EntityUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/8/18 14:25
 */
public class ReleasingSpell extends SpellType {

    public ReleasingSpell() {
        super("releasing", properties().maxLevel(1).mana(20).cd(160));
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        if(result.hasEntity() && result.getEntity() != null && owner instanceof Player player){
            if(! EntityUtil.hasEmptyHand(owner)){
                this.sendTip(owner, "no_empty_hand");
                return false;
            }
            ElementalMasterySpell.addElement(player, false, false, 10F);
            result.getEntity().hurt(IMMDamageSources.spiritualMana(owner), 3F);
            ParticleHelper.spawnLineMovingParticle(owner.level(), IMMParticles.SPIRITUAL_MANA.get(), owner.getEyePosition(), result.getEntity().getEyePosition(), 1, 0.1, 0.1);
            return true;
        }
        return false;
    }

}
