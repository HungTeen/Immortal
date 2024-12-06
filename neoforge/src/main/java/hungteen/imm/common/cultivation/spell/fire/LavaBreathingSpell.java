package hungteen.imm.common.cultivation.spell.fire;

import hungteen.htlib.util.helper.impl.EffectHelper;
import hungteen.imm.api.HTHitResult;
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
        super("lava_breathing", properties(SpellUsageCategory.BUFF_SELF).maxLevel(1).mana(80).cd(400));
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        owner.addEffect(EffectHelper.viewEffect(MobEffects.FIRE_RESISTANCE, 600, 2));
        return true;
    }

}
