package hungteen.imm.common.cultivation.spell.wood;

import hungteen.imm.api.spell.SpellCastContext;
import hungteen.imm.api.spell.SpellUsageCategory;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-08-28 20:38
 **/
public class WoodHealingSpell extends SpellTypeImpl {

    public WoodHealingSpell() {
        super("wood_healing", property(SpellUsageCategory.BUFF_SELF).maxLevel(1).qi(40).cd(400));
    }

    @Override
    public boolean checkActivate(SpellCastContext context) {
        return super.checkActivate(context);
    }

//    @Override
//    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
//        owner.addEffect(EffectHelper.viewEffect(MobEffects.REGENERATION, 300, 0));
//        ElementManager.addElementAmount(owner, Element.WOOD, true, 15);
//        return true;
//    }

}
