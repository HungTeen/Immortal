package hungteen.imm.common.tag;

import hungteen.htlib.util.helper.registry.EntityHelper;
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

    /* forge */
    TagKey<EntityType<?>> VILLAGERS = forgeTag("villagers");
    TagKey<EntityType<?>> PILLAGERS = forgeTag("pillagers");

    /* immortal */
    TagKey<EntityType<?>> CULTIVATORS = tag("cultivators");
    TagKey<EntityType<?>> HUMAN_BEINGS = tag("human_beings");


    private static TagKey<EntityType<?>> tag(String name){
        return create(Util.prefix(name));
    }

    private static TagKey<EntityType<?>> forgeTag(String name){
        return create(Util.forgePrefix(name));
    }

    private static TagKey<EntityType<?>> mcTag(String name){
        return create(Util.mcPrefix(name));
    }

    private static TagKey<EntityType<?>> create(ResourceLocation location) {
        return EntityHelper.get().tag(location);
    }

}
