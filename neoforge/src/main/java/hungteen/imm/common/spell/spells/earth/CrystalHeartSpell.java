package hungteen.imm.common.spell.spells.earth;

import hungteen.htlib.util.helper.impl.EffectHelper;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.HTHitResult;
import hungteen.imm.common.entity.misc.ElementCrystal;
import hungteen.imm.common.spell.spells.SpellType;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

import java.util.Comparator;
import java.util.List;

/**
 * @program Immortal
 * @author PangTeen
 * @create 2023/10/9 22:23
 **/
public class CrystalHeartSpell extends SpellType {

    private static final int MAX_COUNT = 6;

    public CrystalHeartSpell() {
        super("crystal_heart", properties().maxLevel(1).mana(60).cd(200));
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        final AABB aabb = EntityHelper.getEntityAABB(owner, 8F, 3F);
        final List<ElementCrystal> amethysts = EntityHelper.getPredicateEntities(owner, aabb, ElementCrystal.class, EntityHelper::isEntityValid)
                .stream().sorted(Comparator.comparingDouble(e -> e.distanceTo(owner)))
                .limit(MAX_COUNT).toList();
        if (!amethysts.isEmpty()) {
            owner.addEffect(EffectHelper.viewEffect(MobEffects.ABSORPTION, 12000, amethysts.size()));
            amethysts.forEach(ElementCrystal::breakAmethyst);
            return true;
        } else {
            this.sendTip(owner, "no_crystal_around");
            return false;
        }
    }

}
