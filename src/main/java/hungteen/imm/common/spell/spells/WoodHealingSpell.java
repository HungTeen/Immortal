package hungteen.imm.common.spell.spells;

import hungteen.htlib.util.helper.registry.EffectHelper;
import hungteen.imm.api.HTHitResult;
import hungteen.imm.api.enums.Elements;
import hungteen.imm.api.enums.SpellCategories;
import hungteen.imm.common.ElementManager;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-08-28 20:38
 **/
public class WoodHealingSpell extends SpellType {

    public WoodHealingSpell() {
        super("wood_healing", properties(SpellCategories.BUFF_SELF).maxLevel(1).mana(40).cd(500));
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        owner.addEffect(EffectHelper.viewEffect(MobEffects.HEAL, 200, 0));
        ElementManager.addElementAmount(owner, Elements.WOOD, true, 15);
        return true;
    }

}
