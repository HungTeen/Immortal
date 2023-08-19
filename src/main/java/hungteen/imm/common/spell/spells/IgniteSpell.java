package hungteen.imm.common.spell.spells;

import hungteen.htlib.util.helper.registry.ParticleHelper;
import hungteen.imm.api.HTHitResult;
import hungteen.imm.api.enums.Elements;
import hungteen.imm.client.particle.IMMParticles;
import net.minecraft.world.entity.LivingEntity;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/8/19 16:58
 */
public class IgniteSpell extends SpellType {
    public IgniteSpell() {
        super("ignite", properties().mana(25).cd(200).maxLevel(1));
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        if(result.hasEntity() && result.getEntity() != null){
            ElementalMasterySpell.getElementEntry(owner, Elements.FIRE, false).ifPresent(e -> e.addElement(result.getEntity(), 15F));
            result.getEntity().setSecondsOnFire(10);
            ParticleHelper.spawnLineMovingParticle(owner.level(), IMMParticles.SPIRIT.get(), owner.getEyePosition(), result.getEntity().getEyePosition(), 1, 0.1, 0.1);
            return true;
        }
        return false;
    }
}
