package hungteen.imm.common.cultivation.spell.wood;

import hungteen.htlib.util.helper.impl.EffectHelper;
import hungteen.imm.api.spell.SpellCastContext;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import net.minecraft.world.effect.MobEffects;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-08-28 20:38
 **/
public class LevitationSpell extends SpellTypeImpl {

    public LevitationSpell() {
        super("levitation", property().maxLevel(1).mana(10).cd(35));
    }

    @Override
    public boolean checkActivate(SpellCastContext context) {
        context.owner().addEffect(EffectHelper.viewEffect(MobEffects.LEVITATION, 30, 1));
        context.owner().addEffect(EffectHelper.viewEffect(MobEffects.SLOW_FALLING, 60, 2));
        return true;
    }

}
