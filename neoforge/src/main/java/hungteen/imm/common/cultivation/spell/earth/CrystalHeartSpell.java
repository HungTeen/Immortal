package hungteen.imm.common.cultivation.spell.earth;

import hungteen.htlib.util.helper.impl.EffectHelper;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.spell.SpellCastContext;
import hungteen.imm.common.cultivation.InscriptionTypes;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import hungteen.imm.common.entity.misc.ElementAmethyst;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.AABB;

import java.util.Comparator;
import java.util.List;

/**
 * @program Immortal
 * @author PangTeen
 * @create 2023/10/9 22:23
 **/
public class CrystalHeartSpell extends SpellTypeImpl {

    private static final int MAX_COUNT = 6;

    public CrystalHeartSpell() {
        super("crystal_heart", property(InscriptionTypes.ANY).maxLevel(1).qi(60).cd(200));
    }

    @Override
    public boolean checkActivate(SpellCastContext context) {
        final AABB aabb = EntityHelper.getEntityAABB(context.owner(), 8F, 3F);
        final List<ElementAmethyst> amethysts = EntityHelper.getPredicateEntities(context.owner(), aabb, ElementAmethyst.class, EntityHelper::isEntityValid)
                .stream().sorted(Comparator.comparingDouble(e -> e.distanceTo(context.owner())))
                .limit(MAX_COUNT).toList();
        if (!amethysts.isEmpty()) {
            context.owner().addEffect(EffectHelper.viewEffect(MobEffects.ABSORPTION, 12000, amethysts.size()));
            amethysts.forEach(ElementAmethyst::breakAmethyst);
            return true;
        } else {
            sendTip(context, NO_CRYSTAL_AROUND);
            return false;
        }
    }

}
