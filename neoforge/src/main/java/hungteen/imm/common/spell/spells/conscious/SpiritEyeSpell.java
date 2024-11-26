package hungteen.imm.common.spell.spells.conscious;

import hungteen.imm.common.spell.SpellTypes;
import hungteen.imm.common.spell.spells.SpellType;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.world.entity.player.Player;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/8/18 15:26
 */
public class SpiritEyeSpell extends SpellType {

    public SpiritEyeSpell() {
        super("spirit_eyes", properties().maxLevel(2).notTrigger());
    }

    public static boolean knowOwnSpiritRoots(Player player){
        return PlayerUtil.knowRoots(player) || PlayerUtil.hasLearnedSpell(player, SpellTypes.SPIRIT_EYES);
    }

}
