package hungteen.imm.common.cultivation.spell.fire;

import hungteen.htlib.util.helper.impl.EffectHelper;
import hungteen.imm.api.spell.SpellCastContext;
import hungteen.imm.api.spell.SpellUsageCategory;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/8/18 16:20
 */
public class LavaBreathingSpell extends SpellTypeImpl {

    public LavaBreathingSpell() {
        super("lava_breathing", property(SpellUsageCategory.BUFF_SELF).maxLevel(1).qi(80).cd(400));
    }

    @Override
    public boolean checkActivate(SpellCastContext context) {
        context.owner().addEffect(EffectHelper.viewEffect(MobEffects.FIRE_RESISTANCE, 600, 2));
        return true;
    }

    @Override
    public int getCastingPriority(LivingEntity living) {
        return living.isOnFire() ? VERY_HIGH : VERY_LOW;
    }
}
