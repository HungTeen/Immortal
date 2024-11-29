package hungteen.imm.common.cultivation.spell.wood;

import hungteen.htlib.util.helper.impl.EffectHelper;
import hungteen.imm.api.HTHitResult;
import hungteen.imm.api.spell.SpellUsageCategory;
import hungteen.imm.common.cultivation.spell.RequireEmptyHandSpell;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

/**
 * @program Immortal
 * @author PangTeen
 * @create 2023/10/7 22:57
 **/
public class WitherSpell extends RequireEmptyHandSpell {

    public WitherSpell() {
        super("wither", properties(SpellUsageCategory.DEBUFF_TARGET).maxLevel(1).mana(50).cd(450));
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, InteractionHand hand, int level) {
        if(result.getEntity() instanceof LivingEntity living){
            living.addEffect(EffectHelper.viewEffect(MobEffects.WITHER, 320, 0));
            return true;
        }
        return false;
    }
}
