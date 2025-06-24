package hungteen.imm.common.cultivation.spell.earth;

import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.spell.SpellCastContext;
import hungteen.imm.common.cultivation.InscriptionTypes;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import hungteen.imm.common.entity.misc.ElementAmethyst;
import net.minecraft.world.phys.AABB;

import java.util.List;

/**
 * @program Immortal
 * @author PangTeen
 * @create 2023/10/9 22:23
 **/
public class CrystalExplosionSpell extends SpellTypeImpl {

    public CrystalExplosionSpell() {
        super("crystal_explosion", property(InscriptionTypes.ANY).maxLevel(1).qi(100).cd(400));
    }

    @Override
    public boolean checkActivate(SpellCastContext context) {
        final AABB aabb = EntityHelper.getEntityAABB(context.owner(), 8F, 3F);
        final List<ElementAmethyst> amethysts = EntityHelper.getPredicateEntities(context.owner(), aabb, ElementAmethyst.class, EntityHelper::isEntityValid);
        if (!amethysts.isEmpty()) {
            amethysts.forEach(ElementAmethyst::explode);
            return true;
        } else {
            sendTip(context, NO_CRYSTAL_AROUND);
            return false;
        }
    }

}
