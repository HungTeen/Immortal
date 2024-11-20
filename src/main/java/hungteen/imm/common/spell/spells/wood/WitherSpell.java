package hungteen.imm.common.spell.spells.wood;

import hungteen.htlib.util.helper.impl.EffectHelper;
import hungteen.imm.api.HTHitResult;
import hungteen.imm.api.enums.SpellUsageCategories;
import hungteen.imm.common.spell.spells.SpellType;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/10/7 22:57
 **/
public class WitherSpell extends SpellType {

    public WitherSpell() {
        super("wither", properties(SpellUsageCategories.DEBUFF_TARGET).maxLevel(1).mana(50).cd(450));
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        if(result.getEntity() instanceof LivingEntity living){
            living.addEffect(EffectHelper.viewEffect(MobEffects.WITHER, 320, 0));
            return true;
        }
        return false;
    }
}
