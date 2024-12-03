package hungteen.imm.util;

import hungteen.imm.common.tag.IMMDamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/3 22:25
 **/
public class DamageUtil {

    /**
     * @return 是近战攻击。
     */
    public static boolean isMeleeDamage(DamageSource damageSource){
        return damageSource.is(IMMDamageTypeTags.MELEE_DAMAGES);
    }
}
