package hungteen.imm.common.cultivation.spell.basic;

import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.htlib.util.helper.impl.EffectHelper;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.htlib.util.helper.impl.ParticleHelper;
import hungteen.imm.api.spell.SpellCastContext;
import hungteen.imm.api.spell.SpellUsageCategory;
import hungteen.imm.client.particle.IMMParticles;
import hungteen.imm.common.cultivation.CultivationManager;
import hungteen.imm.common.cultivation.RealmManager;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import hungteen.imm.common.entity.effect.IMMEffects;
import hungteen.imm.util.DamageUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

/**
 * 威压法术。
 * @author PangTeen
 * @program Immortal
 * @create 2023/8/18 16:56
 */
public class IntimidationSpell extends SpellTypeImpl {

    public IntimidationSpell() {
        super("intimidation", property(SpellUsageCategory.CUSTOM_BUFF).maxLevel(1).cd(300).qi(10));
    }

    public static boolean canUseOn(LivingEntity owner, LivingEntity target){
        final double range = CultivationManager.getSpiritRange(owner);
        return EntityHelper.isEntityValid(target) && owner.closerThan(target, range) && RealmManager.hasRealmGapAndLarger(owner, target);
    }

    @Override
    public boolean checkActivate(SpellCastContext context) {
        // 生物只能对攻击目标施放威压。
        if(context.targetOpt().isPresent()){
            // 玩家需要检查神识范围。
            if(context.owner() instanceof Player){
                final double range = CultivationManager.getSpiritRange(context.owner());
                if(context.owner().distanceTo(context.target()) > range){
                    sendTip(context, TARGET_TOO_FAR_AWAY);
                    return false;
                }
            }
            if(RealmManager.hasRealmGapAndLarger(context.owner(), context.target())){
                final int gap = RealmManager.getRealmGap(context.owner(), context.target());
                // 威压百分比扣血，但不会扣完。
                if(context.target() instanceof LivingEntity living){
                     float damage = living.getHealth() - Math.max(1, (float) (living.getHealth() * Math.pow(0.8F, gap)));
                     living.hurt(DamageUtil.spirit(context.owner()), damage);
                     living.addEffect(EffectHelper.viewEffect(IMMEffects.OPPRESSION.holder(), Mth.ceil(600 * context.scale()), gap));
                }
                 if(context.target() instanceof ServerPlayer player){
                    PlayerHelper.playClientSound(player, SoundEvents.ELDER_GUARDIAN_CURSE);
                    ParticleHelper.sendParticles(player.serverLevel(), IMMParticles.INTIMIDATION.get(), player.getX(), player.getY(), player.getZ(), 1, 0);
                }
            } else {
                if(context.target() instanceof LivingEntity living && living.getHealth() > 1) {
                    context.target().hurt(DamageUtil.spirit(context.owner()), 1F);
                }
                sendTip(context, ONLY_VALID_FOR_LOW_REALM);
                return false;
            }
        } else {
            sendTip(context, NO_TARGET);
        }
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
