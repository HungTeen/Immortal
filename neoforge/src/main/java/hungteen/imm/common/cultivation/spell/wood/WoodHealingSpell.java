package hungteen.imm.common.cultivation.spell.wood;

import hungteen.htlib.util.helper.impl.EffectHelper;
import hungteen.imm.api.HTHitResult;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.spell.SpellUsageCategory;
import hungteen.imm.common.cultivation.ElementManager;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-08-28 20:38
 **/
public class WoodHealingSpell extends SpellTypeImpl {

    public WoodHealingSpell() {
        super("wood_healing", properties(SpellUsageCategory.BUFF_SELF).maxLevel(1).mana(40).cd(400));
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        owner.addEffect(EffectHelper.viewEffect(MobEffects.REGENERATION, 300, 0));
        ElementManager.addElementAmount(owner, Element.WOOD, true, 15);
        return true;
    }

}
