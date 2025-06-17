package hungteen.imm.common.cultivation.spell.conscious;

import hungteen.imm.common.cultivation.SpellTypes;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.world.entity.player.Player;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/8/18 15:26
 */
public class SpiritEyeSpell extends SpellTypeImpl {

    public SpiritEyeSpell() {
        super("spirit_eyes", property().maxLevel(2).notTrigger());
    }

    public static boolean knowOwnSpiritRoots(Player player){
        return PlayerUtil.knowRoots(player) || PlayerUtil.hasLearnedSpell(player, SpellTypes.SPIRIT_EYES);
    }

}
