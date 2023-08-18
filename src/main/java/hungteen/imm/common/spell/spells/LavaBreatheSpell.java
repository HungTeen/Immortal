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
public class LavaBreatheSpell extends SpellType {

    public LavaBreatheSpell() {
        super("lava_breathe", properties().maxLevel(1).mana(40).cd(3600));
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        owner.addEffect(EffectHelper.viewEffect(MobEffects.FIRE_RESISTANCE, 3000, 2));
        return true;
    }

}
