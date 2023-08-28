package hungteen.imm.common.spell.spells;

import hungteen.htlib.util.helper.registry.EffectHelper;
import hungteen.imm.api.HTHitResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-08-28 20:38
 **/
public class LevitationSpell extends SpellType {

    public LevitationSpell() {
        super("levitation", properties().maxLevel(1).mana(5).cd(10));
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        owner.addEffect(EffectHelper.viewEffect(MobEffects.LEVITATION, 30, 1));
        owner.addEffect(EffectHelper.viewEffect(MobEffects.SLOW_FALLING, 60, 2));
        return true;
    }
}
