package hungteen.imm.common.spell.spells;

import hungteen.imm.common.spell.SpellManager;
import hungteen.imm.common.spell.SpellTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.Event;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/10/7 20:20
 **/
public class CriticalHitSpell extends SpellType {

    public CriticalHitSpell() {
        super("critical_hit", properties().maxLevel(1).mana(20).cd(40));
    }

    public static void checkCriticalHit(Player owner, CriticalHitEvent event) {
        SpellManager.activateSpell(owner, SpellTypes.CRITICAL_HIT, (p, result, spell, level) -> {
            if(! event.isVanillaCritical()){
                event.setDamageModifier(1.5F);
                event.setResult(Event.Result.ALLOW);
                return true;
            }
            return false;
        });
    }

}
