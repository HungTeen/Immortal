package hungteen.imm.common.tag;

import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.util.Util;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/3/2 11:26
 */
public interface IMMEntityTags {

    /* Uniform */

    TagKey<EntityType<?>> VILLAGERS = uniformTag("villagers");
    TagKey<EntityType<?>> PILLAGERS = uniformTag("pillagers");

    /* IMM */

    TagKey<EntityType<?>> NO_ELEMENT_REACTIONS = tag("no_element_reactions");
    TagKey<EntityType<?>> REQUIRE_ELEMENT_DISPLAY_ENTITIES = tag("require_element_display_entities");

    TagKey<EntityType<?>> CULTIVATORS = tag("cultivators");
    TagKey<EntityType<?>> HUMAN_BEINGS = tag("human_beings");

    TagKey<EntityType<?>> COMMON_ARTIFACTS = tag("artifacts/common");
    TagKey<EntityType<?>> MODERATE_ARTIFACTS = tag("artifacts/moderate");
    TagKey<EntityType<?>> ADVANCED_ARTIFACTS = tag("artifacts/advanced");

    TagKey<EntityType<?>> REALM_FOLLOW_OWNER_ENTITIES = uniformTag("realm_follow_owner_entities");

    private static TagKey<EntityType<?>> tag(String name){
        return EntityHelper.get().tag(Util.prefix(name));
    }

    private static TagKey<EntityType<?>> uniformTag(String name){
        return EntityHelper.get().uniformTag(name);
    }

    private static TagKey<EntityType<?>> mcTag(String name){
        return EntityHelper.get().vanillaTag(name);
    }

}
