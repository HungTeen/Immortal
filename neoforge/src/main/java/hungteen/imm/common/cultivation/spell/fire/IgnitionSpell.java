package hungteen.imm.common.cultivation.spell.fire;

import hungteen.imm.api.spell.SpellCastContext;
import hungteen.imm.api.spell.SpellUsageCategory;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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
        super("ignition", property(SpellUsageCategory.DEBUFF_TARGET).mana(15).cd(60).maxLevel(1));
    }

    /**
     * 实体加入世界时，判断是否是被发出的箭，如果是则激活引燃。
     */
    public static void checkIgnitionArrow(Entity projectile){
//        if(projectile instanceof AbstractArrow arrow && arrow.getOwner() instanceof LivingEntity living){
//            SpellManager.activateSpell(living, SpellTypes.IGNITION, (p, result, spell, level) -> {
//                ElementManager.addElementAmount(arrow, Element.FIRE, false, FIRE_AMOUNT, MAX_FIRE_AMOUNT);
//                arrow.setRemainingFireTicks(100);
//                return true;
//            });
//        }
    }

    @Override
    public boolean checkActivate(SpellCastContext context) {
        return super.checkActivate(context);
    }

//    @Override
//    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
//        if(result.hasEntity() && result.getEntity() != null){
//            ElementManager.addElementAmount(result.getEntity(), Element.FIRE, false, FIRE_AMOUNT, MAX_FIRE_AMOUNT);
//            result.getEntity().setRemainingFireTicks(60);
//            ParticleHelper.spawnLineMovingParticle(owner.level(), IMMParticles.QI.get(), owner.getEyePosition(), result.getEntity().getEyePosition(), 1, 0.1, 0.1);
//            return true;
//        }
//        return false;
//    }

    @Override
    public Optional<SoundEvent> getTriggerSound() {
        return Optional.of(SoundEvents.BLAZE_BURN);
    }
}
