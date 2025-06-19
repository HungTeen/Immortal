package hungteen.imm.common.cultivation.spell.basic;

import hungteen.htlib.util.helper.impl.ParticleHelper;
import hungteen.imm.api.spell.SpellCastContext;
import hungteen.imm.client.particle.IMMParticles;
import hungteen.imm.common.cultivation.spell.RequireEmptyHandSpell;
import hungteen.imm.util.DamageUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/8/18 14:25
 */
public class ReleasingSpell extends RequireEmptyHandSpell {

    public ReleasingSpell() {
        super("releasing", property().maxLevel(1).qi(20).cd(160));
    }

    @Override
    public boolean checkActivate(SpellCastContext context, InteractionHand hand) {
        if(context.targetOpt().isPresent() && context.owner() instanceof Player player) {
            ElementalMasterySpell.addElement(player, context.targetOpt().get(), false, false, 10F * context.scale());
            context.targetOpt().get().hurt(DamageUtil.qi(context.owner()), 3F * context.scale());
            ParticleHelper.spawnLineMovingParticle(context.owner().level(), IMMParticles.QI.get(), context.owner().getEyePosition(), context.targetOpt().get().getEyePosition(), 1, 0.1, 0.1);
            return true;
        }
        return false;
    }

}
