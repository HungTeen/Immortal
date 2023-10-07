package hungteen.imm.common.misc.damage;

import hungteen.htlib.util.helper.registry.DamageHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-08-18 20:39
 **/
public class IMMDamageSources {

    public static DamageSource spiritualFlame(Entity causingEntity){
        return DamageHelper.source(causingEntity, IMMDamageTypes.SPIRITUAL_FLAME, causingEntity);
    }

    public static DamageSource spiritualMana(Entity causingEntity){
        return DamageHelper.source(causingEntity, IMMDamageTypes.SPIRITUAL_MANA, causingEntity);
    }

    public static DamageSource elementReaction(Entity causingEntity){
        return DamageHelper.source(causingEntity, IMMDamageTypes.SPIRITUAL_MANA, causingEntity);
    }

}
