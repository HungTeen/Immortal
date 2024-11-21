package hungteen.imm.common.spell.spells.wood;

import hungteen.htlib.util.helper.impl.EffectHelper;
import hungteen.imm.api.HTHitResult;
import hungteen.imm.common.spell.spells.SpellType;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-08-28 20:38
 **/
public class LevitationSpell extends SpellType {

    public LevitationSpell() {
        super("levitation", properties().maxLevel(1).mana(10).cd(35));
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        owner.addEffect(EffectHelper.viewEffect(MobEffects.LEVITATION, 30, 1));
        owner.addEffect(EffectHelper.viewEffect(MobEffects.SLOW_FALLING, 60, 2));
        return true;
    }
}
