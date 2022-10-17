package hungteen.immortal.tag;

import hungteen.immortal.utils.Util;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-16 13:08
 **/
public class ImmortalItemTags {

    /* forge */
    public static final TagKey<Item> ELIXIR_INGREDIENTS = forgeTag("elixir_ingredients");

    private static TagKey<Item> tag(String name){
        return net.minecraft.tags.ItemTags.create(Util.prefix(name));
    }

    private static TagKey<Item> forgeTag(String name){
        return net.minecraft.tags.ItemTags.create(Util.forgePrefix(name));
    }

    private static TagKey<Item> mcTag(String name){
        return net.minecraft.tags.ItemTags.create(Util.mcPrefix(name));
    }
}
