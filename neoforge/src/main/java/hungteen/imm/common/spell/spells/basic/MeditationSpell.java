package hungteen.imm.common.spell.spells.basic;

import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.HTHitResult;
import hungteen.imm.common.spell.spells.SpellType;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/3/6 19:11
 */
public class MeditationSpell extends SpellType {

    public MeditationSpell() {
        super("meditation", properties().maxLevel(1).mana(0).cd(600));
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        if(owner instanceof Player player){
            return PlayerUtil.sitToMeditate(player, player.blockPosition(), (float) (player.getY() - player.blockPosition().getY()), false);
        } else if(EntityHelper.isEntityValid(owner)){
            EntityUtil.sitToMeditate(owner, owner.blockPosition(), (float) (owner.getY() - owner.blockPosition().getY()), false);
            return true;
        }
        return false;
    }

}
