package hungteen.imm.common.cultivation.spell.wood;

import hungteen.htlib.util.helper.impl.EffectHelper;
import hungteen.imm.api.spell.SpellCastContext;
import hungteen.imm.api.spell.SpellUsageCategory;
import hungteen.imm.common.cultivation.InscriptionTypes;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/5 16:59
 **/
public class SpeedSpell extends SpellTypeImpl {

    public SpeedSpell() {
        super("speed", property(SpellUsageCategory.BUFF_SELF, InscriptionTypes.ANY).maxLevel(1).qi(15).cd(200));
    }

    @Override
    public boolean checkActivate(SpellCastContext context) {
        context.owner().addEffect(EffectHelper.viewEffect(MobEffects.MOVEMENT_SPEED, Mth.ceil(200 * context.scale()), 2));
        return true;
    }

}
