package hungteen.imm.common.spell.spells;

import hungteen.imm.api.enums.Elements;
import hungteen.imm.common.ElementManager;
import hungteen.imm.common.spell.SpellManager;
import hungteen.imm.common.spell.SpellTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import javax.annotation.Nullable;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-08-28 18:51
 **/
public class SharpnessSpell extends SpellType {

    public SharpnessSpell() {
        super("sharpness", properties().mana(15).cd(20).maxLevel(1));
    }

    public static void checkSharpening(@Nullable Entity owner, LivingHurtEvent event){
        if(owner instanceof LivingEntity entity && isMeleeAttack(event.getSource()) && ElementManager.hasElement(entity, Elements.METAL, true)){
            SpellManager.activateSpell(entity, SpellTypes.SHARPNESS, (p, result, spell, level) -> {
                event.setAmount(event.getAmount() * 1.5F);
                return true;
            });
        }
    }

    private static boolean isMeleeAttack(DamageSource source){
        return source.is(DamageTypes.MOB_ATTACK) || source.is(DamageTypes.PLAYER_ATTACK);
    }
}
