package hungteen.imm.common.cultivation.spell.spirit;

import hungteen.htlib.util.helper.impl.EffectHelper;
import hungteen.imm.api.spell.SpellCastContext;
import hungteen.imm.api.spell.SpellUsageCategory;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import hungteen.imm.util.EntityUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;

import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/10/7 22:57
 **/
public class WitherSpell extends SpellTypeImpl {

    public WitherSpell() {
        super("wither", property(SpellUsageCategory.TRIGGERED_PASSIVE).maxLevel(1).qi(50).cd(450));
    }

    @Override
    public boolean checkActivate(SpellCastContext context) {
        if(context.targetOpt().isPresent()){
            Optional<InteractionHand> handOpt = Optional.empty();
            if(! context.itemTrigger() ){
                handOpt = EntityUtil.getEmptyHand(context.owner());
                if(handOpt.isEmpty()){
                    sendTip(context, NO_EMPTY_HAND);
                    return false;
                }
            }
            Entity target = context.targetOpt().get();
            MobEffectInstance effectInstance = EffectHelper.viewEffect(MobEffects.WITHER, Mth.floor(200 * context.scale()), 1);
            if(target instanceof LivingEntity living){
                living.addEffect(effectInstance);
            } else if(target instanceof Arrow arrow){
                arrow.addEffect(effectInstance);
            } else {
                sendTip(context, NO_TARGET);
            }
            handOpt.ifPresent(hand -> context.owner().swing(hand));
            return true;
        }
        sendTip(context, NO_TARGET);
        return false;
    }

}
