package hungteen.imm.common.cultivation.spell;

import hungteen.imm.api.spell.SpellCastContext;
import hungteen.imm.util.EntityUtil;
import net.minecraft.world.InteractionHand;

import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/29 14:39
 **/
public abstract class RequireEmptyHandSpell extends SpellTypeImpl {

    public RequireEmptyHandSpell(String name, SpellProperty properties) {
        super(name, properties);
    }

    @Override
    public boolean checkActivate(SpellCastContext context) {
        Optional<InteractionHand> handOpt = EntityUtil.getEmptyHand(context.owner());
        if(handOpt.isEmpty()){
            sendTip(context, NO_EMPTY_HAND);
            return false;
        }
        if(checkActivate(context, handOpt.get())){
            context.owner().swing(handOpt.get());
            return true;
        }
        return false;
    }

    public abstract boolean checkActivate(SpellCastContext context, InteractionHand hand);

}
