package hungteen.imm.common.cultivation.spell.earth;

import hungteen.imm.api.spell.SpellCastContext;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;

/**
 * @program Immortal
 * @author PangTeen
 * @create 2023/10/9 22:23
 **/
public class CrystalHeartSpell extends SpellTypeImpl {

    private static final int MAX_COUNT = 6;

    public CrystalHeartSpell() {
        super("crystal_heart", property().maxLevel(1).mana(60).cd(200));
    }

    @Override
    public boolean checkActivate(SpellCastContext context) {
        return super.checkActivate(context);
    }

//    @Override
//    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
//        final AABB aabb = EntityHelper.getEntityAABB(owner, 8F, 3F);
//        final List<ElementAmethyst> amethysts = EntityHelper.getPredicateEntities(owner, aabb, ElementAmethyst.class, EntityHelper::isEntityValid)
//                .stream().sorted(Comparator.comparingDouble(e -> e.distanceTo(owner)))
//                .limit(MAX_COUNT).toList();
//        if (!amethysts.isEmpty()) {
//            owner.addEffect(EffectHelper.viewEffect(MobEffects.ABSORPTION, 12000, amethysts.size()));
//            amethysts.forEach(ElementAmethyst::breakAmethyst);
//            return true;
//        } else {
//            this.sendTip(owner, "no_crystal_around");
//            return false;
//        }
//    }

}
