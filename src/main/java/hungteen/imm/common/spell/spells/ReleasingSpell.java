package hungteen.imm.common.spell.spells;

import hungteen.imm.api.HTHitResult;
import net.minecraft.world.entity.LivingEntity;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/8/18 14:25
 */
public class ReleasingSpell extends SpellType {

    public ReleasingSpell() {
        super("releasing", properties().maxLevel(1).mana(50).cd(200));
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        return true;
    }

}
