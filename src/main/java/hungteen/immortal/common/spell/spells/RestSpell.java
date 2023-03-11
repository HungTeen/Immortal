package hungteen.immortal.common.spell.spells;

import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.immortal.api.EntityBlockResult;
import hungteen.immortal.common.entity.misc.SeatEntity;
import hungteen.immortal.common.spell.SpellTypes;
import net.minecraft.world.entity.LivingEntity;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/6 19:11
 */
public class RestSpell extends SpellTypes.SpellType {

    public RestSpell(SpellTypes.SpellProperties properties) {
        super("resting", properties);
    }

    @Override
    public boolean onActivate(LivingEntity owner, EntityBlockResult result, int level) {
        //TODO 可以调息的条件。
        if(EntityHelper.isEntityValid(owner)){
            SeatEntity.seatAt(owner.level, owner, owner.blockPosition(), 0, owner.getYRot());
            return true;
        }
        return false;
    }

    @Override
    public boolean isPassiveSpell() {
        return false;
    }
}
