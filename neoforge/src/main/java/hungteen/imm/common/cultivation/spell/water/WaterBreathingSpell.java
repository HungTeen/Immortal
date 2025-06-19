package hungteen.imm.common.cultivation.spell.water;

import hungteen.htlib.util.helper.impl.EffectHelper;
import hungteen.imm.api.spell.SpellCastContext;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import net.minecraft.world.effect.MobEffects;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/8/18 16:20
 */
public class WaterBreathingSpell extends SpellTypeImpl {

    public WaterBreathingSpell() {
        super("water_breathing", property().maxLevel(1).qi(60).cd(300));
    }

    @Override
    public boolean checkActivate(SpellCastContext context) {
        context.owner().addEffect(EffectHelper.viewEffect(MobEffects.WATER_BREATHING, 400, 2));
        return true;
    }

}
