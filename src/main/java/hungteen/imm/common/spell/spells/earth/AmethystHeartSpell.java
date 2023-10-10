package hungteen.imm.common.spell.spells.earth;

import hungteen.htlib.util.helper.registry.EffectHelper;
import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.imm.api.HTHitResult;
import hungteen.imm.common.entity.misc.ElementAmethyst;
import hungteen.imm.common.spell.spells.SpellType;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

import java.util.Comparator;
import java.util.List;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/10/9 22:23
 **/
public class AmethystHeartSpell extends SpellType {

    private static final int MAX_COUNT = 6;

    public AmethystHeartSpell() {
        super("amethyst_heart", properties().maxLevel(1).mana(60).cd(200));
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        final AABB aabb = EntityHelper.getEntityAABB(owner, 8F, 3F);
        final List<ElementAmethyst> amethysts = EntityHelper.getPredicateEntities(owner, aabb, ElementAmethyst.class, EntityHelper::isEntityValid)
                .stream().sorted(Comparator.comparingDouble(e -> e.distanceTo(owner)))
                .limit(MAX_COUNT).toList();
        if (!amethysts.isEmpty()) {
            owner.addEffect(EffectHelper.viewEffect(MobEffects.ABSORPTION, 12000, amethysts.size()));
            amethysts.forEach(ElementAmethyst::breakAmethyst);
            return true;
        } else {
            this.sendTip(owner, "no_amethyst_around");
            return false;
        }
    }

}
