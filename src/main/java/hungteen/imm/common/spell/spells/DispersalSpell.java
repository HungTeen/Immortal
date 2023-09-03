package hungteen.imm.common.spell.spells;

import hungteen.imm.api.HTHitResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/8/18 14:25
 */
public class DispersalSpell extends SpellType {

    public DispersalSpell() {
        super("dispersal", properties().maxLevel(1).mana(30).cd(200));
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        if(owner instanceof Player player){
            ElementalMasterySpell.addElement(player, true, false, 10);
            return true;
        }
        return false;
    }

}
