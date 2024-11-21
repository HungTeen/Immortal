package hungteen.imm.common.spell.spells.basic;

import hungteen.htlib.util.helper.impl.ParticleHelper;
import hungteen.imm.api.HTHitResult;
import hungteen.imm.client.particle.IMMParticles;
import hungteen.imm.common.spell.spells.SpellType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/8/18 14:25
 */
public class DispersalSpell extends SpellType {

    public DispersalSpell() {
        super("dispersal", properties().maxLevel(1).mana(30).cd(200));
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        if(owner instanceof Player player && owner.level() instanceof ServerLevel serverLevel){
            ElementalMasterySpell.addElement(player, player, true, false, 10);
            ParticleHelper.spawnParticles(serverLevel, IMMParticles.SPIRITUAL_MANA.get(), owner.getX(), owner.getEyeY(), owner.getZ(), 10, owner.getBbWidth(), 0.5,0.1);
            return true;
        }
        return false;
    }

}
