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
public class LavaBreathingSpell extends SpellType {

    public LavaBreathingSpell() {
        super("lava_breathing", properties().maxLevel(1).mana(80).cd(400));
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        owner.addEffect(EffectHelper.viewEffect(MobEffects.FIRE_RESISTANCE, 600, 2));
        return true;
    }

}
