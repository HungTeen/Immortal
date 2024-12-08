package hungteen.imm.common.cultivation.spell.basic;

import hungteen.htlib.util.helper.impl.ParticleHelper;
import hungteen.imm.api.HTHitResult;
import hungteen.imm.client.particle.IMMParticles;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import hungteen.imm.util.DamageUtil;
import hungteen.imm.util.EntityUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/8/18 14:25
 */
public class ReleasingSpell extends SpellTypeImpl {

    public ReleasingSpell() {
        super("releasing", properties().maxLevel(1).mana(20).cd(160));
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        if(result.getEntity() != null && owner instanceof Player player){
            Optional<InteractionHand> handOpt = EntityUtil.getEmptyHand(owner);
            if(handOpt.isEmpty()){
                this.sendTip(owner, "no_empty_hand");
                return false;
            }
            ElementalMasterySpell.addElement(player, result.getEntity(), false, false, 10F);
            result.getEntity().hurt(DamageUtil.qi(owner), 3F);

            player.swing(handOpt.get());
            ParticleHelper.spawnLineMovingParticle(owner.level(), IMMParticles.QI.get(), owner.getEyePosition(), result.getEntity().getEyePosition(), 1, 0.1, 0.1);
            return true;
        }
        return false;
    }

}
