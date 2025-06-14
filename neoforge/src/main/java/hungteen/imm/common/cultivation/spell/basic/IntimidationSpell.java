package hungteen.imm.common.cultivation.spell.basic;

import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.htlib.util.helper.impl.ParticleHelper;
import hungteen.imm.api.HTHitResult;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.api.spell.SpellUsageCategory;
import hungteen.imm.client.particle.IMMParticles;
import hungteen.imm.common.cultivation.CultivationManager;
import hungteen.imm.common.cultivation.RealmManager;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;

/**
 * 威压法术。
 * @author PangTeen
 * @program Immortal
 * @create 2023/8/18 16:56
 */
public class IntimidationSpell extends SpellTypeImpl {

    public IntimidationSpell() {
        super("intimidation", properties(SpellUsageCategory.CUSTOM_BUFF).maxLevel(1).cd(300).mana(20));
    }

    public static boolean canUseOn(LivingEntity owner, LivingEntity target){
        final double range = CultivationManager.getSpiritRange(owner);
        return EntityHelper.isEntityValid(target) && owner.closerThan(target, range) && RealmManager.hasRealmGapAndLarger(owner, target);
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        if(owner instanceof Mob mob && mob.getTarget() == null){
            return false;
        }
        final double range = CultivationManager.getSpiritRange(owner);
        final AABB aabb = EntityHelper.getEntityAABB(owner, range, range / 2);
        final RealmType realm = RealmManager.getRealm(owner);
        EntityHelper.getPredicateEntities(owner, aabb, LivingEntity.class, entity -> {
            return RealmManager.hasRealmGapAndLarger(owner, entity);
        }).forEach(target -> {
            //TODO 防止误伤队友。
            final RealmType targetRealm = RealmManager.getRealm(target);
            final float gap = RealmManager.getRealmGap(realm, targetRealm);
            // 威压百分比扣血，但不会扣完。
//            target.setHealth(Math.max(1F, (float) (target.getHealth() * Math.pow(0.8F, gap))));
//            target.addEffect(EffectHelper.viewEffect(IMMEffects.OPPRESSION.holder(), 600, gap));
            if(target instanceof ServerPlayer player){
                PlayerHelper.playClientSound(player, SoundEvents.ELDER_GUARDIAN_CURSE);
                ParticleHelper.sendParticles(player.serverLevel(), IMMParticles.INTIMIDATION.get(), player.getX(), player.getY(), player.getZ(), 1, 0);
            }
        });
        return true;
    }

    @Override
    public int getCastingPriority(LivingEntity living) {
        if(living instanceof Mob mob){
            if(hasLargeRealm(living, mob.getTarget()) || hasLargeRealm(living, mob.getLastHurtByMob())){
                return HIGH;
            }
            return LOW;
        }
        return super.getCastingPriority(living);
    }

    private static boolean hasLargeRealm(LivingEntity owner, @Nullable LivingEntity target){
        return target != null && RealmManager.hasRealmGapAndLarger(owner, target);
    }
}
