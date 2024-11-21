package hungteen.imm.common.spell.spells.metal;

import hungteen.imm.api.cultivation.Element;
import hungteen.imm.common.ElementManager;
import hungteen.imm.common.spell.SpellManager;
import hungteen.imm.common.spell.SpellTypes;
import hungteen.imm.common.spell.spells.SpellType;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import javax.annotation.Nullable;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-08-28 18:51
 **/
public class SharpnessSpell extends SpellType {

    public SharpnessSpell() {
        super("sharpness", properties().mana(25).cd(50).maxLevel(1));
    }

    public static void checkSharpening(@Nullable Entity owner, LivingDamageEvent.Pre event){
        if(owner instanceof LivingEntity entity && isMeleeAttack(event.getSource()) && ElementManager.hasElement(entity, Element.METAL, true)){
            SpellManager.activateSpell(entity, SpellTypes.SHARPNESS, (p, result, spell, level) -> {
                event.setNewDamage(event.getNewDamage() * 1.5F);
                return true;
            });
        }
    }

    private static boolean isMeleeAttack(DamageSource source){
        return source.is(DamageTypes.MOB_ATTACK) || source.is(DamageTypes.PLAYER_ATTACK);
    }
}
