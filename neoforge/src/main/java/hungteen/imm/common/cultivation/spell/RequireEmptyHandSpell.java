package hungteen.imm.common.cultivation.spell;

import hungteen.imm.api.HTHitResult;
import hungteen.imm.util.EntityUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/29 14:39
 **/
public abstract class RequireEmptyHandSpell extends SpellTypeImpl {

    public RequireEmptyHandSpell(String name, SpellProperties properties) {
        super(name, properties);
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        Optional<InteractionHand> handOpt = EntityUtil.getEmptyHand(owner);
        if(handOpt.isEmpty()){
            this.sendTip(owner, "no_empty_hand");
            return false;
        }
        if(checkActivate(owner, result, handOpt.get(), level)){
            owner.swing(handOpt.get());
            return true;
        }
        return false;
    }

    public abstract boolean checkActivate(LivingEntity owner, HTHitResult result, InteractionHand hand, int level);

}
