package hungteen.imm.common.spell.spells;

import hungteen.htlib.util.helper.registry.EffectHelper;
import hungteen.imm.api.HTHitResult;
import hungteen.imm.api.enums.SpellCategories;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/10/7 22:57
 **/
public class WitherSpell extends SpellType{

    public WitherSpell() {
        super("wither", properties(SpellCategories.DEBUFF_TARGET).maxLevel(1).mana(50).cd(450));
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        if(result.getEntity() instanceof LivingEntity living){
            living.addEffect(EffectHelper.viewEffect(MobEffects.WITHER, 160, 0));
            return true;
        }
        return false;
    }
}
