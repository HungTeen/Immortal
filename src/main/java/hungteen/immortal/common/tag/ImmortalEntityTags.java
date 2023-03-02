package hungteen.immortal.common.tag;

import hungteen.immortal.utils.Util;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/2 11:26
 */
public class ImmortalEntityTags {

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
        return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, location);
    }

}
