package hungteen.imm.common.spell.spells.earth;

import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.imm.api.HTHitResult;
import hungteen.imm.common.entity.misc.ElementAmethyst;
import hungteen.imm.common.spell.spells.SpellType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

import java.util.List;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/10/9 22:23
 **/
public class AmethystExplosionSpell extends SpellType {

    public AmethystExplosionSpell() {
        super("amethyst_explosion", properties().maxLevel(1).mana(100).cd(400));
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        final AABB aabb = EntityHelper.getEntityAABB(owner, 8F, 3F);
        final List<ElementAmethyst> amethysts = EntityHelper.getPredicateEntities(owner, aabb, ElementAmethyst.class, EntityHelper::isEntityValid);
        if (!amethysts.isEmpty()) {
            amethysts.forEach(ElementAmethyst::explode);
            return true;
        } else {
            this.sendTip(owner, "no_amethyst_around");
            return false;
        }
    }
}
