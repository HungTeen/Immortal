package hungteen.imm.common.spell.spells.basic;

import hungteen.htlib.util.helper.registry.EffectHelper;
import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.imm.api.HTHitResult;
import hungteen.imm.api.enums.SpellUsageCategories;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.common.RealmManager;
import hungteen.imm.common.effect.IMMEffects;
import hungteen.imm.common.spell.spells.SpellType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/8/18 16:56
 */
public class IntimidateSpell extends SpellType {

    public IntimidateSpell() {
        super("intimidate", properties(SpellUsageCategories.CUSTOM).maxLevel(1).cd(300).mana(20));
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        final double range = RealmManager.getSpiritRange(owner);
        final AABB aabb = EntityHelper.getEntityAABB(owner, range, range / 2);
        final IRealmType realm = RealmManager.getEntityRealm(owner);
        EntityHelper.getPredicateEntities(owner, aabb, LivingEntity.class, entity -> {
            final IRealmType targetRealm = RealmManager.getEntityRealm(entity);
            return RealmManager.hasRealmGap(realm, targetRealm) && RealmManager.compare(realm, targetRealm);
        }).forEach(target -> { //TODO 防止误伤队友。
            final IRealmType targetRealm = RealmManager.getEntityRealm(target);
            final int gap = RealmManager.getRealmGap(realm, targetRealm);
            target.addEffect(EffectHelper.viewEffect(IMMEffects.OPPRESSION.get(), 600, gap));
        });
        return true;
    }

}
