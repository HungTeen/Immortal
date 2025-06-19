package hungteen.imm.common.cultivation.spell.wood;

import hungteen.imm.api.spell.SpellCastContext;
import hungteen.imm.api.spell.SpellUsageCategory;
import hungteen.imm.common.cultivation.spell.RequireEmptyHandSpell;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/10/7 22:57
 **/
public class WitherSpell extends RequireEmptyHandSpell {

    public WitherSpell() {
        super("wither", property(SpellUsageCategory.TRIGGERED_PASSIVE).maxLevel(1).qi(50).cd(450));
    }

    @Override
    public boolean checkActivate(SpellCastContext context) {
        return false;
    }

//    @Override
//    public boolean checkActivate(LivingEntity owner, HTHitResult result, InteractionHand hand, int level) {
//        if (result.getEntity() instanceof LivingEntity living) {
//            living.addEffect(EffectHelper.viewEffect(MobEffects.WITHER, 200, 1));
//            return true;
//        }
//        return false;
//    }

    @Override
    public boolean checkActivate(SpellCastContext context, InteractionHand hand) {
        return false;
    }

    /**
     * 实体加入世界时，判断是否是被发出的箭，如果是则附加凋零效果。
     */
    public static void checkWitherArrow(Entity projectile) {
//        if (projectile instanceof Arrow arrow && arrow.getOwner() instanceof LivingEntity living) {
//            SpellManager.activateSpell(living, SpellTypes.WITHER, (p, result, spell, level) -> {
//                arrow.addEffect(EffectHelper.viewEffect(MobEffects.WITHER, 240, 1));
//                return true;
//            });
//        }
    }

}
