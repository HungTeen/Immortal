package hungteen.imm.common.spell.spells;

import hungteen.htlib.util.helper.registry.EffectHelper;
import hungteen.imm.api.HTHitResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/8/18 16:20
 */
public class WaterBreatheSpell extends SpellType {

    public WaterBreatheSpell() {
        super("water_breathe", properties().maxLevel(1).mana(20).cd(3000));
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        owner.addEffect(EffectHelper.viewEffect(MobEffects.WATER_BREATHING, 2400, 2));
        return true;
    }

}
