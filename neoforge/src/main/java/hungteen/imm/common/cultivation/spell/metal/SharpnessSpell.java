package hungteen.imm.common.cultivation.spell.metal;

import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import javax.annotation.Nullable;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-08-28 18:51
 **/
public class SharpnessSpell extends SpellTypeImpl {

    public SharpnessSpell() {
        super("sharpness", property().mana(25).cd(50).maxLevel(1));
    }

    /**
     * 受到的伤害变成原来的 1.5 倍。
     * @param owner 造成伤害的实体。
     */
    public static void checkSharpening(@Nullable Entity owner, LivingIncomingDamageEvent event){
//        if(owner instanceof LivingEntity entity && isMeleeAttack(event.getSource()) && ElementManager.hasElement(entity, Element.METAL, true)){
//            SpellManager.activateSpell(entity, SpellTypes.SHARPNESS, (p, result, spell, level) -> {
//                event.setAmount(event.getOriginalAmount() * 1.5F);
//                return true;
//            });
//        }
    }

    private static boolean isMeleeAttack(DamageSource source){
        return source.is(DamageTypes.MOB_ATTACK) || source.is(DamageTypes.PLAYER_ATTACK);
    }
}
