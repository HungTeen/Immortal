package hungteen.imm.common.cultivation.spell.wood;

import hungteen.htlib.util.helper.impl.EffectHelper;
import hungteen.imm.api.spell.SpellCastContext;
import hungteen.imm.common.cultivation.InscriptionTypes;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-08-28 20:38
 **/
public class LevitationSpell extends SpellTypeImpl {

    public LevitationSpell() {
        super("levitation", property(InscriptionTypes.ANY).maxLevel(1).qi(10).cd(40));
    }

    @Override
    public boolean checkActivate(SpellCastContext context) {
        context.owner().addEffect(EffectHelper.viewEffect(MobEffects.LEVITATION, Mth.ceil(30 * context.scale()), 1));
        context.owner().addEffect(EffectHelper.viewEffect(MobEffects.SLOW_FALLING, Mth.ceil(60 * context.scale()), 2));
        return true;
    }

}
