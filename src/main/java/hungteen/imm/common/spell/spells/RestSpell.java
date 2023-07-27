package hungteen.imm.common.spell.spells;

import hungteen.htlib.common.entity.SeatEntity;
import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.imm.api.EntityBlockResult;
import hungteen.imm.common.spell.SpellTypes;
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
            SeatEntity.seatAt(owner.level(), owner, owner.blockPosition(), 0, owner.getYRot(), 30, true);
            return true;
        }
        return false;
    }

}
