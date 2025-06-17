package hungteen.imm.common.tag;

import hungteen.htlib.util.helper.impl.DamageHelper;
import hungteen.imm.util.Util;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/8/22 11:07
 */
public interface IMMDamageTypeTags {

    /* Uniform */

    TagKey<DamageType> MELEE_DAMAGES = uniformTag("melee_damages");

    /* Immortal */

    TagKey<DamageType> IGNORE_REALM = tag("imm_realms/ignore_realm");
    TagKey<DamageType> IMM_REALM_LEVEL_1 = tag("imm_realms/level_1");
    TagKey<DamageType> IMM_REALM_LEVEL_2 = tag("imm_realms/level_2");
    TagKey<DamageType> IMM_REALM_LEVEL_3 = tag("imm_realms/level_3");
    TagKey<DamageType> QI_DAMAGES = tag("qi_damages");
    TagKey<DamageType> SPIRIT_DAMAGES = tag("spirit_damages");
    TagKey<DamageType> ELEMENTS = tag("elements");

    private static TagKey<DamageType> tag(String name){
        return DamageHelper.get().tag(Util.prefix(name));
    }

    private static TagKey<DamageType> uniformTag(String name){
        return DamageHelper.get().uniformTag(name);
    }

}
