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
public class IMMEntityTags {

    /* forge */
    public static final TagKey<EntityType<?>> VILLAGERS = forgeTag("villagers");
    public static final TagKey<EntityType<?>> PILLAGERS = forgeTag("pillagers");

    /* immortal */
    public static final TagKey<EntityType<?>> CULTIVATORS = tag("cultivators");
    public static final TagKey<EntityType<?>> HUMAN_BEINGS = tag("human_beings");


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
