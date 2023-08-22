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

    TagKey<DamageType> SPIRITUALS = tag("spirituals");

    private static TagKey<DamageType> tag(String name){
        return create(Util.prefix(name));
    }

    private static TagKey<DamageType> create(ResourceLocation location) {
        return TagKey.create(Registries.DAMAGE_TYPE, location);
    }

}
