package hungteen.imm.common.cultivation.spell.metal;

import hungteen.imm.common.cultivation.SpellManager;
import hungteen.imm.common.cultivation.SpellTypes;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;

/**
 * @program Immortal
 * @author PangTeen
 * @create 2023/10/7 20:20
 **/
public class CriticalHitSpell extends SpellTypeImpl {

    public CriticalHitSpell() {
        super("critical_hit", properties().maxLevel(1).mana(20).cd(40));
    }

    public static void checkCriticalHit(Player owner, CriticalHitEvent event) {
        SpellManager.activateSpell(owner, SpellTypes.CRITICAL_HIT, (p, result, spell, level) -> {
            if(! event.isVanillaCritical()){
                event.setDamageMultiplier(1.5F);
                return true;
            }
            return false;
        });
    }

}
