package hungteen.imm.common.cultivation.spell.wood;

import hungteen.htlib.util.helper.impl.EffectHelper;
import hungteen.imm.api.HTHitResult;
import hungteen.imm.api.spell.SpellUsageCategory;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/5 16:59
 **/
public class SpeedSpell extends SpellTypeImpl {

    public SpeedSpell() {
        super("speed", properties(SpellUsageCategory.BUFF_SELF).maxLevel(1).mana(15).cd(200));
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        owner.addEffect(EffectHelper.viewEffect(MobEffects.MOVEMENT_SPEED, 100, 2));
        return true;
    }

}
