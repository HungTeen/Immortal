package hungteen.immortal.common.tag;

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
    public static final TagKey<Item> SPIRITUAL_STONES = forgeTag("spiritual_stones");
    public static final TagKey<Item> SPIRITUAL_STONES_LEVEL_ONE = forgeTag("spiritual_stone/level_one_stones");
    public static final TagKey<Item> SPIRITUAL_STONES_LEVEL_TWO = forgeTag("spiritual_stone/level_two_stones");

    /* immortal */

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
