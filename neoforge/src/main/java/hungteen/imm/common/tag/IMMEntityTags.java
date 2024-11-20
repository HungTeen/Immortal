package hungteen.imm.common.tag;

import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.util.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/2 11:26
 */
public interface IMMEntityTags {

    /* Forge */

    TagKey<EntityType<?>> VILLAGERS = forgeTag("villagers");
    TagKey<EntityType<?>> PILLAGERS = forgeTag("pillagers");
    TagKey<EntityType<?>> REALM_FOLLOW_OWNER_ENTITIES = forgeTag("realm_follow_owner_entities");
    TagKey<EntityType<?>> COMMON_ARTIFACTS = forgeTag("artifacts/common");
    TagKey<EntityType<?>> MODERATE_ARTIFACTS = forgeTag("artifacts/moderate");
    TagKey<EntityType<?>> ADVANCED_ARTIFACTS = forgeTag("artifacts/advanced");

    /* Immortal */

    TagKey<EntityType<?>> NO_ELEMENT_REACTIONS = tag("no_element_reactions");
    TagKey<EntityType<?>> CULTIVATORS = tag("cultivators");
    TagKey<EntityType<?>> HUMAN_BEINGS = tag("human_beings");



    private static TagKey<EntityType<?>> tag(String name){
        return create(Util.prefix(name));
    }

    private static TagKey<EntityType<?>> forgeTag(String name){
        return create(Util.neo().prefix(name));
    }

    private static TagKey<EntityType<?>> mcTag(String name){
        return create(Util.mc().prefix(name));
    }

    private static TagKey<EntityType<?>> create(ResourceLocation location) {
        return EntityHelper.get().tag(location);
    }

}
