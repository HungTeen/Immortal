package hungteen.imm.common.spell.spells.wood;

import hungteen.htlib.util.helper.impl.EffectHelper;
import hungteen.imm.api.HTHitResult;
import hungteen.imm.api.enums.Elements;
import hungteen.imm.api.enums.SpellUsageCategories;
import hungteen.imm.common.ElementManager;
import hungteen.imm.common.spell.spells.SpellType;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-08-28 20:38
 **/
public class WoodHealingSpell extends SpellType {

    public WoodHealingSpell() {
        super("wood_healing", properties(SpellUsageCategories.BUFF_SELF).maxLevel(1).mana(40).cd(400));
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        owner.addEffect(EffectHelper.viewEffect(MobEffects.REGENERATION, 300, 0));
        ElementManager.addElementAmount(owner, Elements.WOOD, true, 15);
        return true;
    }

}
