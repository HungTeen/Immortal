package hungteen.imm.common.cultivation.spell.metal;

import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.spell.SpellCastContext;
import hungteen.imm.api.spell.TriggerCondition;
import hungteen.imm.common.cultivation.*;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import javax.annotation.Nullable;

/**
 * @author HungTeen
 * @program Immortal
 * @create 2023-08-28 18:51
 **/
public class SharpnessSpell extends SpellTypeImpl {

    public SharpnessSpell() {
        super("sharpness", property(InscriptionTypes.ANY).qi(25).cd(50).maxLevel(1));
    }

    /**
     * 受到的伤害变成原来的 1.5 倍。
     */
    @Override
    public boolean checkActivate(SpellCastContext context) {
        if (context.getEventOpt().isPresent() && context.owner() instanceof LivingEntity entity && ElementManager.hasElement(entity, Element.METAL, true)) {
            if (context.getEvent() instanceof LivingIncomingDamageEvent event && isMeleeAttack(event.getSource())) {
                event.setAmount(event.getOriginalAmount() * 1.5F * context.scale());
                return true;
            }
        }
        return false;
    }

    /**
     * 受到的伤害变成原来的 1.5 倍。
     * @param owner 造成伤害的实体。
     */
    public static void checkSharpening(@Nullable Entity owner, LivingIncomingDamageEvent event){
        if(owner instanceof LivingEntity entity){
            SpellManager.activateSpell(entity, SpellTypes.SHARPNESS, ctx -> {
                ctx.setEvent(event);
            });
        }
    }

    @Override
    public boolean compatWith(TriggerCondition condition, int level) {
        return condition == TriggerConditions.ATTACK;
    }

    private static boolean isMeleeAttack(DamageSource source) {
        return source.is(DamageTypes.MOB_ATTACK) || source.is(DamageTypes.PLAYER_ATTACK);
    }
}
