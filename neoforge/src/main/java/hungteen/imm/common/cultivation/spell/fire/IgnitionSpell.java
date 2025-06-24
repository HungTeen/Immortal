package hungteen.imm.common.cultivation.spell.fire;

import hungteen.htlib.util.helper.impl.ParticleHelper;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.spell.SpellCastContext;
import hungteen.imm.api.spell.SpellUsageCategory;
import hungteen.imm.client.particle.IMMParticles;
import hungteen.imm.common.cultivation.ElementManager;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import hungteen.imm.util.EntityUtil;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;

import java.util.Optional;

/**
 * 引燃目标。
 * @program Immortal
 * @author HungTeen
 * @create 2023-08-27 19:30
 **/
public class IgnitionSpell extends SpellTypeImpl {

    private static final float MAX_FIRE_AMOUNT = 15;
    private static final float FIRE_AMOUNT = 10;

    public IgnitionSpell() {
        super("ignition", property(SpellUsageCategory.DEBUFF_TARGET).qi(10).cd(60).maxLevel(1));
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
            // 如果目标已经在燃烧或在水中，则不激活。
            if(target.isOnFire() || target.isInWaterRainOrBubble()) {
                return false;
            }
            // 添加火元素。
            ElementManager.addElementAmount(target, Element.FIRE, false, FIRE_AMOUNT, MAX_FIRE_AMOUNT);
            target.setRemainingFireTicks(Mth.floor(60 * context.scale()));
            ParticleHelper.spawnLineMovingParticle(context.level(), IMMParticles.QI.get(), context.owner().getEyePosition(), target.getEyePosition(), 1, 0.1, 0.1);
            handOpt.ifPresent(hand -> context.owner().swing(hand));
            return true;
        }
        sendTip(context, NO_TARGET);
        return false;
    }

    @Override
    public Optional<SoundEvent> getTriggerSound() {
        return Optional.of(SoundEvents.BLAZE_BURN);
    }
}
