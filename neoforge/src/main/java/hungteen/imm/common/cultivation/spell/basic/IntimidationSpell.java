package hungteen.imm.common.cultivation.spell.basic;

import hungteen.htlib.util.helper.impl.EffectHelper;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.HTHitResult;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.api.enums.SpellUsageCategories;
import hungteen.imm.common.cultivation.CultivationManager;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import hungteen.imm.common.effect.IMMEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/8/18 16:56
 */
public class IntimidationSpell extends SpellTypeImpl {

    public IntimidationSpell() {
        super("intimidation", properties(SpellUsageCategories.CUSTOM).maxLevel(1).cd(300).mana(20));
    }

    public static boolean canUseOn(LivingEntity owner, LivingEntity target){
        final double range = CultivationManager.getSpiritRange(owner);
        return EntityHelper.isEntityValid(target) && owner.closerThan(target, range) && CultivationManager.hasRealmGapAndLarger(owner, target);
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        final double range = CultivationManager.getSpiritRange(owner);
        final AABB aabb = EntityHelper.getEntityAABB(owner, range, range / 2);
        final RealmType realm = CultivationManager.getRealm(owner);
        EntityHelper.getPredicateEntities(owner, aabb, LivingEntity.class, entity -> {
            return CultivationManager.hasRealmGapAndLarger(owner, entity);
        }).forEach(target -> { //TODO 防止误伤队友。
            final RealmType targetRealm = CultivationManager.getRealm(target);
            final int gap = CultivationManager.getRealmGap(realm, targetRealm);
            // 威压百分比扣血，但不会扣完。
            target.setHealth(Math.max(1F, (float) (target.getHealth() * Math.pow(0.8F, gap))));
            target.addEffect(EffectHelper.viewEffect(IMMEffects.OPPRESSION.holder(), 600, gap));
        });
        return true;
    }

}
