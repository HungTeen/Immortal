package hungteen.imm.common.tag;

import hungteen.imm.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/8/22 11:07
 */
public interface IMMDamageTypeTags {

    /* Forge */

    TagKey<DamageType> IGNORE_REALM = forgeTag("imm_realms/ignore_realm");
    TagKey<DamageType> IMM_REALM_LEVEL_1 = forgeTag("imm_realms/level_1");
    TagKey<DamageType> IMM_REALM_LEVEL_2 = forgeTag("imm_realms/level_2");
    TagKey<DamageType> IMM_REALM_LEVEL_3 = forgeTag("imm_realms/level_3");

    /* Immortal */

    TagKey<DamageType> SPIRITUALS = tag("spirituals");
    TagKey<DamageType> ELEMENTS = tag("elements");

    private static TagKey<DamageType> forgeTag(String name){
        return create(Util.neo().prefix(name));
    }

    private static TagKey<DamageType> tag(String name){
        return create(Util.prefix(name));
    }

    private static TagKey<DamageType> create(ResourceLocation location) {
        return TagKey.create(Registries.DAMAGE_TYPE, location);
    }

}
